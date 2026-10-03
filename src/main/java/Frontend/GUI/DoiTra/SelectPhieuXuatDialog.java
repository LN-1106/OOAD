package Frontend.GUI.DoiTra;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Backend.DTO.PhieuXuat;
import Backend.DatabaseHelper;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;

public class SelectPhieuXuatDialog extends JDialog {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private PhieuXuat selectedPX = null;
    private CustomButton btnChon;
    private ArrayList<PhieuXuat> listPX = new ArrayList<>();

    public SelectPhieuXuatDialog() {
        setTitle("DANH SÁCH HÓA ĐƠN ĐÃ XUẤT");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        String[] header = {"Mã Phiếu Xuất", "Mã Khách Hàng", "Ngày Xuất", "Tổng Tiền"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tbl = new Table();
        tbl.setModel(tblModel);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        btnChon = new CustomButton("Chọn phiếu xuất", Theme.ACCENT_COLOR);

        btnChon.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                selectedPX = listPX.get(row);
                System.out.println("Đã chọn phiếu xuất: " + selectedPX.getMaPhieuXuat());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn!");
            }
        });
        
        JPanel pnlBottom = new JPanel();
        pnlBottom.add(btnChon);
        add(pnlBottom, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tblModel.setRowCount(0);
        listPX.clear();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Try multiple query variations to handle different boolean column types
        String[] sqlVariants = {
            "SELECT * FROM PhieuXuat WHERE TrangThai = true ORDER BY NgayXuat DESC",
            "SELECT * FROM PhieuXuat WHERE TrangThai = 1 ORDER BY NgayXuat DESC",
            "SELECT * FROM PhieuXuat ORDER BY NgayXuat DESC LIMIT 50"
        };
        
        for (String sql : sqlVariants) {
            try (java.sql.Connection conn = DatabaseHelper.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                 java.sql.ResultSet rs = ps.executeQuery()) {
                
                int count = 0;
                while (rs.next()) {
                    try {
                        String maPX = rs.getString("MaPhieuXuat");
                        java.sql.Timestamp ngayXuat = rs.getTimestamp("NgayXuat");
                        String maNV = rs.getString("MaNV");
                        String maKH = rs.getString("MaKH");
                        String maKM = rs.getString("MaKM");
                        double tongTien = rs.getDouble("TongTien");
                        boolean trangThai = rs.getBoolean("TrangThai");
                        
                        PhieuXuat px = new PhieuXuat(maPX, ngayXuat, maNV, maKH, maKM, tongTien, trangThai);
                        listPX.add(px);
                        
                        String ngayStr = "N/A";
                        if (ngayXuat != null) {
                            LocalDateTime ldt = ngayXuat.toLocalDateTime();
                            ngayStr = ldt.format(dtf);
                        }
                        
                        tblModel.addRow(new Object[]{
                            maPX,
                            maKH,
                            ngayStr,
                            String.format("%,.0f VNĐ", tongTien)
                        });
                        count++;
                    } catch (Exception rowEx) {
                        System.err.println("Lỗi xử lý dòng: " + rowEx.getMessage());
                    }
                }
                System.out.println("Đã load " + count + " phiếu xuất từ query: " + sql);
                if (count > 0) break; // Nếu có dữ liệu thì dừng
                
            } catch (Exception e) {
                System.err.println("Query thất bại: " + sql + " - " + e.getMessage());
            }
        }
        
        if (listPX.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có phiếu xuất nào! Kiểm tra database.");
        }
    }

    public PhieuXuat getSelectedPX() {
        return selectedPX;
    }
}
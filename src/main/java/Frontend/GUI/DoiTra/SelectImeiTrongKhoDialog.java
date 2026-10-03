package Frontend.GUI.DoiTra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;

public class SelectImeiTrongKhoDialog extends JDialog {
    private JTable tbl;
    private CustomButton btnChon, btnThoat;
    private DefaultTableModel tblModel;
    private String selectedImei = null;

    public SelectImeiTrongKhoDialog() {
        setTitle("CHỌN MÁY MỚI TRONG KHO ĐỂ THAY THẾ");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        initUI();
        loadData();
    }

    private void initUI() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHeader.setBackground(new Color(39, 174, 96)); // Màu xanh lá cho khác biệt
        JLabel lbl = new JLabel("Danh sách sản phẩm đang có sẵn trong kho");
        lbl.setForeground(Color.WHITE);
        pnlHeader.add(lbl);
        add(pnlHeader, BorderLayout.NORTH);

        String[] header = {"Mã IMEI", "Tên Sản Phẩm", "Giá Bán"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tbl = new Table();
        tbl.setModel(tblModel);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnChon = new CustomButton("Chọn máy này", Theme.ACCENT_COLOR);
        btnThoat = new CustomButton("Hủy bỏ", Theme.DANGER_COLOR);

        btnChon.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                selectedImei = tbl.getValueAt(row, 0).toString();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một máy để đổi cho khách!");
            }
        });

        btnThoat.addActionListener(e -> dispose());
        pnlBottom.add(btnChon);
        pnlBottom.add(btnThoat);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        tblModel.setRowCount(0);
        String sql = """
                SELECT 
                    ct.MaImei, 
                    sp.TenSP, 
                    pb.MauSac, 
                    pb.GiaBan
                FROM ChiTietSP ct
                JOIN PhienBanSP pb ON ct.MaPhienBan = pb.MaPhienBan
                JOIN SanPham sp ON pb.MaSP = sp.MaSP
                WHERE ct.TinhTrang = 'Trong kho' 
                AND ct.TrangThai = TRUE
            """;
        
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tenSP = rs.getString("TenSP");
                String mauSac = rs.getString("MauSac");
                double giaBan = rs.getDouble("GiaBan");
                tblModel.addRow(new Object[]{
                    rs.getString("MaImei"),
                    (tenSP != null ? tenSP : "N/A") + " (" + (mauSac != null ? mauSac : "N/A") + ")",
                    String.format("%,.0f VNĐ", giaBan)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSelectedImei() {
        return selectedImei;
    }
}
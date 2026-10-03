package Frontend.GUI.DoiTra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Backend.DAO.DoiTraDAO;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class SelectImeiDialog extends JDialog {

    private JTable tbl;
    private CustomButton btnChon, btnThoat;
    private DefaultTableModel tblModel;
    private String selectedImei = null;
    private String maPX;
    private DoiTraDAO dtDAO = new DoiTraDAO();

    public SelectImeiDialog(String maPX) {
        this.maPX = maPX;
        setTitle("CHỌN MÃ IMEI SẢN PHẨM");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        initUI();
        loadData();
    }

    private void initUI() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHeader.setBackground(new Color(41, 128, 185));
        JLabel lbl = new JLabel("Danh sách sản phẩm trong phiếu: " + maPX);
        lbl.setForeground(Color.WHITE);
        pnlHeader.add(lbl);
        add(pnlHeader, BorderLayout.NORTH);

        String[] header = {"Mã IMEI", "Tên Phiên Bản", "Tình Trạng"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tbl = new Table();
        tbl.setModel(tblModel);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnChon = new CustomButton("Chọn IMEI", Theme.ACCENT_COLOR);
        btnThoat = new CustomButton("Hủy bỏ", Theme.DANGER_COLOR);

        btnChon.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                selectedImei = tbl.getValueAt(row, 0).toString();
                dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng!");
            }
        });

        btnThoat.addActionListener(e -> dispose());

        pnlBottom.add(btnChon);
        pnlBottom.add(btnThoat);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        tblModel.setRowCount(0);
        ArrayList<String[]> listImei = getListImeiFromDB(maPX); 
        
        for (String[] row : listImei) {
            tblModel.addRow(row);
        }
    }

    private ArrayList<String[]> getListImeiFromDB(String maPX) {
        ArrayList<String[]> result = new ArrayList<>();
        String sql = """
                SELECT 
                    ct.MaImei, 
                    sp.TenSP, 
                    pb.MauSac, 
                    pb.CongSuat, 
                    ct.TinhTrang 
                FROM ChiTietSP ct
                JOIN PhienBanSP pb ON ct.MaPhienBan = pb.MaPhienBan
                JOIN SanPham sp ON pb.MaSP = sp.MaSP
                WHERE ct.MaPhieuXuat = ? 
                AND ct.TrangThai = TRUE
            """;
        
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPX);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("TenSP");
                String mauSac = rs.getString("MauSac");
                String congSuat = rs.getString("CongSuat");
                String tenHienThi = (maSP != null ? maSP : "N/A") + " [" + 
                               (mauSac != null ? mauSac : "N/A") + " - " + 
                               (congSuat != null ? congSuat : "N/A") + "]";
                String tinhTrang = rs.getString("TinhTrang");
                result.add(new String[]{
                    rs.getString("MaImei"),
                    tenHienThi,
                    (tinhTrang != null ? tinhTrang : "Chưa rõ")
                });
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn SelectImeiDialog: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public String getSelectedImei() {
        return selectedImei;
    }
}
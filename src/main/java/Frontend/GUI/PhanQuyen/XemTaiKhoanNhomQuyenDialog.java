package Frontend.GUI.PhanQuyen;

import Backend.DatabaseHelper;
import Frontend.Compoent.Table;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class XemTaiKhoanNhomQuyenDialog extends JDialog {
      private String maNQ;
      private String tenNQ;
      private DefaultTableModel model;

      public XemTaiKhoanNhomQuyenDialog(JFrame parent, String maNQ, String tenNQ) {
            super(parent, "Tài khoản thuộc: " + tenNQ, true);
            this.maNQ = maNQ;
            this.tenNQ = tenNQ;

            setSize(500, 350);
            setLocationRelativeTo(parent);
            setLayout(new MigLayout("fill, insets 10", "[grow]", "[]10[grow]"));

            initUI();
            loadData();
      }

      private void initUI() {
            JLabel lblTitle = new JLabel("Danh sách tài khoản nhóm: " + tenNQ);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            add(lblTitle, "wrap");

            String[] cols = { "Tên Đăng Nhập", "Mật Khẩu", "Mã NV", "Trạng Thái" };
            model = new DefaultTableModel(cols, 0) {
                  @Override
                  public boolean isCellEditable(int row, int column) {
                        return false;
                  }
            };

            Table tbl = new Table();
            tbl.setModel(model);

            // Căn giữa các cột cho đẹp
            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < 4; i++) {
                  tbl.getColumnModel().getColumn(i).setCellRenderer(center);
            }

            JScrollPane scrollPane = new JScrollPane(tbl);
            add(scrollPane, "grow");
      }

      private void loadData() {
            // Truy vấn trực tiếp vào bảng TaiKhoan để lấy dữ liệu
            String sql = "SELECT TenDangNhap, MatKhau, MaNV, TrangThai FROM TaiKhoan WHERE MaNhomQuyen = ?";

            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement pst = conn.prepareStatement(sql)) {

                  pst.setString(1, maNQ);
                  ResultSet rs = pst.executeQuery();

                  while (rs.next()) {
                        String ten = rs.getString("TenDangNhap");
                        String pass = rs.getString("MatKhau");
                        String manv = rs.getString("MaNV");
                        String tt = rs.getBoolean("TrangThai") ? "Hoạt động" : "Bị khóa";
                        model.addRow(new Object[] { ten, pass, manv, tt });
                  }
            } catch (Exception e) {
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu tài khoản!");
            }
      }
}
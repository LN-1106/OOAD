package Frontend.GUI.NhanVien;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Frame;

import Backend.BUS.NhanVienBUS;
import Backend.DTO.NhanVien;
import Frontend.Compoent.BaseThaoTacDialog;

public class NhanVienAddDialog extends BaseThaoTacDialog {
    private JTextField txtMa, txtTen, txtSDT, txtDiaChi, txtChucVu, txtEmail, txtLuong;
    private NhanVienBUS nvBUS = new NhanVienBUS();

    public NhanVienAddDialog(Frame parent) {
        super("THÊM NHÂN VIÊN", 500, 450);

        // Tự động lấy và điền mã mới
        String newMa = nvBUS.getNewMa();
        txtMa.setText(newMa);

        txtMa.setEditable(false);
        txtMa.setFocusable(false);

        SwingUtilities.invokeLater(() -> {
            txtTen.requestFocusInWindow();
        });
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã NV:"));
        txtMa = new JTextField();
        pnlContent.add(txtMa, "growx, h 35!");

        pnlContent.add(new JLabel("Tên NV:"));
        txtTen = new JTextField();
        pnlContent.add(txtTen, "growx, h 35!");

        pnlContent.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        pnlContent.add(txtSDT, "growx, h 35!");

        pnlContent.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlContent.add(txtDiaChi, "growx, h 35!");

        pnlContent.add(new JLabel("Chức vụ:"));
        txtChucVu = new JTextField();
        pnlContent.add(txtChucVu, "growx, h 35!");

        pnlContent.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlContent.add(txtEmail, "growx, h 35!");

        pnlContent.add(new JLabel("Lương:"));
        txtLuong = new JTextField();
        pnlContent.add(txtLuong, "growx, h 35!");
    }

    @Override
    protected void logicXacNhan() {
        try {
            double luong = txtLuong.getText().isEmpty() ? 0 : Double.parseDouble(txtLuong.getText());
            NhanVien nv = new NhanVien(
                    txtMa.getText(),
                    txtTen.getText(),
                    txtSDT.getText(),
                    txtDiaChi.getText(),
                    txtChucVu.getText(),
                    txtEmail.getText(),
                    luong,
                    true
            );
            String validationMsg = nvBUS.validate(nv, true); // true cho Add
            if (!validationMsg.equals("OK")) {
                JOptionPane.showMessageDialog(this, validationMsg, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (nvBUS.add(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số!");
        }
    }
}

package Frontend.GUI.NhanVien;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Backend.BUS.NhanVienBUS;
import Backend.DTO.NhanVien;
import Frontend.Compoent.BaseThaoTacDialog;

public class NhanVienFixDialog extends BaseThaoTacDialog {
    private JTextField txtMa, txtTen, txtSDT, txtDiaChi, txtChucVu, txtEmail, txtLuong;
    private NhanVienBUS nvBUS = new NhanVienBUS();
    private String ma, ten, sdt, diaChi, chucVu, email;
    private double luong;

    public NhanVienFixDialog(String ma, String ten, String sdt, String diaChi,
                             String chucVu, String email, double luong) {
        super("SỬA NHÂN VIÊN", 500, 450);
        txtMa.setText(ma);
        txtTen.setText(ten);
        txtSDT.setText(sdt);
        txtDiaChi.setText(diaChi);
        txtChucVu.setText(chucVu);
        txtEmail.setText(email);
        txtLuong.setText(String.valueOf(luong));
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã NV:"));
        txtMa = new JTextField();
        txtMa.setText(ma);
        txtMa.setEditable(false);
        txtMa.setFocusable(false);
        pnlContent.add(txtMa, "growx, h 35!");

        pnlContent.add(new JLabel("Tên NV:"));
        txtTen = new JTextField();
        txtTen.setText(ten);
        pnlContent.add(txtTen, "growx, h 35!");

        pnlContent.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        txtSDT.setText(sdt);
        pnlContent.add(txtSDT, "growx, h 35!");

        pnlContent.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        txtDiaChi.setText(diaChi);
        pnlContent.add(txtDiaChi, "growx, h 35!");

        pnlContent.add(new JLabel("Chức vụ:"));
        txtChucVu = new JTextField();
        txtChucVu.setText(chucVu);
        pnlContent.add(txtChucVu, "growx, h 35!");

        pnlContent.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        txtEmail.setText(email);
        pnlContent.add(txtEmail, "growx, h 35!");

        pnlContent.add(new JLabel("Lương:"));
        txtLuong = new JTextField();
        txtLuong.setText(String.valueOf(luong));
        pnlContent.add(txtLuong, "growx, h 35!");
    }

    @Override
    protected void logicXacNhan() {
        try {
            double luong = Double.parseDouble(txtLuong.getText());
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
            String validationMsg = nvBUS.validate(nv, false); // false cho Update
            if (!validationMsg.equals("OK")) {
                JOptionPane.showMessageDialog(this, validationMsg, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (nvBUS.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số!");
        }
    }
}

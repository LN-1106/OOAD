package Frontend.GUI.KhachHang;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Backend.BUS.KhachHangBUS;
import Backend.DTO.KhachHang;
import Frontend.Compoent.BaseThaoTacDialog;

public class KhachHangFixDialog extends BaseThaoTacDialog {
    private JTextField txtMa, txtTen, txtSDT, txtDiaChi;
    private KhachHangBUS khBUS = new KhachHangBUS();

    public KhachHangFixDialog(String ma, String ten, String sdt, String diaChi) {
        super("SỬA KHÁCH HÀNG", 450, 350);

        txtMa.setText(ma);
        txtTen.setText(ten);
        txtSDT.setText(sdt);
        txtDiaChi.setText(diaChi);

        txtMa.setEditable(false);
        txtMa.setFocusable(false);
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã KH:"));
        txtMa = new JTextField();
        pnlContent.add(txtMa, "growx, h 35!");

        pnlContent.add(new JLabel("Tên KH:"));
        txtTen = new JTextField();
        pnlContent.add(txtTen, "growx, h 35!");

        pnlContent.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        pnlContent.add(txtSDT, "growx, h 35!");

        pnlContent.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlContent.add(txtDiaChi, "growx, h 35!");
    }

    @Override
    protected void logicXacNhan() {
        KhachHang kh = new KhachHang(
                txtMa.getText(),
                txtTen.getText(),
                txtSDT.getText(),
                txtDiaChi.getText(),
                true
        );
        String msg = khBUS.validate(kh, false);
        if (!msg.equals("OK")) {
            JOptionPane.showMessageDialog(this, msg, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (khBUS.update(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
}

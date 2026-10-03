package Frontend.GUI.KhachHang;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Backend.BUS.KhachHangBUS;
import Backend.DTO.KhachHang;
import Frontend.Compoent.BaseThaoTacDialog;

public class KhachHangAddDialog extends BaseThaoTacDialog {
    private JTextField txtMa, txtTen, txtSDT, txtDiaChi;
    private KhachHangBUS khBUS = new KhachHangBUS();
    private KhachHangTable parentTable;

    public KhachHangAddDialog() {
        super("THÊM KHÁCH HÀNG", 450, 350);

        // Tự động lấy và điền mã mới
        String newMa = khBUS.getNewMa();
        txtMa.setText(newMa);

        txtMa.setEditable(false);
        txtMa.setFocusable(false);

        SwingUtilities.invokeLater(() -> {
            txtTen.requestFocusInWindow();
        });
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
        String msg = khBUS.validate(kh, true);
        if (!msg.equals("OK")) {
            JOptionPane.showMessageDialog(this, msg, "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (khBUS.add(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!");
        }
    }
}

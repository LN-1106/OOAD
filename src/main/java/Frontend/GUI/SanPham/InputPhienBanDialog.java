package Frontend.GUI.SanPham;

import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Backend.BUS.PhienBanSanPhamBUS;
import Backend.DTO.PhienBanSanPham;
import Frontend.Compoent.ThaoTacDialog;

public class InputPhienBanDialog extends ThaoTacDialog {
    private JTextField txtMaPB, txtMauSac, txtRam, txtDungLuongPin, txtBoNhoTrong, txtGiaNhap, txtGiaBan, txtSoLuong;
    private PhienBanSanPhamBUS pbBUS = new PhienBanSanPhamBUS();
    private boolean isUpdate = false;
    private boolean isSuccess = false;
    private String maSP;

    public InputPhienBanDialog(Window parent, String maSP, PhienBanSanPham pb) {
        super(parent, pb == null ? "THÊM PHIÊN BẢN MỚI" : "CẬP NHẬT PHIÊN BẢN", 450, 500);

        this.maSP = maSP;
        this.isUpdate = (pb != null);

        if (isUpdate) {
            fillData(pb);
        } else {
            txtSoLuong.setText("0");
            txtMaPB.setText(pbBUS.getNextID());
        }
        txtMaPB.setEditable(false);
        txtMaPB.setFocusable(false);
        txtSoLuong.setEditable(false);
        txtSoLuong.setFocusable(false);
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã Phiên Bản:"));
        txtMaPB = new JTextField();
        pnlContent.add(txtMaPB, "growx,h 35!");

        pnlContent.add(new JLabel("Màu Sắc:"));
        txtMauSac = new JTextField();
        pnlContent.add(txtMauSac, "growx,h 35!");

        pnlContent.add(new JLabel("Ram:"));
        txtRam = new JTextField();
        pnlContent.add(txtRam, "growx,h 35!");

        pnlContent.add(new JLabel("Dung Lượng Pin:"));
        txtDungLuongPin = new JTextField();
        pnlContent.add(txtDungLuongPin, "growx,h 35!");

        pnlContent.add(new JLabel("Bộ nhớ trong:"));
        txtBoNhoTrong = new JTextField();
        pnlContent.add(txtBoNhoTrong, "growx,h 35!");

        pnlContent.add(new JLabel("Giá Nhập:"));
        txtGiaNhap = new JTextField();
        pnlContent.add(txtGiaNhap, "growx,h 35!");

        pnlContent.add(new JLabel("Giá Bán:"));
        txtGiaBan = new JTextField();
        pnlContent.add(txtGiaBan, "growx,h 35!");

        pnlContent.add(new JLabel("Số Lượng Tồn:"));
        txtSoLuong = new JTextField();
        pnlContent.add(txtSoLuong, "growx,h 35!");
    }

    @Override
    protected void logicXacNhan() {

        try {
            double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
            double giaBan = Double.parseDouble(txtGiaBan.getText().trim());

            if (giaNhap < 0 || giaBan < 0) {
                JOptionPane.showMessageDialog(this, "Giá tiền không được âm");
                return;
            }

            PhienBanSanPham pb = new PhienBanSanPham();
            pb.setMaPhienBan(txtMaPB.getText().trim());
            pb.setMaSP(this.maSP);
            pb.setMauSac(txtMauSac.getText().trim());
            pb.setRam(txtRam.getText().trim());
            pb.setBoNhoTrong(txtBoNhoTrong.getText().trim());
            pb.setDungLuongPin(txtDungLuongPin.getText().trim());
            pb.setGiaNhap(giaNhap);
            pb.setGiaBan(giaBan);
            pb.setSoLuongTon(Integer.parseInt(txtSoLuong.getText().trim()));
            pb.setTrangThai(true);

            boolean ketQua = isUpdate ? pbBUS.update(pb) : pbBUS.add(pb);

            if (ketQua) {
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                this.isSuccess = true;
                this.dispose();
            } else
                JOptionPane.showMessageDialog(this, "Lưu thất bại");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void fillData(PhienBanSanPham pb) {
        if (pb == null)
            return;
        txtMaPB.setText(pb.getMaPhienBan());
        txtMauSac.setText(pb.getMauSac());  
        txtRam.setText(pb.getRam());
        txtDungLuongPin.setText(pb.getDungLuongPin());
        txtBoNhoTrong.setText(pb.getBoNhoTrong());
        txtGiaNhap.setText(String.format("%.0f", pb.getGiaNhap()));
        txtGiaBan.setText(String.format("%.0f", pb.getGiaBan()));
        txtSoLuong.setText(String.valueOf(pb.getSoLuongTon()));
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
package Frontend.GUI.SanPham;

import Backend.BUS.ChiTietSPBUS;
import Backend.DTO.ChiTietSP;
import Frontend.Compoent.ThaoTacDialog;
import javax.swing.*;
import java.awt.*;

public class InputChiTietDialog extends ThaoTacDialog  {

    private JTextField txtIMEI, txtMaPB, txtMaPN, txtMaPX;
    private JComboBox<String> cboTinhTrang;
    private ChiTietSPBUS ctBUS = new ChiTietSPBUS();
    private boolean isSuccess = false;
    private String maPB;

    public InputChiTietDialog(JFrame parent, String maPB, ChiTietSP ct) {
        super(parent, "CẬP NHẬT THÔNG TIN", 450, 500);
        this.maPB = maPB;
        
        if (ct != null) {
            fillData(ct);
        }

        txtMaPB.setEditable(false);
        txtMaPB.setFocusable(false);
        txtMaPN.setEditable(false);
        txtMaPN.setFocusable(false);
        txtMaPX.setEditable(false);
        txtMaPX.setFocusable(false);
        txtIMEI.setEditable(false);
        txtIMEI.setFocusable(false);
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã IMEI:")); 
        txtIMEI = new JTextField();
        pnlContent.add(txtIMEI, "growx,h 35!");
        
        pnlContent.add(new JLabel("Mã Phiên Bản:"));
        txtMaPB = new JTextField();
        pnlContent.add(txtMaPB, "growx,h 35!");

        pnlContent.add(new JLabel("Mã Phiếu Nhập:"));
        txtMaPN = new JTextField();
        pnlContent.add(txtMaPN, "growx,h 35!");

        pnlContent.add(new JLabel("Mã Phiếu Xuất:"));
        txtMaPX = new JTextField();
        pnlContent.add(txtMaPX, "growx,h 35!");

        pnlContent.add(new JLabel("Tình Trạng:"));
        cboTinhTrang = new JComboBox<>(new String[]{"Trong kho", "Lỗi"});
        cboTinhTrang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTinhTrang.setBackground(Color.WHITE);
        pnlContent.add(cboTinhTrang, "growx, h 35!");
    }

    private void fillData(ChiTietSP ct) {
        txtMaPB.setText(ct.getMaPhienBan());
        txtIMEI.setText(ct.getMaImei());
        txtMaPN.setText(ct.getMaPhieuNhap() == null ? "N/A" : ct.getMaPhieuNhap());
        txtMaPX.setText(ct.getMaPhieuXuat() == null ? "N/A" : ct.getMaPhieuXuat());
        cboTinhTrang.setSelectedItem(ct.getTinhTrang());
    }

    @Override
    protected void logicXacNhan() {
        ChiTietSP ct = new ChiTietSP();
        ct.setMaImei(txtIMEI.getText().trim());
        ct.setMaPhienBan(maPB);
        ct.setTinhTrang(cboTinhTrang.getSelectedItem().toString());
        String pn = txtMaPN.getText().trim();
        ct.setMaPhieuNhap(pn.equals("N/A") ? null : pn); 
        String px = txtMaPX.getText().trim();
        ct.setMaPhieuXuat(px.equals("N/A") ? null : px);

        if (ctBUS.update(ct)) {
            isSuccess = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            isSuccess = false;
        }
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
}
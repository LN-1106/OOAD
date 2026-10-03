package Frontend.GUI.SanPham;

import Frontend.Compoent.ThaoTacDialog;
import Backend.BUS.SanPhamBUS;
import Backend.DTO.SanPham;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ThemSanPhamDialog extends ThaoTacDialog {

    private SanPhamBUS spBUS = new SanPhamBUS();
    private JTextField txtMaSP, txtTenSP, txtBaoHanh;
    private JComboBox<String> cboMaLoai,cboMaHang;
    private JTextArea txtMoTa;
    private boolean isSuccess = false;
    private JFrame parentFrame;

    public ThemSanPhamDialog(JFrame parent) {
        super(parent, "THÊM SẢN PHẨM", 550, 480);
        this.parentFrame = parent; 

        txtMaSP.setText(spBUS.getNextID());
        txtMaSP.setEditable(false);
        txtMaSP.setFocusable(false);

        loadDataComboBox();
    }

    @Override
    protected void initForm() {
        pnlContent.add(new JLabel("Mã sản phẩm: "));
        txtMaSP = new JTextField();
        pnlContent.add(txtMaSP, "growx,h 35!");

        pnlContent.add(new JLabel("Tên sản phẩm: "));
        txtTenSP = new JTextField();
        pnlContent.add(txtTenSP, "growx, h 35!");

        pnlContent.add(new JLabel("Mã loại: "));
        cboMaLoai=new JComboBox<>();
        cboMaLoai.setBackground(Color.WHITE);
        pnlContent.add(cboMaLoai, "growx, h 35!");

        pnlContent.add(new JLabel("Mã Hãng: "));
        cboMaHang=new JComboBox<>();
        cboMaHang.setBackground(Color.WHITE);
        pnlContent.add(cboMaHang, "growx, h 35!");

        pnlContent.add(new JLabel("Bảo hành (tháng): "));
        txtBaoHanh = new JTextField("12");
        pnlContent.add(txtBaoHanh, "growx, h 35!");

        pnlContent.add(new JLabel("Mô tả sản phẩm: "), "aligny top");
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        pnlContent.add(scrollMoTa, "growx, h 100!");
    }

    @Override
    protected void logicXacNhan() {
        if (txtTenSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên sản phẩm!");
            return;
        }
        try{
            SanPham sp = new SanPham();
            sp.setMaSP(txtMaSP.getText().trim());
            sp.setTenSP(txtTenSP.getText().trim());
            sp.setMaLoai(cboMaLoai.getSelectedItem().toString().split(" - ")[0].trim());
            sp.setMaHang(cboMaHang.getSelectedItem().toString().split(" - ")[0].trim());
            sp.setMoTa(txtMoTa.getText().trim());
            sp.setThoiGianBaoHanh(Integer.parseInt(txtBaoHanh.getText().trim()));
            sp.setTrangThai(true);

            if (spBUS.add(sp)) {
                this.isSuccess = true;
                int confirm = JOptionPane.showConfirmDialog(this,"Thêm sản phẩm thành công\nBạn có muốn thiết lập Phiên Bản ngay không?", "Thông báo",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                this.dispose();   
                if (confirm == JOptionPane.YES_OPTION) {
                    PhienBanSPDialog pbDlg = new PhienBanSPDialog(parentFrame, sp.getMaSP(), sp.getTenSP(), true);
                    pbDlg.setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        }catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Thời gian bảo hành phải là số nguyên");
        } 

    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private void loadDataComboBox() {

        ArrayList<String> dsLoai = spBUS.getDanhSachLoai(); 
        for (String chuoiLoai : dsLoai) {
            cboMaLoai.addItem(chuoiLoai);
        }

        ArrayList<String> dsHang = spBUS.getDanhSachHang(); 
        for (String chuoiHang : dsHang) {
            cboMaHang.addItem(chuoiHang);
        }
    }
}
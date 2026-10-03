package Frontend.GUI.SanPham;

import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import Backend.DTO.SanPham;
import Backend.BUS.SanPhamBUS;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import java.util.ArrayList;

public class SuaSanPhamDialog extends JDialog {

    private JTextField txtMaSP, txtTenSP, txtSoLuong, txtBaoHanh;
    private JTextArea txtMoTa;
    private JComboBox<String> cboMaLoai,cboMaHang;
    private CustomButton btnLuu, btnSuaPhienBan, btnHuy;
    private boolean isSuccess = false;

    SanPhamBUS spBUS = new SanPhamBUS();

    public SuaSanPhamDialog(JFrame parent, String ma, String ten, String sl, String maLoai, String maHang, String mota,String baohanh) {
        super(parent, "Chỉnh sửa sản phẩm", true);
        setSize(600, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        initComponents(ma, ten, sl, maLoai, maHang, mota, baohanh);
        loadDataComboBox(maLoai,maHang);

        txtMaSP.setEditable(false);
        txtMaSP.setFocusable(false);
        txtSoLuong.setEditable(false);
        txtSoLuong.setFocusable(false);
    }

    private void initComponents(String ma, String ten, String sl, String maLoai, String maHang, String mota,String baohanh) {

        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlHeader.setBackground(Theme.PRIMARY_COLOR);
        JLabel lblHeader = new JLabel("CHỈNH SỬA SẢN PHẨM");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(Color.WHITE);
        pnlHeader.add(lblHeader);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlBody = new JPanel(new MigLayout("wrap 2, fillx, insets 20 40 20 40", "[130px!][grow]", "[]15[]"));
        pnlBody.setBackground(Color.WHITE);

        pnlBody.add(new JLabel("Mã sản phẩm:"));
        txtMaSP = new JTextField(ma);
        pnlBody.add(txtMaSP, "growx,h 35!");

        pnlBody.add(new JLabel("Tên sản phẩm:"));
        txtTenSP = new JTextField(ten);
        pnlBody.add(txtTenSP, "growx,h 35!");

        pnlBody.add(new JLabel("Số lượng:"));
        txtSoLuong = new JTextField(sl);
        pnlBody.add(txtSoLuong, "growx,h 35!");

        pnlBody.add(new JLabel("Mã loại:"));
        cboMaLoai = new JComboBox<>();
        cboMaLoai.setBackground(Color.WHITE);
        pnlBody.add(cboMaLoai, "growx,h 35!");

        pnlBody.add(new JLabel("Mã hãng:"));
        cboMaHang = new JComboBox<>();
        cboMaHang.setBackground(Color.WHITE);
        pnlBody.add(cboMaHang, "growx,h 35!");

        pnlBody.add(new JLabel("Bảo hành (tháng):"));
        txtBaoHanh = new JTextField(baohanh);
        pnlBody.add(txtBaoHanh, "growx,h 35!");

        pnlBody.add(new JLabel("Mô tả sản phẩm:"), "aligny top");

        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setText(mota);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        pnlBody.add(scrollMoTa, "growx, h 100!");

        add(pnlBody, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlFooter.setBackground(Color.WHITE);

        btnLuu = new CustomButton("Lưu thông tin", Theme.ACCENT_COLOR);
        btnLuu.setPreferredSize(new Dimension(140, 40));

        btnSuaPhienBan = new CustomButton("Sửa phiên bản", new Color(243, 156, 18));
        btnSuaPhienBan.setPreferredSize(new Dimension(140, 40));

        btnHuy = new CustomButton("Huỷ bỏ", Theme.DANGER_COLOR);
        btnHuy.setPreferredSize(new Dimension(140, 40));

        pnlFooter.add(btnLuu);
        pnlFooter.add(btnSuaPhienBan);
        pnlFooter.add(btnHuy);
        add(pnlFooter, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            try {
                SanPham sp = new SanPham();
                sp.setMaSP(txtMaSP.getText());
                sp.setTenSP(txtTenSP.getText());
                sp.setMaLoai(cboMaLoai.getSelectedItem().toString().split(" - ")[0].trim());
                sp.setMaHang(cboMaHang.getSelectedItem().toString().split(" - ")[0].trim());
                sp.setThoiGianBaoHanh(Integer.parseInt(txtBaoHanh.getText().trim()));
                sp.setMoTa(txtMoTa.getText());

                if (spBUS.update(sp)) {
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                    isSuccess = true;
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnSuaPhienBan.addActionListener(e -> {
            JFrame FrameCha=(JFrame) SwingUtilities.getWindowAncestor(this);
            PhienBanSPDialog dlgPB=new PhienBanSPDialog(FrameCha,txtMaSP.getText(),txtTenSP.getText(),true);
            dlgPB.setVisible(true);
        });
    }

    private void loadDataComboBox(String MaLoai, String MaHang){
        ArrayList<String> dsLoai = spBUS.getDanhSachLoai();
        for(String chuoiLoai : dsLoai){
            cboMaLoai.addItem(chuoiLoai);

        if (MaLoai != null && chuoiLoai.contains(MaLoai)) cboMaLoai.setSelectedItem(chuoiLoai);
        }

        ArrayList<String> dsHang = spBUS.getDanhSachHang();
        for(String chuoiHang : dsHang){
            cboMaHang.addItem(chuoiHang);
            if (MaHang != null && chuoiHang.contains(MaHang)) cboMaHang.setSelectedItem(chuoiHang);
        }
        
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
package Frontend.GUI.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Backend.DTO.KhuyenMai;
import Backend.BUS.KhuyenMaiBUS;
import Frontend.Compoent.BaseThaoTacDialog;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.util.Date;

public class KhuyenMaiDialog extends BaseThaoTacDialog {
    private JTextField txtMaKM, txtTenKM, txtGiamGia;
    private JSpinner spnNgayBD, spnNgayKT;
    private KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();
    private KhuyenMai resultData = null;

    public KhuyenMaiDialog(Frame owner, String title, KhuyenMai data) {
        super(title, 450, 480);
        
        if (data != null) {
            fillData(data);
            txtMaKM.setEditable(false);
            txtMaKM.setFocusable(false);
        } else {
            // Lấy mã tự động từ BUS
            String newMa = kmBUS.getNewMaKM(); 
            txtMaKM.setText(newMa);
            txtMaKM.setEditable(false);
            txtMaKM.setFocusable(false);
            
            // Focus vào ô tên chương trình
            SwingUtilities.invokeLater(() -> txtTenKM.requestFocusInWindow());
        }
    }

    @Override
    protected void initForm() {
        pnlContent.setLayout(new MigLayout("fillx, insets 25 40 20 40", "[right, 130!]15[grow, fill]"));
        pnlContent.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        String fieldStyle = "h 36!, wrap 20"; 

        pnlContent.add(new JLabel("Mã khuyến mãi:") {{ setFont(labelFont); }});
        txtMaKM = new JTextField();
        setupRoundedField(txtMaKM);
        pnlContent.add(txtMaKM, fieldStyle);

        pnlContent.add(new JLabel("Tên chương trình:") {{ setFont(labelFont); }});
        txtTenKM = new JTextField();
        setupRoundedField(txtTenKM);
        pnlContent.add(txtTenKM, fieldStyle);

        pnlContent.add(new JLabel("% Giảm giá:") {{ setFont(labelFont); }});
        txtGiamGia = new JTextField();
        setupRoundedField(txtGiamGia);
        pnlContent.add(txtGiamGia, fieldStyle);

        pnlContent.add(new JLabel("Ngày bắt đầu:") {{ setFont(labelFont); }});
        spnNgayBD = new JSpinner(new SpinnerDateModel());
        setupRoundedSpinner(spnNgayBD);
        pnlContent.add(spnNgayBD, fieldStyle);

        pnlContent.add(new JLabel("Ngày kết thúc:") {{ setFont(labelFont); }});
        spnNgayKT = new JSpinner(new SpinnerDateModel());
        setupRoundedSpinner(spnNgayKT);
        pnlContent.add(spnNgayKT, fieldStyle);
    }

    private void setupRoundedField(JTextField tf) {
        // Sử dụng arc = 8 để bo góc nhẹ (không phải hình viên thuốc)
        tf.putClientProperty("JComponent.outlineRoundArc", 8); 
        tf.setMargin(new Insets(0, 10, 0, 10)); 
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setDisabledTextColor(new Color(120, 120, 120));
    }

    private void setupRoundedSpinner(JSpinner sp) {
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy"));
        sp.putClientProperty("JComponent.outlineRoundArc", 8);
        sp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField editorTf = ((JSpinner.DefaultEditor) editor).getTextField();
            editorTf.setBorder(new EmptyBorder(0, 5, 0, 5));
            editorTf.setOpaque(false);
        }
    }

    private void fillData(KhuyenMai data) {
        txtMaKM.setText(data.getMaKM());
        txtTenKM.setText(data.getTenKM());
        txtGiamGia.setText(String.valueOf(data.getPhanTramGiam()));
        spnNgayBD.setValue(data.getNgayBD());
        spnNgayKT.setValue(data.getNgayKT());
    }

    @Override
    protected void logicXacNhan() {
        try {
            String maKM = txtMaKM.getText();
            String tenKM = txtTenKM.getText();
            double phanTram = Double.parseDouble(txtGiamGia.getText());
            Date ngayBD = (Date) spnNgayBD.getValue();
            Date ngayKT = (Date) spnNgayKT.getValue();

            resultData = new KhuyenMai(maKM, tenKM, phanTram, ngayBD, ngayKT, 1);
            
            // Kiểm tra nghiệp vụ qua BUS
            String msg = kmBUS.validate(resultData, !txtMaKM.isFocusable());
            if (!msg.equals("OK")) {
                JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho phần giảm giá!");
        }
    }

    public KhuyenMai getData() { return resultData; }
}

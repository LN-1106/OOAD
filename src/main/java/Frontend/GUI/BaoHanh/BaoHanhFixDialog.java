package Frontend.GUI.BaoHanh;

import javax.swing.*;
import Backend.BUS.BaoHanhBUS;
import Backend.DTO.BaoHanh;
import Frontend.Compoent.BaseThaoTacDialog;
import net.miginfocom.swing.MigLayout;
import java.time.LocalDate;

public class BaoHanhFixDialog extends BaseThaoTacDialog {
    private JTextField txtMaBH, txtImei, txtMaPX;
    private JSpinner spnNgayBD, spnNgayKT;
    private BaoHanhBUS bhBUS = new BaoHanhBUS();

    public BaoHanhFixDialog(String ma, String imei, String maPX) {
        super("SỬA PHIẾU BẢO HÀNH", 450, 500);

        txtMaBH.setText(ma);
        txtImei.setText(imei);
        txtMaPX.setText(maPX);

        txtMaBH.setEditable(false);
        txtMaBH.setFocusable(false);

        SwingUtilities.invokeLater(() -> {
            txtImei.requestFocusInWindow();
        });
    }

    @Override
    protected void initForm() {
        pnlContent.removeAll();
        pnlContent.setLayout(new MigLayout("wrap 2, fillx, insets 30", "[100!]20[grow]", "[]20[]20[]20[]20[]"));

        pnlContent.add(new JLabel("Mã bảo hành:"));
        txtMaBH = new JTextField();
        pnlContent.add(txtMaBH, "growx, h 35!");

        pnlContent.add(new JLabel("Mã Imei:"));
        txtImei = new JTextField();
        pnlContent.add(txtImei, "growx, h 35!");

        pnlContent.add(new JLabel("Mã phiếu xuất:"));
        txtMaPX = new JTextField();
        pnlContent.add(txtMaPX, "growx, h 35!");

        pnlContent.add(new JLabel("Ngày bắt đầu:"));
        spnNgayBD = new JSpinner(new SpinnerDateModel());
        spnNgayBD.setEditor(new JSpinner.DateEditor(spnNgayBD, "dd/MM/yyyy"));
        pnlContent.add(spnNgayBD, "growx, h 35!");

        pnlContent.add(new JLabel("Ngày kết thúc:"));
        spnNgayKT = new JSpinner(new SpinnerDateModel());
        spnNgayKT.setEditor(new JSpinner.DateEditor(spnNgayKT, "dd/MM/yyyy"));
        pnlContent.add(spnNgayKT, "growx, h 35!");
    }

    @Override
    protected void logicXacNhan() {
        LocalDate ngayBD = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd").format(spnNgayBD.getValue()));
        LocalDate ngayKT = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd").format(spnNgayKT.getValue()));

        BaoHanh bh = new BaoHanh(txtMaBH.getText(), txtImei.getText(), txtMaPX.getText(), ngayBD, ngayKT);

        if (bhBUS.update(bh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng kiểm tra Mã Imei hoặc Phiếu xuất.");
        }
    }
}
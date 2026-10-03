package Frontend.GUI.DoiTra;

import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import Backend.DTO.DoiTra;
import Frontend.Compoent.BaseThaoTacDialog;

public class DoiTraDetailDialog extends BaseThaoTacDialog {

    private DoiTra doiTra;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtMaDT, txtNgayDT, txtLyDo, txtMaPX, txtTenKH, txtImei, txtStatus;

    public DoiTraDetailDialog(DoiTra doiTra) {
        super("CHI TIẾT PHIẾU ĐỔI TRẢ", 450, 600);
        this.doiTra = doiTra;
        
        fillData();
        
        btnXacNhan.setVisible(false);
        btnHuy.setText("Đóng");
    }

    @Override
    protected void initForm() {
        pnlContent.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 20", "[right]15[grow, fill]"));

        addHeaderLabel("THÔNG TIN PHIẾU");
        
        pnlContent.add(new JLabel("Mã phiếu đổi trả:"));
        txtMaDT = createReadOnlyField("");
        pnlContent.add(txtMaDT, "h 35!, wrap");

        pnlContent.add(new JLabel("Ngày thực hiện:"));
        txtNgayDT = createReadOnlyField("");
        pnlContent.add(txtNgayDT, "h 35!, wrap");

        pnlContent.add(new JLabel("Lý do trả hàng:"));
        txtLyDo = createReadOnlyField("");
        pnlContent.add(txtLyDo, "h 35!, wrap");

        pnlContent.add(new JSeparator(), "span, growx, gaptop 10, gapbottom 10, wrap");

        addHeaderLabel("NGUỒN GỐC GIAO DỊCH");

        pnlContent.add(new JLabel("Mã phiếu xuất:"));
        txtMaPX = createReadOnlyField("");
        pnlContent.add(txtMaPX, "h 35!, wrap");

        pnlContent.add(new JLabel("Khách hàng:"));
        txtTenKH = createReadOnlyField("");
        pnlContent.add(txtTenKH, "h 35!, wrap");

        pnlContent.add(new JSeparator(), "span, growx, gaptop 10, gapbottom 10, wrap");

        addHeaderLabel("SẢN PHẨM ĐÃ THU HỒI");

        pnlContent.add(new JLabel("Mã IMEI:"));
        txtImei = createReadOnlyField(""); 
        txtImei.setForeground(new Color(231, 76, 60)); 
        txtImei.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlContent.add(txtImei, "h 35!, wrap");

        pnlContent.add(new JLabel("Trạng thái:"));
        txtStatus = createReadOnlyField("Đã nhập lại kho");
        txtStatus.setForeground(new Color(46, 204, 113)); 
        pnlContent.add(txtStatus, "h 35!, wrap");
    }

    private void addHeaderLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(52, 73, 94));
        pnlContent.add(lbl, "span, growx, gaptop 5, gapbottom 5, wrap");
    }

    private JTextField createReadOnlyField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setFocusable(false);
        field.setBackground(new Color(245, 245, 245));
        return field;
    }

    private void fillData() {
        if (doiTra != null) {
            txtMaDT.setText(doiTra.getMaDoiTra());
            txtNgayDT.setText(doiTra.getNgayDoiTra().format(dtf));
            txtLyDo.setText(doiTra.getLyDo());
            txtMaPX.setText(doiTra.getMaPhieuXuat());
            txtTenKH.setText(doiTra.getTenKH());
            txtImei.setText(doiTra.getMaImei());
        }
    }

    @Override
    protected void logicXacNhan() {
        dispose();
    }
}
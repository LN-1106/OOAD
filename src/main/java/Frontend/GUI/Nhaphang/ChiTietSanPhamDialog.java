package Frontend.GUI.Nhaphang;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import Frontend.Compoent.InfoField;

public class ChiTietSanPhamDialog extends JDialog{
    public ChiTietSanPhamDialog(JFrame parent, String ma, String ten, String gia) {
        setTitle("Chi tiết sản phẩm");
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setModal(true); 

        JPanel pnlMain = new JPanel(new MigLayout("fill, insets 20", "[grow]15[300!]", "[grow]"));
        pnlMain.setBackground(Color.WHITE);

        // --- PHẦN THÔNG TIN BÊN TRÁI ---
        JPanel info = new JPanel(new MigLayout("wrap 1, fillx, insets 20", "[fill]"));
        info.setBackground(new Color(248, 249, 250));

        info.add(new JLabel("Mã sản phẩm:"), "gaptop 10");
        info.add(new InfoField(ma), "h 40!");

        info.add(new JLabel("Tên sản phẩm:"), "gaptop 10");
        info.add(new InfoField(ten), "h 40!");

        info.add(new JLabel("Đơn giá nhập:"), "gaptop 10");
        info.add(new InfoField(gia), "h 40!");

        // --- PHẦN PREVIEW BÊN PHẢI ---
        JPanel pnlPreview = new JPanel(new MigLayout("wrap 1, fillx, insets 15", "[center]", "[]push[]5[]"));
        pnlPreview.setBackground(new Color(248, 249, 250));
        pnlPreview.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel lblImg = new JLabel();
        lblImg.putClientProperty("FlatLaf.style", "arc: 15"); 
        lblImg.setOpaque(true);
        lblImg.setBackground(Color.WHITE);
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel lblImei = new JLabel("Mã IMEI/Serial bảo hành:");
        lblImei.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        InfoField txtImei = new InfoField("IMEI-123456789");
        txtImei.setEditable(false);
        txtImei.setHorizontalAlignment(JTextField.CENTER);

        pnlPreview.add(lblImg, "w 250!, h 250!");
        pnlPreview.add(lblImei, "left, gaptop 20");
        pnlPreview.add(txtImei, "growx, h 40!, gaptop 5");

        pnlMain.add(info, "grow");
        pnlMain.add(pnlPreview, "growy");
        add(pnlMain, BorderLayout.CENTER);
    }
}

package Frontend.GUI.ThongKe;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import Backend.BUS.ThongKeBUS;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class KpiCardsPanel extends JPanel {

    private JLabel lblValDoanhThu, lblValDonHang, lblValVon, lblValLoiNhuan;
    private final ThongKeBUS tkBUS = new ThongKeBUS();
    private final DecimalFormat df = new DecimalFormat("#,###");

    public KpiCardsPanel() {
        setLayout(new MigLayout("fill, insets 0", "[grow]15[grow]15[grow]15[grow]", "fill"));
        setOpaque(false);
        initComponents();
        loadRealData();
    }

    private void initComponents() {
        lblValDoanhThu = new JLabel("0");
        lblValDonHang = new JLabel("0");
        lblValVon = new JLabel("0");
        lblValLoiNhuan = new JLabel("0");

        add(createKpiCard("Doanh Thu Ngày", "+Hôm nay", lblValDoanhThu, "landmark.svg", new Color(46, 204, 113)));
        add(createKpiCard("Đơn Hàng Ngày", "Mới", lblValDonHang, "handbag.svg", new Color(52, 152, 219)));
        add(createKpiCard("Vốn Nhập Tháng", "Chi", lblValVon, "banknote-arrow-up.svg", new Color(231, 76, 60)));
        add(createKpiCard("Lợi Nhuận Tháng", "Thuần", lblValLoiNhuan, "banknote-arrow-down.svg", new Color(155, 89, 182)));
    }

    private void loadRealData() {
        lblValDoanhThu.setText(df.format(tkBUS.getDoanhThuNgay()));
        lblValDonHang.setText(String.valueOf(tkBUS.getDonHangMoiNgay()));
        lblValVon.setText(df.format(tkBUS.getVonNhapThang()));
        lblValLoiNhuan.setText(df.format(tkBUS.getLoiNhuanThang()));
    }

    private JPanel createKpiCard(String title, String trend, JLabel lblValue, String iconName, Color color) {
        JPanel card = new JPanel(new MigLayout("insets 15", "[][grow]", "[]2[]"));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc: 20");

        FlatSVGIcon icon = new FlatSVGIcon("images/icon/" + iconName, 30, 30);
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(149, 165, 166));

        JLabel lblTrend = new JLabel(trend);
        lblTrend.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTrend.setForeground(color);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));

        card.add(new JLabel(icon), "spany 2, gapright 10");
        card.add(lblTitle, "split 2");
        card.add(lblTrend, "wrap");
        card.add(lblValue);

        return card;
    }
}
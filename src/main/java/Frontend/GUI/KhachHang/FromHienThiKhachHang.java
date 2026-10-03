package Frontend.GUI.KhachHang;

import javax.swing.JPanel;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class FromHienThiKhachHang extends JPanel {
    private KhachHangToolbar toolbar;
    private KhachHangTable table;

    public FromHienThiKhachHang() {
        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new KhachHangTable();
        toolbar = new KhachHangToolbar(table);

        add(toolbar, "growx");
        add(table, "grow");
    }
}

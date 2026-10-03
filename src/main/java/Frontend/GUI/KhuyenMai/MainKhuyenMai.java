package Frontend.GUI.KhuyenMai;

import javax.swing.*;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class MainKhuyenMai extends JPanel {
    private KhuyenMaiToolbar toolbar;
    private KhuyenMaiTable table;

    public MainKhuyenMai() {
        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new KhuyenMaiTable();
        toolbar = new KhuyenMaiToolbar(table);

        add(toolbar, "growx");
        add(table, "grow");
    }
}

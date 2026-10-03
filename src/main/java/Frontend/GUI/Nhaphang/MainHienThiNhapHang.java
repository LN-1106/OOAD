package Frontend.GUI.Nhaphang;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class MainHienThiNhapHang extends JPanel {
    private NhapHangToolbar toolbar;
    private NhapHangSidebar sidebar;
    private NhapHangTable table;

    public MainHienThiNhapHang() {
        setLayout(new MigLayout("fill, insets 15", "[280!]15[grow, fill]", "[][grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new NhapHangTable(null);
        sidebar = new NhapHangSidebar(table);
        table.setSidebar(sidebar);
        toolbar = new NhapHangToolbar(table, sidebar);

        add(toolbar, "span 2, growx, wrap, gapbottom 10");
        add(sidebar, "w 280!, growy, cell 0 1");
        add(table, "grow, cell 1 1");
    }
}
package Frontend.GUI.PhieuXuat;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class PhieuXuatPanel extends JPanel {
    private PhieuXuatToolbar toolbar;
    private PhieuXuatSidebar sidebar;
    private PhieuXuatTable table;

    public PhieuXuatPanel() {
        setLayout(new MigLayout("fill, insets 15", "[280!]15[grow, fill]", "[][grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new PhieuXuatTable();
        toolbar = new PhieuXuatToolbar(table);
        sidebar = new PhieuXuatSidebar(table);

        add(toolbar, "span 2, growx, wrap, gapbottom 10");
        add(sidebar, "w 280!, growy, cell 0 1");
        add(table, "grow, cell 1 1");
    }
}

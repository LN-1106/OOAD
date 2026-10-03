package Frontend.GUI.PhieuNhap;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class FromHienThiPhieuNhap extends JPanel{
    private PhieuNhapToolbar toolbar;
    private PhieuNhapSidebar sidebar;
    private PhieuNhapTable table;
    
    public FromHienThiPhieuNhap() {
        setLayout(new MigLayout("fill, insets 15", "[280!]15[grow, fill]", "[][grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new PhieuNhapTable();
        toolbar = new PhieuNhapToolbar(table);
        sidebar = new PhieuNhapSidebar(table);

        add(toolbar, "span 2, growx, wrap, gapbottom 10");
        add(sidebar, "w 280!, growy, cell 0 1");
        add(table, "grow, cell 1 1");
    }
}

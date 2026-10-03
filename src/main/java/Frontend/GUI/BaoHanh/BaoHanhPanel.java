package Frontend.GUI.BaoHanh;

import javax.swing.*;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class BaoHanhPanel extends JPanel {
    private BaoHanhToolbar toolbar;
    private BaoHanhTable table;

    public BaoHanhPanel() {
        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new BaoHanhTable();
        toolbar = new BaoHanhToolbar(table);

        add(toolbar, "growx");
        add(table, "grow");
    }
}

package Frontend.GUI.DoiTra;

import javax.swing.JPanel;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class MainHienThiDoiTra extends JPanel {

    private DoiTraToolbar toolbar;
    private DoiTraTable table;

    public MainHienThiDoiTra() {

        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new DoiTraTable();
        toolbar = new DoiTraToolbar(table);

        add(toolbar, "growx");
        add(table, "grow");
    }
}
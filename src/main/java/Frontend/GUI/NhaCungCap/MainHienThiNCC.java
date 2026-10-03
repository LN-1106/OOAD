package Frontend.GUI.NhaCungCap;

import javax.swing.JPanel;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class MainHienThiNCC extends JPanel{
    private NCCToolbar toolbar;
    private NCCTable table;

    public MainHienThiNCC() {
        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table =  new NCCTable();
        toolbar = new NCCToolbar(table);
    
        add(toolbar, "growx");
        add(table, "grow");
    }
}

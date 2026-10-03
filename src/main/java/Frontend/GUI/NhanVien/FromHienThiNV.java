package Frontend.GUI.NhanVien;

import javax.swing.JPanel;

import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class FromHienThiNV extends JPanel {
    private NhanVienToolbar toolbar;
    private NhanVienTable table;

    public FromHienThiNV() {
        setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        table = new NhanVienTable();
        toolbar = new NhanVienToolbar(table);

        add(toolbar, "growx");
        add(table, "grow");
    }
}

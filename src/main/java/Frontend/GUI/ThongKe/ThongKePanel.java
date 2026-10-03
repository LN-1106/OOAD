package Frontend.GUI.ThongKe;

import javax.swing.*;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class ThongKePanel extends JPanel {

    public ThongKePanel() {
        setLayout(new MigLayout("fill, insets 20", "[grow, fill]", "[]20[350!]20[grow]"));
        setBackground(Theme.BACKGROUND_COLOR);

        add(new KpiCardsPanel(), "growx,wrap");
        add(new ProductChartPanel(), "growx, wrap");
        add(new TrendChartPanel(), "grow");
    }
}
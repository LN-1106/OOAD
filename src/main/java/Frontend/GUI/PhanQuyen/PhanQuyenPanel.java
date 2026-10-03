package Frontend.GUI.PhanQuyen;

import javax.swing.*;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class PhanQuyenPanel extends JPanel {
      private PhanQuyenToolBar toolbar;
      private PhanQuyenTable table;

      public PhanQuyenPanel() {
            setLayout(new MigLayout("wrap 1, fill, insets 15", "[grow, fill]", "[]20[grow]"));
            setBackground(Theme.BACKGROUND_COLOR);

            table = new PhanQuyenTable();
            toolbar = new PhanQuyenToolBar(table);

            add(toolbar, "growx");
            add(table, "grow");
      }
}
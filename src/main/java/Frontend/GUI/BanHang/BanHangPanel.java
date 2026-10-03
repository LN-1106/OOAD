package Frontend.GUI.BanHang;

import javax.swing.*;
import java.awt.*;
import Frontend.Compoent.Theme;
import Frontend.GUI.BaoHanh.BaoHanhTable;
import Frontend.GUI.PhieuXuat.PhieuXuatTable;
import net.miginfocom.swing.MigLayout;

public class BanHangPanel extends JPanel {
    private ProductGrid productGrid;
    private BanHangSidebar sidebar;
    private BanHangToolbar toolbar;
    private PaginationPanel pagination;
    private PhieuXuatTable pxTable;
    private BaoHanhTable bhTable;

    public BanHangPanel() {
        setLayout(new MigLayout("fill, insets 15", "[280!]15[grow, fill]", "[][grow][]"));
        setBackground(Theme.BACKGROUND_COLOR);

        this.pxTable = new PhieuXuatTable();
        this.bhTable = new BaoHanhTable();

        this.sidebar = new BanHangSidebar(pxTable, bhTable);

        productGrid = new ProductGrid(sidebar, pagination);
        toolbar = new BanHangToolbar(productGrid, sidebar);

        add(toolbar, "span 2, growx, wrap, gapbottom 10");
        add(sidebar, "w 280!, growy, span 1 2");
        add(productGrid, "grow, wrap");

        productGrid.loadData();
    }
}

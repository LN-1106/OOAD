package Frontend.GUI.BanHang;

import javax.swing.*;

import Backend.BUS.LoaiSPBUS;
import Backend.DTO.ThuocTinhSanPham.LoaiSP;
import net.miginfocom.swing.MigLayout;
import Frontend.Compoent.Theme;
import Frontend.Compoent.SearchTextField;
import java.awt.*;
import java.util.ArrayList;

public class BanHangToolbar extends JPanel {
    private ProductGrid productGrid;
    private BanHangSidebar sidebar;
    private JComboBox<LoaiSP> cbFilter;

    public BanHangToolbar(ProductGrid productGrid, BanHangSidebar sidebar) {
        this.productGrid = productGrid;
        this.sidebar = sidebar;

        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm sản phẩm...");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().trim();
                productGrid.loadDataSearch(query);
            }
        });
        add(txtSearch, "growx, h 35!");

        cbFilter = new JComboBox<>();
        DefaultComboBoxModel<LoaiSP> model = new DefaultComboBoxModel<>();
        model.addElement(new LoaiSP("All", "Tất cả sản phẩm"));

        LoaiSPBUS loaiBUS = new LoaiSPBUS();
        ArrayList<LoaiSP> listLoai = loaiBUS.getAll();
        for (LoaiSP l : listLoai) {
            model.addElement(l);
        }
        cbFilter.setModel(model);

        cbFilter.addActionListener(e -> {
            LoaiSP selected = (LoaiSP) cbFilter.getSelectedItem();
            if (selected != null) {
                productGrid.loadDataByLoai(selected.getMaLoai());
            }
        });

        cbFilter.setPreferredSize(new Dimension(200, 35));
        cbFilter.putClientProperty("FlatLaf.style", "arc: 10");
        add(cbFilter, "w 200!");
    }
}

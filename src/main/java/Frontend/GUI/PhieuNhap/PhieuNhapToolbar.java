package Frontend.GUI.PhieuNhap;

import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.ButtonXuatPdf;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class PhieuNhapToolbar extends JPanel{
    private PhieuNhapTable table;

    public PhieuNhapToolbar(PhieuNhapTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC );

        SearchTextField txtSearch = new SearchTextField("Tìm phiếu nhập hàng ...");
        ButtonXuatExcel btnExcel = new ButtonXuatExcel("Xuất EXCEL");
        

        add(txtSearch, "growx, h 35!");
        add(btnExcel, "w 105!, h 35!");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().trim();
                table.loadDataBySearch(query);
            }
        });
        
        btnExcel.addActionListener(e -> {
            Frontend.Compoent.XuatExcel.xuat(table.getTable());
        });
    }
}

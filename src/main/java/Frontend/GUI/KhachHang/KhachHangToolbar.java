package Frontend.GUI.KhachHang;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Backend.BUS.KhachHangBUS;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class KhachHangToolbar extends JPanel {
    private KhachHangTable table;
    private KhachHangBUS khBUS = new KhachHangBUS();

    public KhachHangToolbar(KhachHangTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm khách hàng ...");
        ButtonAdd btnAdd = new ButtonAdd("Thêm");
        ButtonFix btnFix = new ButtonFix("Sửa");
        ButtonDele btnDele = new ButtonDele("Xóa");
        ButtonXuatExcel btnXuatExcel = new ButtonXuatExcel("Xuất Excel");

        add(txtSearch, "growx, h 35!");
        add(btnAdd, "w 95!, h 35!");
        add(btnFix, "w 95!, h 35!");
        add(btnDele, "w 95!, h 35!");
        add(btnXuatExcel, "w 105!, h 35!");

        btnXuatExcel.addActionListener(e -> {
            Frontend.Compoent.XuatExcel.xuat(table.getTbl());
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().toLowerCase().trim();
                // table.loadDataBySearch(query);
            }
        });

        btnAdd.addActionListener(e -> {
            KhachHangAddDialog dialog = new KhachHangAddDialog();
            dialog.setVisible(true);
            table.loadData();
            // table.loadComboBox();
        });

        btnFix.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int row = table.getTbl().convertRowIndexToModel(selectedRow);

            String ma = table.getTbl().getValueAt(row, 1).toString();
            String ten = table.getTbl().getValueAt(row, 2).toString();
            String sdt = table.getTbl().getValueAt(row, 3).toString();
            String diaChi = table.getTbl().getValueAt(row, 4).toString();

            KhachHangFixDialog dialog = new KhachHangFixDialog(ma, ten, sdt, diaChi);
            dialog.setVisible(true);
            table.loadData();
            // table.loadComboBox();
        });

        btnDele.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa!");
                return;
            }

            String ma = table.getTbl().getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this,
                    "Xóa khách hàng: " + ma + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                khBUS.delete(ma);
                table.loadData();
                // table.loadComboBox();
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                table.filterByKeyword(txtSearch.getText());
            }
        });
    }
}

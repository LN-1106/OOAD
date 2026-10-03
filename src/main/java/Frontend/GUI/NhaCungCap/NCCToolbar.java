package Frontend.GUI.NhaCungCap;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Backend.BUS.NhaCungCapBUS;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class NCCToolbar extends JPanel{
    private NCCTable table;
    private NhaCungCapBUS nccBUS = new NhaCungCapBUS();

    public NCCToolbar(NCCTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm nhà cung cấp ...");
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
                table.loadDataBySearch(query);
            }
        });

        btnAdd.addActionListener(e -> {
            NCCAddDialog dialog = new NCCAddDialog();
            dialog.setVisible(true);
            table.loadData();
            table.loadComboBox();
        });

        btnFix.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ma = table.getTbl().getValueAt(selectedRow, 1).toString();
            String ten = table.getTbl().getValueAt(selectedRow, 2).toString();
            String diaChi = table.getTbl().getValueAt(selectedRow, 3).toString();
            String sdt = table.getTbl().getValueAt(selectedRow, 4).toString();

            NCCFixDialog dialog = new NCCFixDialog(ma, ten, diaChi, sdt);
            dialog.setVisible(true);
            table.loadData();
            table.loadComboBox();

        });

        btnDele.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa!");
                return;
            }

            String ma = table.getTbl().getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this, "Xóa mã: " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if(nccBUS.delete(ma)) {
                    table.loadData();
                    table.loadComboBox();
                    JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                }
            }
        });
    }
}

package Frontend.GUI.BaoHanh;

import java.awt.Color;
import java.awt.Font;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import Backend.BUS.BaoHanhBUS;

public class BaoHanhToolbar extends JPanel {
    private BaoHanhTable table;
    private BaoHanhBUS baoHanhBUS = new BaoHanhBUS();

    public BaoHanhToolbar(BaoHanhTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm thiết bị bảo hành (IMEI, Mã BH)...");
        ButtonAdd btnAdd = new ButtonAdd("Thêm");
        // ButtonFix btnFix = new ButtonFix("Sửa");
        ButtonDele btnDele = new ButtonDele("Xóa");
        ButtonXuatExcel btnXuatExcel = new ButtonXuatExcel("Xuất Excel");

        add(txtSearch, "growx, h 35!");
        add(btnAdd, "w 95!, h 35!");
        // add(btnFix, "w 95!, h 35!");
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
            BaoHanhAddDialog dialog = new BaoHanhAddDialog();
            dialog.setVisible(true);
            table.loadData();
        });

        // btnFix.addActionListener(e -> {
        //     int selectedRow = table.getTbl().getSelectedRow();
        //     if (selectedRow == -1) {
        //         JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phiếu bảo hành để sửa!", "Thông báo",
        //                 JOptionPane.WARNING_MESSAGE);
        //         return;
        //     }

        //     String maBH = table.getTbl().getValueAt(selectedRow, 1).toString();
        //     String maImei = table.getTbl().getValueAt(selectedRow, 2).toString();
        //     String maPX = table.getTbl().getValueAt(selectedRow, 3).toString();

        //     BaoHanhFixDialog dialog = new BaoHanhFixDialog(maBH, maImei, maPX);
        //     dialog.setVisible(true);
        //     table.loadData();
        // });

        btnDele.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa!");
                return;
            }

            String maBH = table.getTbl().getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa phiếu bảo hành: " + maBH + "?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                if (baoHanhBUS.delete(maBH)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa thành công phiếu: " + maBH);
                    table.loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
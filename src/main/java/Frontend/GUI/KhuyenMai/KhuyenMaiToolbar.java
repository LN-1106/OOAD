package Frontend.GUI.KhuyenMai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Frontend.Compoent.*;
import Backend.BUS.KhuyenMaiBUS;
import Backend.DTO.KhuyenMai;
import net.miginfocom.swing.MigLayout;

public class KhuyenMaiToolbar extends JPanel {
    private KhuyenMaiTable table;
    private KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();

    public KhuyenMaiToolbar(KhuyenMaiTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm theo mã hoặc tên...");
        ButtonAdd btnAdd = new ButtonAdd("Thêm");
        ButtonFix btnFix = new ButtonFix("Sửa");
        ButtonDele btnDele = new ButtonDele("Xóa");
        ButtonXuatExcel btnExcel = new ButtonXuatExcel("Xuất Excel");

        add(txtSearch, "growx, h 35!");
        add(btnAdd, "w 95!, h 35!");
        add(btnFix, "w 95!, h 35!");
        add(btnDele, "w 95!, h 35!");
        add(btnExcel, "w 110!, h 35!");

        // --- Action Tìm kiếm ---
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = txtSearch.getText();
        // Gọi hàm này để vừa lọc theo từ khóa, vừa giữ bộ lọc ComboBox
                table.loadDataBySearch(query);
            }
        });

        // --- Action Thêm ---
        btnAdd.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this);
            KhuyenMaiDialog diag = new KhuyenMaiDialog((Frame) parent, "Thêm mới khuyến mãi", null);
            diag.setVisible(true);
            
            KhuyenMai resultKM = diag.getData();
            if (resultKM != null) {
                JOptionPane.showMessageDialog(this, kmBUS.addKhuyenMai(resultKM));
                table.loadData();
            }
        });

        // --- Action Sửa ---
        btnFix.addActionListener(e -> {
            int row = table.getTbl().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để chỉnh sửa!");
                return;
            }
            
            String ma = table.getTbl().getValueAt(row, 1).toString();
            KhuyenMai selected = null;
            for (KhuyenMai k : kmBUS.getAllKhuyenMai()) {
                if (k.getMaKM().equals(ma)) {
                    selected = k;
                    break;
                }
            }

            if (selected != null) {
                Window parent = SwingUtilities.getWindowAncestor(this);
                KhuyenMaiDialog diag = new KhuyenMaiDialog((Frame) parent, "Chỉnh sửa khuyến mãi", selected);
                diag.setVisible(true);
                
                KhuyenMai updatedKM = diag.getData();
                if (updatedKM != null) {
                    JOptionPane.showMessageDialog(this, kmBUS.updateKhuyenMai(updatedKM));
                    table.loadData();
                }
            }
        });

        // --- Action Xóa (Soft Delete) ---
        btnDele.addActionListener(e -> {
            int row = table.getTbl().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần ngừng hoạt động!");
                return;
            }
            
            String ma = table.getTbl().getValueAt(row, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn NGỪNG HOẠT ĐỘNG mã: " + ma + "?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
                
            if (opt == JOptionPane.YES_OPTION) {
                if (kmBUS.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái ngừng hoạt động!");
                    table.loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thực hiện!");
                }
            }
        });

        // --- Action Xuất Excel ---
        btnExcel.addActionListener(e -> {
            // Sẽ triển khai khi bạn có class ExcelUtil
            Frontend.Compoent.XuatExcel.xuat(table.getTbl());
        });
    }
}

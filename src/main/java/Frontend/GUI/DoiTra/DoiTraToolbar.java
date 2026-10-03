package Frontend.GUI.DoiTra;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import Backend.BUS.DoiTraBUS;
import Backend.DTO.DoiTra;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class DoiTraToolbar extends JPanel {

    private DoiTraTable table;
    private DoiTraBUS doiTraBUS = new DoiTraBUS();

    private ButtonAdd btnAdd;
    private ButtonFix btnFix;
    private ButtonDele btnDele;
    private ButtonXuatExcel btnXuatExcel;
    private SearchTextField txtSearch;

    public DoiTraToolbar(DoiTraTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        txtSearch = new SearchTextField("Tìm kiếm mã DT, mã PX, IMEI, khách hàng...");
        btnAdd = new ButtonAdd("Thêm");
        btnFix = new ButtonFix("Sửa");
        btnDele = new ButtonDele("Xóa");
        btnXuatExcel = new ButtonXuatExcel("Xuất Excel");

        add(txtSearch, "growx, h 35!");
        add(btnAdd, "w 95!, h 35!");
        add(btnFix, "w 95!, h 35!");
        add(btnDele, "w 95!, h 35!");
        add(btnXuatExcel, "w 105!, h 35!");
        
        initEvent();
    }

    private void initEvent() {
        btnAdd.addActionListener(e -> {
            DoiTraAddDialog dialog = new DoiTraAddDialog();
            dialog.setVisible(true);
            table.loadData();
        });

        btnFix.addActionListener(e -> {
            JTable tbl = table.getTable();
            int row = tbl.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu đổi trả cần sửa!");
                return;
            }

            String maDT = tbl.getValueAt(row, 0).toString();
            
            DoiTra selectedDTO = null;
            for (DoiTra item : doiTraBUS.getAll()) {
                if (item.getMaDoiTra().equals(maDT)) {
                    selectedDTO = item;
                    break;
                }
            }

            if (selectedDTO != null) {
                DoiTraFixDialog dialog = new DoiTraFixDialog(selectedDTO);
                dialog.setVisible(true);
                table.loadData();
            }
        });

        btnDele.addActionListener(e -> {
            JTable tbl = table.getTable();
            int row = tbl.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xóa!");
                return;
            }

            String ma = tbl.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa phiếu " + ma + "?\n(Lưu ý: Thao tác này sẽ ẩn phiếu khỏi danh sách hiển thị)",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (doiTraBUS.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                    table.loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa phiếu này.");
                }
            }
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String keyword = txtSearch.getText().trim();
                ArrayList<DoiTra> result = doiTraBUS.search(keyword);
                table.updateTable(result); 
            }
        });
        
        btnXuatExcel.addActionListener(e -> {
            Frontend.Compoent.XuatExcel.xuat(table.getTable());
        });
    }
}
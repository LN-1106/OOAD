package Frontend.GUI.SanPham;

import Backend.BUS.SanPhamBUS;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.ButtonRefresh;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.XuatExcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import net.miginfocom.swing.MigLayout;

public class SanPhamToolbar extends JPanel {
    SanPhamBUS spBUS=new SanPhamBUS();
    private SanPhamPanel parentPanel;
    private SearchTextField txtSearch;
    public SanPhamToolbar(SanPhamPanel parentPanel) {
        this.parentPanel = parentPanel;
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new MigLayout("fillx, insets 15", "[grow]10[]10[]10[]10[]", "[]"));

        txtSearch = new SearchTextField("Tìm kiếm sản phẩm...");
        txtSearch.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent evt) {
            String keyword = txtSearch.getText().trim();
            parentPanel.getTable().search(keyword);
            // JTable tbl = parentPanel.getTable().getTable();
            
            // if (tbl.getRowSorter() != null) {
            //     @SuppressWarnings("unchecked")
            //     TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbl.getRowSorter();
            //     if (keyword.isEmpty()) {
            //         sorter.setRowFilter(null);
            //         return;
            //     }
            //     try {
            //     String[] andWords = keyword.split("\\s+");
            //     java.util.List<RowFilter<Object,Object>> andFilters = new java.util.ArrayList<>();
                
            //     for (String word : andWords) {
            //         andFilters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(word), 1, 2, 4, 5,7)); 
            //     }
                
            //     sorter.setRowFilter(RowFilter.andFilter(andFilters));
                    
            //     } catch (Exception ex) {
            //     }
            // }
        }
        });

        ButtonAdd btnAdd = new ButtonAdd("Thêm");
        ButtonFix btnFix = new ButtonFix("Sửa");
        ButtonDele btnDele = new ButtonDele("Xóa");
        ButtonRefresh btnRefresh = new ButtonRefresh("Làm Mới");
        ButtonXuatExcel btnXuatExcel = new ButtonXuatExcel("Xuất Excel");
    
        add(txtSearch, "growx, h 35!");       
        add(btnAdd, "w 95!, h 35!");  
        add(btnFix, "w 95!, h 35!");
        add(btnDele, "w 95!, h 35!");         
        add(btnRefresh, "w 115!, h 35!");
        add(btnXuatExcel, "w 105!, h 35!");     

        btnAdd.addActionListener(e -> {
            JFrame frameCha = (JFrame) SwingUtilities.getWindowAncestor(this);
            ThemSanPhamDialog dialog = new ThemSanPhamDialog(frameCha);
            dialog.setVisible(true);
            parentPanel.loadData();
        });

        btnFix.addActionListener(e -> {
            JTable tbl = parentPanel.getTable().getTable();
            int selectedRow = tbl.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hàng để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ma = tbl.getValueAt(selectedRow, 1).toString();
            String ten = tbl.getValueAt(selectedRow, 2).toString();
            String sl = tbl.getValueAt(selectedRow, 3).toString();
            String maLoai = tbl.getValueAt(selectedRow, 4).toString();
            String maHang = tbl.getValueAt(selectedRow, 5).toString();
            String mota = tbl.getValueAt(selectedRow, 6).toString();
            String baohanh = tbl.getValueAt(selectedRow, 7).toString();

            JFrame frameCha = (JFrame) SwingUtilities.getWindowAncestor(this);
            SuaSanPhamDialog dialog = new SuaSanPhamDialog(frameCha, ma, ten, sl, maLoai, maHang, mota, baohanh);
            dialog.setVisible(true);

            if (dialog.isSuccess()) {
                parentPanel.loadData();
            }
        });
        
        btnDele.addActionListener(e -> {
            JTable tbl = parentPanel.getTable().getTable();
            int selectedRow = tbl.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maSP = tbl.getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa: " + maSP + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                if(spBUS.delete(maSP)){
                JOptionPane.showMessageDialog(this,"Xóa thành công");
                parentPanel.loadData();
                }
            }
        });

        btnXuatExcel.addActionListener(e -> {
            XuatExcel.xuat(parentPanel.getTable().getTable());
        });

        btnRefresh.addActionListener(e -> {
            txtSearch.setText(""); 
            JTable tbl = parentPanel.getTable().getTable();
            if (tbl.getRowSorter() != null) {
                //@SuppressWarnings("unchecked")
                TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbl.getRowSorter();
                sorter.setRowFilter(null);
            }
            parentPanel.getTable().resetFilters();
            parentPanel.loadData();
        });
    }
    
    public String getKeyword() { return txtSearch.getText(); }
}
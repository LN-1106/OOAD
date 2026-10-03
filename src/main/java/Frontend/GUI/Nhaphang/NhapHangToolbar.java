package Frontend.GUI.Nhaphang;

import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.ButtonNhapExcel;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.DocExcel;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import Backend.BUS.LoaiSPBUS;
import Backend.DAO.LoaiSPDAO;
import Backend.DTO.ThuocTinhSanPham.LoaiSP;

import java.awt.*;
import java.util.ArrayList;

public class NhapHangToolbar extends JPanel{
    private NhapHangTable table;
    private JComboBox<LoaiSP> cbFilter;
    private NhapHangSidebar sidebar;

    public NhapHangToolbar(NhapHangTable table, NhapHangSidebar sidebar) {
        this.table = table;
        this.sidebar = sidebar;
        setLayout(new MigLayout("insets 10", "[grow]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: 20");
        setOpaque(false);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm tên SP, mã SP...");
        add(txtSearch, "growx, h 35!");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().trim();
                table.loadDataBySearch(query);
            }
        });

        ButtonXuatExcel btnNhapExcel = new ButtonXuatExcel("Nhập Excel");
        btnNhapExcel.addActionListener(e -> {
            ArrayList<Object[]> dataList = DocExcel.docDuLieuNhapHang();
            
            if (!dataList.isEmpty()) {
                this.sidebar.nhapTuExcel(dataList);
            }
        });

        add(btnNhapExcel, "h 35!, w 105!");

        cbFilter = new JComboBox<>();
        DefaultComboBoxModel<LoaiSP> model = new DefaultComboBoxModel<>();
        model.addElement(new LoaiSP("All", "Tất cả sản phẩm"));

        LoaiSPBUS loaiBUS = new LoaiSPBUS();
        ArrayList<LoaiSP> listLoai = loaiBUS.getAll();
        for(LoaiSP l : listLoai) {
            model.addElement(l);
        }
        cbFilter.setModel(model);

        cbFilter.addActionListener(e -> {
            LoaiSP selected = (LoaiSP) cbFilter.getSelectedItem();
            if(selected != null) {
                table.loadDataByLoai(selected.getMaLoai());
            }
        });

        cbFilter.setPreferredSize(new Dimension(200, 35));
        cbFilter.putClientProperty("FlatLaf.style", "arc: 10");
        add(cbFilter, "h 35!,w 130!");
    }
}

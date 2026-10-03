package Frontend.GUI.Nhaphang;

import Backend.BUS.PhienBanSanPhamBUS;
import Backend.DTO.PhienBanSanPham;
import Frontend.Compoent.Table;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NhapHangTable extends JScrollPane {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private PhienBanSanPhamBUS phienbansanphamBUS;
    private NhapHangSidebar sidebar;
    private String currentSearchQuery = "";
    private String currentMaLoai = "All";
    private String currentMaNCC = "All";
    private TableRowSorter<DefaultTableModel> sorter;

    public NhapHangTable(NhapHangSidebar sidebar) {
        phienbansanphamBUS = new PhienBanSanPhamBUS();
        this.sidebar = sidebar;
        initTable();
        loadData();
    }

    public void setSidebar(NhapHangSidebar sidebar) {
        this.sidebar = sidebar;
    }

    private void initTable() {
        String[] columns = {"STT", "Mã Phiên Bản", "Tên Sản Phẩm", "Màu Sắc", "Giá Nhập", "Số Lượng Tồn"};
        tblModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tbl = new Table();
        tbl.setModel(tblModel);
        sorter = new TableRowSorter<>(tblModel);
        tbl.setRowSorter(sorter);

        tbl.getColumnModel().getColumn(0).setPreferredWidth(60);
        tbl.getColumnModel().getColumn(0).setMaxWidth(70);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(110);
        tbl.getColumnModel().getColumn(1).setMaxWidth(120);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbl.getColumnModel().getColumn(2).setMaxWidth(230);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbl.getColumnModel().getColumn(3).setMaxWidth(160);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbl.getColumnModel().getColumn(5).setMaxWidth(130);

        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tbl.getSelectedRow();
                if (row == -1) return;

                String maPB = tbl.getValueAt(row, 1).toString();
                String tenSP = tbl.getValueAt(row, 2).toString();
                String mauSP = tbl.getValueAt(row, 3).toString();
                String gia = tbl.getValueAt(row, 4).toString();
                String ton = tbl.getValueAt(row, 5).toString();

                // Đẩy dữ liệu sang Sidebar thông qua hàm public
                sidebar.updateInfo(maPB, tenSP, gia, ton);

                // if (e.getClickCount() == 2) {
                //     new ChiTietSanPhamDialog(null, ma, ten, gia).setVisible(true);
                // }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        setViewportView(tbl);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
    }

    public void loadData() {
        applyFiltersNhapHang();
    }
    
    public void applyFiltersNhapHang() {
        tblModel.setRowCount(0);

        ArrayList<PhienBanSanPham> list = phienbansanphamBUS.getFilteredListNhapHang(currentMaNCC, currentMaLoai, currentSearchQuery);

        DecimalFormat df = new DecimalFormat("#,###");
        int stt = 1;
        for (PhienBanSanPham pbsp : list) {
            Object[] row = {
                stt++,
                pbsp.getMaPhienBan(),
                pbsp.getTenSP(),
                pbsp.getMauSac(),
                df.format(pbsp.getGiaNhap()),
                pbsp.getSoLuongTon(),
            };
            tblModel.addRow(row);
        }
    }

    public void loadDataBySearch(String query) {
        this.currentSearchQuery = query;
        applyFiltersNhapHang();
    }

    public void loadDataByLoai(String maLoai) {
        this.currentMaLoai = maLoai;
        applyFiltersNhapHang();
    }

    public void loadDataByNCC(String maNCC) {
        this.currentMaNCC = maNCC;
        applyFiltersNhapHang();
    }
}
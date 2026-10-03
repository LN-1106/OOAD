package Frontend.GUI.SanPham;

import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import Backend.DTO.SanPham;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class SanPhamTable extends JPanel {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;
    private JComboBox<String> cboPhanLoai,cboHang;
    private TableRowSorter<DefaultTableModel> sorter;

    public SanPhamTable() {
        setLayout(new MigLayout("wrap 1, fill, insets 10", "[grow]", "[]15[grow]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        initHeader();
        initTable();
    }

    private void initHeader() {
        JPanel pnlHeader = new JPanel(new MigLayout("insets 10","[]push[][][][]"));
        pnlHeader.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        JLabel lblTitle = new JLabel("Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cboPhanLoai = new JComboBox<>(new String[] { "Tất cả", "Loa", "Tai nghe", "Phụ kiện" });
        cboPhanLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboPhanLoai.setBackground(Color.WHITE);
        cboPhanLoai.setFocusable(false);
        cboPhanLoai.setPreferredSize(new Dimension(130, 30));

        cboHang = new JComboBox<>(new String[] { "Tất cả", "Marsall", "Sony", "JBL" });
        cboHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboHang.setBackground(Color.WHITE);
        cboHang.setFocusable(false);
        cboHang.setPreferredSize(new Dimension(130, 30));

        cboPhanLoai.addActionListener(e -> {
            loai = cboPhanLoai.getSelectedItem().toString();
            Filter();
            // if (sorter != null) {
            //     if (selected.equals("Tất cả")) {
            //         sorter.setRowFilter(null);
            //     } else {
            //         String maLoai = "";
            //         switch (selected) {
            //             case "Loa":maLoai = "L01"; break;
            //             case "Tai nghe":maLoai = "L02"; break;
            //             case "Phụ kiện":maLoai = "L03"; break;
            //         }
            //         sorter.setRowFilter(RowFilter.regexFilter("^" + maLoai + "$", 4));
            //     }
            // }
        });

        cboHang.addActionListener(e->{
            hang=cboHang.getSelectedItem().toString();
            Filter();
        });

        pnlHeader.add(lblTitle);
        pnlHeader.add(new JLabel("Loại:"));
        pnlHeader.add(cboPhanLoai, "w 120!, h 35!");
        pnlHeader.add(new JLabel("Hãng:"));
        pnlHeader.add(cboHang, "w 120!, h 35!");
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] columns = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Mã Loại", "Mã Hãng", "Mô Tả", "Thời Gian Bảo Hành" };

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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(null);
        add(scrollPane, "grow");
    }

    public void showData(ArrayList<SanPham> list) {
        tblModel.setRowCount(0);
        int stt = 1;
        if (list != null) {
            for (SanPham sp : list) {
                tblModel.addRow(new Object[] {
                    stt++, sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(), sp.getMaLoai(), sp.getMaHang(), sp.getMoTa(),sp.getThoiGianBaoHanh()
                });
            }
        }
    }

    public void resetFilters() {
        if (cboPhanLoai != null) cboPhanLoai.setSelectedIndex(0); 
        if (cboHang != null) cboHang.setSelectedIndex(0);
        // this.loai = "Tất cả";
        // this.hang = "Tất cả";
        // this.textTK = "";
        if (tbl.getRowSorter() != null) {
        ((TableRowSorter<?>) tbl.getRowSorter()).setRowFilter(null);
    }
    }

    public JTable getTable() { return tbl; }

    private String textTK="";
    private String loai="Tất cả";
    private String hang="Tất cả";
    public void search(String text){
            this.textTK=text;
            Filter();
        }

    private void Filter() {

        TableRowSorter<?> sorter = (TableRowSorter<?>) tbl.getRowSorter();
        if (sorter == null) return;
        java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();

        if (!loai.equalsIgnoreCase("Tất cả")) {
            String maLoai = "";
            switch (loai) {
                case "Loa": maLoai = "L01"; break;
                case "Tai nghe": maLoai = "L02"; break;
                case "Phụ kiện": maLoai = "L03"; break;
            }
            if (!maLoai.isEmpty()) {
                filters.add(RowFilter.regexFilter("^" + maLoai + "$", 4));
            }
        }

        if (!hang.equalsIgnoreCase("Tất cả")) {
            String maHang = "";
            switch (hang) {
                case "Marsall": maHang = "H01"; break; 
                case "Sony": maHang = "H02"; break;
                case "JBL": maHang = "H03"; break;
            }
            if (!maHang.isEmpty()) {
                filters.add(RowFilter.regexFilter("^" + maHang + "$", 5));
            }
        }
        if (!textTK.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(textTK)));
        }
        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }
    
}

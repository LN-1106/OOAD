package Frontend.GUI.KhachHang;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Backend.BUS.KhachHangBUS;
import Backend.DTO.KhachHang;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class KhachHangTable extends JPanel {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;
    private KhachHangBUS khBUS;
    // private JComboBox<KhachHang> cboKH;
    private JComboBox<String> cboSort;
    private TableRowSorter<DefaultTableModel> sorter;

    public KhachHangTable() {
        khBUS = new KhachHangBUS();
        setLayout(new MigLayout("wrap 1, fill, insets 10", "[grow]", "[]15[grow]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: 20");

        initFilterHeader();
        initTable();
        loadData();
    }

    private void initFilterHeader() {
        JPanel pnlHeader = new JPanel(new MigLayout("insets 10", "[]push[]"));
        pnlHeader.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        JLabel lblTitle = new JLabel("Khách hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // cboKH = new JComboBox<>();
        // loadComboBox();

        // cboKH.addActionListener(e -> {
        //     KhachHang selected = (KhachHang) cboKH.getSelectedItem();
        //     if (selected != null) {
        //         if (selected.getId().equals("All")) {
        //             loadData();
        //         } else {
        //             loadDataByFilter(selected.getId());
        //         }
        //     }
        // });

        // cboKH.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        // pnlHeader.add(lblTitle);
        // pnlHeader.add(cboKH, "w 150!, h 35!");
        // add(pnlHeader, "growx");
        cboSort = new JComboBox<>(new String[]{
                "Tên mặc định",
                "Tên A - Z",
                "Tên Z - A"
        });

    cboSort.addActionListener(e -> {
    int colTen = 2; // cột "Tên KH"

        switch (cboSort.getSelectedIndex()) {
            case 0: // Mặc định
                sorter.setSortKeys(null);
                break;

            case 1: // A - Z
                sorter.setSortKeys(
                    java.util.List.of(
                        new RowSorter.SortKey(colTen, SortOrder.ASCENDING)
                    )
                );
                break;

            case 2: // Z - A
                sorter.setSortKeys(
                    java.util.List.of(
                        new RowSorter.SortKey(colTen, SortOrder.DESCENDING)
                    )
                );
                break;
        }
    });


        cboSort.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        pnlHeader.add(lblTitle);
        pnlHeader.add(cboSort, "w 150!, h 35!");
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] columns = { "STT", "Mã KH", "Tên KH", "SĐT", "Địa chỉ" };
        tblModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tbl = new Table();
        tbl.setModel(tblModel);

        sorter = new TableRowSorter<>(tblModel);
        tbl.setRowSorter(sorter);
        
        sorter.setComparator(2, (o1, o2) -> {
            String ten1 = o1.toString().trim();
            String ten2 = o2.toString().trim();

            String last1 = ten1.substring(ten1.lastIndexOf(" ") + 1);
            String last2 = ten2.substring(ten2.lastIndexOf(" ") + 1);

            return last1.compareToIgnoreCase(last2);
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(null);
        add(scrollPane, "grow");
    }

    public void loadData() {
        tblModel.setRowCount(0);
        ArrayList<KhachHang> listKH = khBUS.getAllKhachHang();

        int stt = 1;
        for (KhachHang kh : listKH) {
            Object[] row = {
                    stt++,
                    kh.getId(),
                    kh.getHoTen(),
                    kh.getSdt(),
                    kh.getDiaChi()
            };
            tblModel.addRow(row);
        }
    }

    public void loadDataByFilter(String maKH) {
        tblModel.setRowCount(0);
        ArrayList<KhachHang> listKH = khBUS.getAllKhachHang();

        int stt = 1;
        for (KhachHang kh : listKH) {
            if (kh.getId().equals(maKH)) {
                Object[] row = {
                        stt++,
                        kh.getId(),
                        kh.getHoTen(),
                        kh.getSdt(),
                        kh.getDiaChi()
                };
                tblModel.addRow(row);
            }
        }
    }

    public JTable getTbl() { return tbl; }
        public void filterByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            sorter.setRowFilter(null); // reset
            return;
        }

        String[] words = keyword.toLowerCase().trim().split("\\s+");

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String ma = entry.getStringValue(1).toLowerCase();   // Mã NV
                String ten = entry.getStringValue(2).toLowerCase();  // Tên NV
                String sdt = entry.getStringValue(3).toLowerCase();  // SĐT
                String diaChi = entry.getStringValue(4).toLowerCase();
                
                String fullInfo = ma + " " + ten + " " + sdt + " " + diaChi;

                for (String word : words) {
                    if (!fullInfo.contains(word)) {
                        return false;
                    }
                }
                return true;
            }
        });
    }
    // public void loadComboBox() {
    //     if (cboKH == null) return;
    //     Object selected = cboKH.getSelectedItem();
    //     cboKH.removeAllItems();
    //     cboKH.addItem(new KhachHang("All", "Tất cả", "", "", true));
    //     ArrayList<KhachHang> list = khBUS.getAllKhachHang();
    //     for (KhachHang kh : list) {
    //         cboKH.addItem(kh);
    //     }
    //     if (selected != null) cboKH.setSelectedItem(selected);
    // }

    // public void loadDataBySearch(String query) {
    //     tblModel.setRowCount(0);
    //     ArrayList<KhachHang> list = khBUS.getAllKhachHang();

    //     int stt = 1;
    //     for (KhachHang kh : list) {
    //         boolean matchMa = kh.getId().toLowerCase().contains(query.toLowerCase());
    //         boolean matchTen = kh.getHoTen().toLowerCase().contains(query.toLowerCase());
    //         if (matchMa || matchTen) {
    //             Object[] row = {
    //                     stt++,
    //                     kh.getId(),
    //                     kh.getHoTen(),
    //                     kh.getSdt(),
    //                     kh.getDiaChi()
    //             };
    //             tblModel.addRow(row);
    //         }
    //     }
    //     if (query.isEmpty()) cboKH.setSelectedIndex(0);
    // }
//     public void resetFilters() {
//         cboSort.setSelectedIndex(0);
//         cboSortLuong.setSelectedIndex(0);
//         cboChucVu.setSelectedIndex(0);

//         currentKeyword = "";

//         if (sorter != null) {
//             sorter.setRowFilter(null);
//             sorter.setSortKeys(null);
//         }
//     }
//     btnRefresh.addActionListener(e -> {
//     txtSearch.setText("");
//     table.resetFilters();
//     table.loadData();
// });

    public void loadDataByAdvancedFilter(String ma, String ten, String diaChi, String sdt){
        tblModel.setRowCount(0);
        ArrayList<KhachHang> list = khBUS.getAllKhachHang();

        int stt = 1;
        for (KhachHang kh : list) {
            boolean matchMa = kh.getId().toLowerCase().contains(ma.toLowerCase());
            boolean matchTen = kh.getHoTen().toLowerCase().contains(ten.toLowerCase());
            boolean matchDiaChi = kh.getDiaChi().toLowerCase().contains(diaChi.toLowerCase());
            boolean matchSDT = kh.getSdt().toLowerCase().contains(sdt.toLowerCase());
            if (matchMa && matchTen && matchDiaChi && matchSDT) {
                Object[] row = {
                        stt++,
                        kh.getId(),
                        kh.getHoTen(),
                        kh.getSdt(),
                        kh.getDiaChi()
                };
                tblModel.addRow(row);
            }
        }
    }
}
// btnExcel.addActionListener(e -> {

//     JFileChooser chooser = new JFileChooser();
//     int result = chooser.showOpenDialog(null);

//     if (result == JFileChooser.APPROVE_OPTION) {

//         File file = chooser.getSelectedFile();

//         try {

//             FileInputStream fis = new FileInputStream(file);
//             Workbook workbook = new XSSFWorkbook(fis);
//             Sheet sheet = workbook.getSheetAt(0);

//             boolean firstRow = true;
//             int soNhanVienThem = 0;

//             for (Row row : sheet) {

//                 // bỏ dòng tiêu đề
//                 if (firstRow) {
//                     firstRow = false;
//                     continue;
//                 }

//                 DataFormatter formatter = new DataFormatter();

//                 String ma = formatter.formatCellValue(row.getCell(0));
//                 String ten = formatter.formatCellValue(row.getCell(1));
//                 String sdt = formatter.formatCellValue(row.getCell(2));
//                 String diaChi = formatter.formatCellValue(row.getCell(3));
//                 String chucVu = formatter.formatCellValue(row.getCell(4));
//                 String email = formatter.formatCellValue(row.getCell(5));
//                 double luong = row.getCell(6).getNumericCellValue();

//                 NhanVien nv = new NhanVien(ma, ten, sdt, diaChi, chucVu, email, luong);

//                 String check = nvBUS.validate(nv, true);

//                 if (check.equals("OK")) {
//                     nvBUS.add(nv);
//                     soNhanVienThem++;
//                 }
//             }

//             workbook.close();
//             fis.close();

//             table.loadData();

//             JOptionPane.showMessageDialog(null,
//                     "Đã nhập " + soNhanVienThem + " nhân viên từ Excel!",
//                     "Thành công",
//                     JOptionPane.INFORMATION_MESSAGE);

//         } catch (Exception ex) {
//             ex.printStackTrace();
//             JOptionPane.showMessageDialog(null,
//                     "Lỗi đọc file Excel!",
//                     "Error",
//                     JOptionPane.ERROR_MESSAGE);
//         }
//     }
// });
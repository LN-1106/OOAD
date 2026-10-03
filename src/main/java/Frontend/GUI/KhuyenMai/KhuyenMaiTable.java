package Frontend.GUI.KhuyenMai;

import Backend.BUS.KhuyenMaiBUS;
import Backend.DTO.KhuyenMai;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class KhuyenMaiTable extends JPanel {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;
    private KhuyenMaiBUS kmBUS;
    private JComboBox<String> cboStatus; // Đổi tên cho giống cboStatus bên Bảo hành
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private TableRowSorter<DefaultTableModel> sorter;
    private String currentKeyword = "";

    public KhuyenMaiTable() {
        kmBUS = new KhuyenMaiBUS();
        // Layout và màu nền WHITE giống hệt file BaoHanhTable bạn gửi
        setLayout(new MigLayout("wrap 1, fill, insets 10", "[grow]", "[]15[grow]"));
        setBackground(Color.WHITE); 
        putClientProperty("FlatLaf.style", "arc: 20");

        initHeader(); // Đổi tên hàm cho khớp
        initTable();
        loadData();
    }

    private void initHeader() {
        // Cấu trúc Header: insets 10, gapx 5 và không set Background (để lấy màu trắng của cha)
        JPanel pnlHeader = new JPanel(new MigLayout("insets 10, gapx 5", "[]push[]"));
        pnlHeader.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        
        JLabel lblTitle = new JLabel("Khuyến mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cboStatus = new JComboBox<>(new String[] {
            "Tất cả trạng thái", "Đang hoạt động", "Ngừng hoạt động"
        });
        cboStatus.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        
        cboStatus.addActionListener(e -> applyFilters());

        pnlHeader.add(lblTitle);
        pnlHeader.add(cboStatus, "w 200!, h 35!");
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] header = {"STT", "Mã KM", "Tên chương trình", "% Giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbl = new Table();
        tbl.setModel(tblModel);

        sorter = new TableRowSorter<>(tblModel);
        tbl.setRowSorter(sorter);

        sorter.setComparator(3, (String s1, String s2) -> {
        // Loại bỏ ký tự '%' và chuyển về kiểu Double để so sánh số học
        Double d1 = Double.parseDouble(s1.replace("%", "").trim());
        Double d2 = Double.parseDouble(s2.replace("%", "").trim());
        return d1.compareTo(d2);
        });
        // Căn giữa dữ liệu giống Bảo hành
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(null); // Bỏ viền ScrollPane giống Bảo hành
        add(scrollPane, "grow");
    }

    public void loadData() {
        kmBUS.refreshData();
        displayData(kmBUS.getAllKhuyenMai());
    }

    // Giữ tên displayData để KhuyenMaiToolbar gọi không bị lỗi symbol
    public void displayData(ArrayList<KhuyenMai> list) {
        tblModel.setRowCount(0);
        int stt = 1;
        for (KhuyenMai km : list) {
            Object[] row = {
                stt++,
                km.getMaKM(),
                km.getTenKM(),
                km.getPhanTramGiam() + "%",
                sdf.format(km.getNgayBD()),
                sdf.format(km.getNgayKT()),
                (km.getTrangThai() == 1) ? "Đang hoạt động" : "Ngừng hoạt động"
            };
            tblModel.addRow(row);
        }
    }

    // Cơ chế lọc applyFilters giống hệt Bảo hành
    public void applyFilters() {
        String status = cboStatus.getSelectedItem().toString().trim();
        String keyword = currentKeyword.toLowerCase().trim();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Cột 6 là Trạng thái
                String trangThaiTable = entry.getStringValue(6);
                String maKM = entry.getStringValue(1).toLowerCase();
                String tenKM = entry.getStringValue(2).toLowerCase();

                boolean matchSearch = keyword.isEmpty() || maKM.contains(keyword) || tenKM.contains(keyword);
                boolean matchStatus = status.equals("Tất cả trạng thái") || trangThaiTable.equals(status);

                return matchSearch && matchStatus;
            }
        });
    }

    public void loadDataBySearch(String query) {
        this.currentKeyword = query.trim();
        applyFilters();
    }

    public JTable getTbl() {
        return tbl;
    }
}

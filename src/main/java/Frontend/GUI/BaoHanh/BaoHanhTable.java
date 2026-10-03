package Frontend.GUI.BaoHanh;

import Backend.DTO.BaoHanh;
import Backend.BUS.BaoHanhBUS;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BaoHanhTable extends JPanel {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;
    private BaoHanhBUS baoHanhBUS = new BaoHanhBUS();
    private JComboBox<String> cboStatus;
    private TableRowSorter<DefaultTableModel> sorter;
    private String currentKeyword = "";

    private static final String STATUS_ALL = "Tất cả trạng thái";
    private static final String STATUS_WARRANTY = "Còn bảo hành";
    private static final String STATUS_FIXING = "Đang sửa chữa";
    private static final String STATUS_DONE = "Hoàn thành";
    private static final String STATUS_RETURNED = "Đã trả máy";

    public BaoHanhTable() {
        setLayout(new MigLayout("wrap 1, fill, insets 10", "[grow]", "[]15[grow]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: 20");

        initHeader();
        initTable();
        loadData();
        addTableEvents();
    }

    private void initHeader() {
        JPanel pnlHeader = new JPanel(new MigLayout("insets 10, gapx 5", "[]push[]"));
        pnlHeader.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        JLabel lblTitle = new JLabel("Bảo hành");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cboStatus = new JComboBox<>(new String[] {
                STATUS_ALL, STATUS_WARRANTY, STATUS_FIXING, STATUS_DONE, STATUS_RETURNED
        });
        cboStatus.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        cboStatus.addActionListener(e -> applyFilters());

        pnlHeader.add(lblTitle);
        pnlHeader.add(cboStatus, "w 160!, h 35!");
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] colums = { "STT", "Mã bảo hành", "Mã Imei", "Mã phiếu xuất", "Ngày bắt đầu", "Ngày kết thúc",
                "Tình trạng" };
        tblModel = new DefaultTableModel(colums, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbl = new Table();
        tbl.setModel(tblModel);

        sorter = new TableRowSorter<>(tblModel);
        tbl.setRowSorter(sorter);

        tbl.getColumnModel().getColumn(6).setMinWidth(0);
        tbl.getColumnModel().getColumn(6).setMaxWidth(0);
        tbl.getColumnModel().getColumn(6).setPreferredWidth(0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(null);
        add(scrollPane, "grow");
    }

    public void loadData() {
        renderTable(baoHanhBUS.getAllBaoHanh());
    }

    public void applyFilters() {
        String selected = cboStatus.getSelectedItem().toString().trim();
        String keyword = currentKeyword.toLowerCase().trim();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String trangThai = entry.getStringValue(6).trim();

                String maBH = entry.getStringValue(1).toLowerCase();
                String imei = entry.getStringValue(2).toLowerCase();

                boolean matchSearch = keyword.isEmpty()
                        || maBH.contains(keyword)
                        || imei.contains(keyword);

                boolean matchStatus = selected.equals(STATUS_ALL)
                        || trangThai.equalsIgnoreCase(selected);

                return matchSearch && matchStatus;
            }
        });
    }

    private void renderTable(ArrayList<BaoHanh> list) {
        tblModel.setRowCount(0);
        int stt = 1;
        for (BaoHanh bh : list) {
            String trangThaiThuc = bh.getTinhTrang();
            if (trangThaiThuc == null || trangThaiThuc.trim().isEmpty()) {
                trangThaiThuc = STATUS_WARRANTY; 
            }

            tblModel.addRow(new Object[] {
                    stt++,
                    bh.getMaBH(),
                    bh.getMaImei(),
                    bh.getMaPhieuXuat(),
                    bh.getNgayBatDau(),
                    bh.getNgayKetThuc(),
                    trangThaiThuc
            });
        }
    }

    public JTable getTbl() {
        return tbl;
    }

    public void loadDataBySearch(String query) {
        this.currentKeyword = query.trim();
        applyFilters();
    }

    private void addTableEvents() {
        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tbl.getSelectedRow();
                    if (row != -1) {
                        String maBH = tbl.getValueAt(row, 1).toString();
                        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(BaoHanhTable.this);
                        ChiTietBaoHanhDialog dialog = new ChiTietBaoHanhDialog(parent, maBH);
                        dialog.setVisible(true);
                    }
                }
            }
        });
    }
}

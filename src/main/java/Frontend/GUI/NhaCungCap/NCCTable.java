package Frontend.GUI.NhaCungCap;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Backend.BUS.NhaCungCapBUS;
import Backend.DTO.NhaCungCap;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class NCCTable extends JPanel {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;
    private NhaCungCapBUS nccBUS;
    private JComboBox<NhaCungCap> cboNCC;
    private TableRowSorter<DefaultTableModel> sorter;


    public NCCTable() {
        nccBUS = new NhaCungCapBUS();
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
        JLabel lblTitle = new JLabel("Nhà cung cấp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cboNCC = new JComboBox<>();
        loadComboBox();

        cboNCC.addActionListener(e -> {
            NhaCungCap selected = (NhaCungCap) cboNCC.getSelectedItem();
            if (selected != null) {
                if (selected.getMaNCC().equals("All")) {
                    loadData();
                } else {
                    loadDataByFilter(selected.getTenNCC());
                }
            }
        });

        cboNCC.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        pnlHeader.add(lblTitle);
        pnlHeader.add(cboNCC, "w 150!, h 35!");
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] colums = { "STT", "Mã Nhà cung cấp", "Tên Nhà cung cấp", "Địa chỉ", "SĐT" };
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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(null);
        add(scrollPane, "grow");

        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int row = tbl.getSelectedRow();
                    String ma = tbl.getValueAt(row, 1).toString();
                    String ten = tbl.getValueAt(row, 2).toString();

                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(NCCTable.this);
                    new NCCGanSPDialog(parent, ma, ten).setVisible(true);
                }
            }
        });
    }

    public void loadData() {
        tblModel.setRowCount(0);
        ArrayList<NhaCungCap> listNCC = nccBUS.getAllNhaCungCap();

        int stt = 1;
        for (NhaCungCap ncc : listNCC) {
            Object[] row = {
                    stt++,
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getDiaChi(),
                    ncc.getSdt(),
            };
            tblModel.addRow(row);
        }
    }

    public void loadDataByFilter(String tenNCC) {
        tblModel.setRowCount(0);
        ArrayList<NhaCungCap> listNCC = nccBUS.getAllNhaCungCap();

        int stt = 1;
        for (NhaCungCap ncc : listNCC) {
            if (ncc.getTenNCC().equals(tenNCC)) {
                Object[] row = {
                        stt++,
                        ncc.getMaNCC(),
                        ncc.getTenNCC(),
                        ncc.getDiaChi(),
                        ncc.getSdt(),
                };
                tblModel.addRow(row);
            }
        }
    }

    public JTable getTbl() {
        return tbl;
    }

    public void loadComboBox() {
        if (cboNCC == null) return;

        Object selected = cboNCC.getSelectedItem(); // Lưu lại item đang được chọn hiện tại
        cboNCC.removeAllItems(); // Xóa sạch dữ liệu cũ
        cboNCC.addItem(new NhaCungCap("All", "Tất cả", "", ""));
        ArrayList<NhaCungCap> list = nccBUS.getAllNhaCungCap();
        ArrayList<String> addNames = new ArrayList<>();
        for (NhaCungCap ncc : list) {
            String currentName = ncc.getTenNCC().trim();
            if (!addNames.contains(currentName)) {
                cboNCC.addItem(ncc);
                addNames.add(currentName);
            }
        }

        if (selected != null)
            cboNCC.setSelectedItem(selected);
    }

    public void loadDataBySearch(String query) {
        tblModel.setRowCount(0);
        ArrayList<NhaCungCap> list = nccBUS.getAllNhaCungCap();

        String lowerQuery = query.toLowerCase().trim();
        if(lowerQuery.isEmpty()) {
            loadData();
            return;
        }
        String[] keyWords = lowerQuery.split("\\s+");

        int stt = 1;
        for (NhaCungCap ncc : list) {
            String infoToSearch = (ncc.getMaNCC() + " " +
                                   ncc.getTenNCC() + " " +
                                   ncc.getDiaChi() + " " +
                                   ncc.getSdt()).toLowerCase();

            boolean matchesAll = true;
            for(String word : keyWords) {
                if(!infoToSearch.contains(word)) {
                    matchesAll = false;
                    break;
                }
            }

            if (matchesAll) {
                tblModel.addRow(new Object[]{
                        stt++,
                        ncc.getMaNCC(),
                        ncc.getTenNCC(),
                        ncc.getDiaChi(),
                        ncc.getSdt(),
                });
            }
        }
    }
}

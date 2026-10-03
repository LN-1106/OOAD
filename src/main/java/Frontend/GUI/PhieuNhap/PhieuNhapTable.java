package Frontend.GUI.PhieuNhap;

import Backend.BUS.PhieuNhapBUS;
import Backend.DTO.PhieuNhap;
import Frontend.Compoent.Table;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhieuNhapTable extends JScrollPane {
    private JTable tbl;     
    private DefaultTableModel tblModel;
    private PhieuNhapBUS phieuNhapBUS;
    private TableRowSorter<DefaultTableModel> sorter;


    public PhieuNhapTable() {
        phieuNhapBUS = new PhieuNhapBUS();
        initTable();
        loadData();
        addTableEvents();
    }

    private void initTable() {
        String[] columns = { "STT", "Mã HĐN", "Ngày Lập", "Mã NV", "Tổng Tiền" };
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

        // Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        setViewportView(tbl);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
    }

    public void filter(/*String maNCC,*/ Date from, Date to, String minStr, String maxStr) {
        long min = (minStr == null || minStr.trim().isEmpty()) ? 0 : Long.parseLong(minStr.replaceAll("[^0-9]", ""));
        long max = (maxStr == null || maxStr.trim().isEmpty()) ? 9999999999L : Long.parseLong(maxStr.replaceAll("[^0-9]", ""));

        tblModel.setRowCount(0);
        ArrayList<PhieuNhap> list = phieuNhapBUS.getFilteredPhieuNhap(/*maNCC,*/ from, to, min, max);

        DecimalFormat df = new DecimalFormat("#,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        int stt = 1;
        for (PhieuNhap pn : list) {
            tblModel.addRow(new Object[]{stt++, pn.getmaPhieuNhap(), sdf.format(pn.getngayNhap()), 
                                        pn.getmaNV(), df.format(pn.getTongTien())});
        }
    }

    public void loadDataBySearch(String query) {
        tblModel.setRowCount(0);
        ArrayList<PhieuNhap> list = phieuNhapBUS.getAllPhieuNhap();
        DecimalFormat df = new DecimalFormat("#,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String lowerQuery = query.toLowerCase().trim();
        if(lowerQuery.isEmpty()) {
            loadData();
            return;
        }
        String[] keyWords = lowerQuery.split("\\s+");

        int stt = 1;
        for (PhieuNhap pn : list) {
            String tongTienStr = String.valueOf((long)pn.getTongTien());
            String ngayNhapStr = sdf.format(pn.getngayNhap());

            String infoToSearch = (pn.getmaPhieuNhap() + " " +
                                   ngayNhapStr + " " +
                                   pn.getmaNV() + " " +
                                   pn.getmaNCC() + " " +
                                   tongTienStr).toLowerCase();

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
                    pn.getmaPhieuNhap(),
                    ngayNhapStr,
                    pn.getmaNV(),
                    df.format(pn.getTongTien())
                });
            }
        }
    }

    public void loadData() {
        filter(/*"All",*/ null, null, "", "");
    }

    private void addTableEvents() {
        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tbl.getSelectedRow();
                    if (row != -1) {
                        String maPN = tblModel.getValueAt(row, 1).toString();
                        openDetailDialog(maPN);
                    }
                }
            }
        });
    }

    private void openDetailDialog(String maPN) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChiTietHoaDonNhapDialog dialog = new ChiTietHoaDonNhapDialog(parentFrame, maPN);
        dialog.setVisible(true);
    }

    public JTable getTable() {
        return tbl;
    }
}

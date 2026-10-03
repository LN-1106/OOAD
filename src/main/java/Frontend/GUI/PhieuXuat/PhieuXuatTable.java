package Frontend.GUI.PhieuXuat;

import Backend.BUS.PhieuXuatBUS;
import Backend.DTO.PhieuXuat;
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

public class PhieuXuatTable extends JScrollPane {
    private JTable tbl;
    private DefaultTableModel tblModel;
    private PhieuXuatBUS phieuXuatBUS;
    private TableRowSorter<DefaultTableModel> sorter;

    public PhieuXuatTable() {
        phieuXuatBUS = new PhieuXuatBUS();
        initTable();
        loadData("");
        addTableEvents();
    }

    private void initTable() {
        String[] columns = { "STT", "Mã HĐX", "Ngày Bán", "Mã NV", "Khách Hàng", "Tổng Tiền" };
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

        setViewportView(tbl);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
    }

    public void loadData(String keyword) {
        tblModel.setRowCount(0);
        ArrayList<PhieuXuat> listPX = phieuXuatBUS.search(keyword);

        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        int stt = 1;
        for (PhieuXuat px : listPX) {
            Object[] row = {
                    stt++,
                    px.getMaPhieuXuat(),
                    px.getNgayXuat() != null ? sdf.format(px.getNgayXuat()) : "N/A",
                    px.getMaNV(),
                    px.getMaKH(),
                    df.format(px.getTongTien())
            };
            tblModel.addRow(row);
        }
    }

    public void reload() {
        loadData("");
    }

    private void addTableEvents() {
        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tbl.getSelectedRow();
                    if (row != -1) {
                        String maPX = tblModel.getValueAt(row, 1).toString();
                        openDetailDialog(maPX);
                    }
                }
            }
        });
    }

    private void openDetailDialog(String maPX) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChiTietPhieuXuatDialog dialog = new ChiTietPhieuXuatDialog(parentFrame, maPX);
        dialog.setVisible(true);
    }

    public void filterData(Date start, Date end, String nv, double minPrice, double maxPrice) {
        tblModel.setRowCount(0);
        ArrayList<PhieuXuat> listPX = phieuXuatBUS.selectAll(); // Lấy tất cả để lọc

        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        int stt = 1;
        for (PhieuXuat px : listPX) {
            boolean matchDate = true;
            if (start != null && px.getNgayXuat().before(start))
                matchDate = false;
            if (end != null && px.getNgayXuat().after(end))
                matchDate = false;

            boolean matchNV = nv.equals("Tất cả") || px.getMaNV().equalsIgnoreCase(nv);

            boolean matchPrice = px.getTongTien() >= minPrice && px.getTongTien() <= maxPrice;

            if (matchDate && matchNV && matchPrice) {
                Object[] row = {
                        stt++,
                        px.getMaPhieuXuat(),
                        px.getNgayXuat() != null ? sdf.format(px.getNgayXuat()) : "N/A",
                        px.getMaNV(),
                        px.getMaKH(),
                        df.format(px.getTongTien())
                };
                tblModel.addRow(row);
            }
        }
    }

    public JTable getTable() {
        return tbl;
    }
}
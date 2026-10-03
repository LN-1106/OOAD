package Frontend.GUI.DoiTra;

import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Backend.BUS.DoiTraBUS;
import Backend.DTO.DoiTra;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class DoiTraTable extends JPanel {

    private JTable tbl;
    private DefaultTableModel tblModel;
    private DoiTraBUS doiTraBUS = new DoiTraBUS();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DoiTraTable() {
        setLayout(new MigLayout("wrap 1, fill, insets 10", "[grow]", "[]15[grow]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: 20");

        initHeader();
        initTable();
        loadData();
    }

    private void initHeader() {
        JPanel pnlHeader = new JPanel(new MigLayout("insets 10", "[]push[]"));
        pnlHeader.putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);
        
        JLabel lblTitle = new JLabel("Danh sách đổi trả");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnlHeader.add(lblTitle);
        add(pnlHeader, "growx");
    }

    private void initTable() {
        String[] header = {
            "Mã Đổi Trả", "Mã PX", "Khách Hàng", 
            "Ngày Mua", "Ngày Trả", "Hạn Cuối", 
            "Mã IMEI", "Lý Do"
        };

        tblModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbl = new Table();
        tbl.setModel(tblModel);

        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tbl.getSelectedRow();
                    if (row != -1) {
                        String maDT = tbl.getValueAt(row, 0).toString();
                        showDetail(maDT);
                    }
                }
            }
        });

        // Căn giữa dữ liệu trong các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(new JScrollPane(tbl), "grow");
    }

    /**
     * Tải dữ liệu từ BUS lên Table
     */
    public void loadData() {
        tblModel.setRowCount(0);
        ArrayList<DoiTra> list = doiTraBUS.getAll();
        for (DoiTra dt : list) {
            addRow(dt);
        }
    }

    /**
     * Hàm phụ để thêm một dòng vào bảng, xử lý định dạng ngày tháng
     */
    private void addRow(DoiTra dt) {
        // Lấy ngày đã được DTO tự động tính toán (Ngày hết hạn = Ngày mua + 30)
        String ngayMuaStr = (dt.getNgayMua() != null) ? dt.getNgayMua().format(dtf) : "N/A";
        String ngayTraStr = (dt.getNgayDoiTra() != null) ? dt.getNgayDoiTra().format(dtf) : "N/A";
        String hanCuoiStr = (dt.getNgayHetHan() != null) ? dt.getNgayHetHan().format(dtf) : "N/A";

        tblModel.addRow(new Object[]{
            dt.getMaDoiTra(),
            dt.getMaPhieuXuat(),
            dt.getTenKH(),
            ngayMuaStr,
            ngayTraStr,
            hanCuoiStr,
            dt.getMaImei(), // Cột IMEI mới thay cho cột Số lượng cũ
            dt.getLyDo()
        });
    }

    /**
     * Tìm kiếm và cập nhật lại bảng
     */
    public void updateTable(ArrayList<DoiTra> newList) {
        tblModel.setRowCount(0);
        for (DoiTra dt : newList) {
            addRow(dt);
        }
    }

    /**
     * Hiển thị Dialog chi tiết khi Double Click
     */
    private void showDetail(String maDT) {
        // Tìm đối tượng DoiTra trong list hiện tại
        DoiTra selected = null;
        for (DoiTra dt : doiTraBUS.getAll()) {
            if (dt.getMaDoiTra().equals(maDT)) {
                selected = dt;
                break;
            }
        }

        if (selected != null) {
            // Hiển thị Dialog chi tiết sản phẩm và quá trình nhập lại kho
            DoiTraDetailDialog detailDialog = new DoiTraDetailDialog(selected);
            detailDialog.setVisible(true);
        }
    }

    public JTable getTable() { return tbl; }
    
    public DefaultTableModel getModel() { return tblModel; }
}
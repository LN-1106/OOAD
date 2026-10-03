package Frontend.GUI.KhuyenMai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class KhuyenMaiPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public KhuyenMaiPanel() {
        setBackground(new Color(242, 244, 246));
        // Layout chính dồn lên top
        setLayout(new MigLayout("fillx, insets 20, gapy 20", "[fill]", "[]"));

        initComponents();
    }
    
    private void initComponents() {
        // --- 1. THANH CÔNG CỤ (SEARCH & FILTER) ---
        JPanel pnlToolBar = new JPanel(new MigLayout("insets 10, fillx", "[grow]10[]10[]10[]", "[]"));
        pnlToolBar.setBackground(Color.WHITE);
        pnlToolBar.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JTextField txtSearch = new JTextField(" Tìm kiếm thiết bị bảo hành (IMEI, Mã BH)..."); // Text mẫu giống ảnh
        txtSearch.setForeground(Color.GRAY);
        
        JButton btnAdd = createStyledButton("+ Thêm", new Color(46, 204, 113));
        JButton btnEdit = createStyledButton("Sửa", new Color(241, 196, 15));
        JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
        JButton btnExport = createStyledButton("Xuất Excel", new Color(149, 165, 166));

        pnlToolBar.add(txtSearch, "h 40!, growx");
        pnlToolBar.add(btnAdd, "h 40!, w 80!");
        pnlToolBar.add(btnEdit, "h 40!, w 80!");
        pnlToolBar.add(btnDelete, "h 40!, w 80!");
        pnlToolBar.add(btnExport, "h 40!, w 100!");

        // --- 2. BẢNG DỮ LIỆU (CARD VIEW) ---
        JPanel pnlTableCard = new JPanel(new MigLayout("fill, insets 15", "[fill]", "[]10[grow]"));
        pnlTableCard.setBackground(Color.WHITE);
        pnlTableCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JLabel lblTitle = new JLabel("Danh sách Khuyến mãi");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        String[] cols = {"STT", "Mã KM", "Tên Chương Trình", "Giá Trị", "Bắt Đầu", " Kết Thúc", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        styleTable();

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        pnlTableCard.add(lblTitle, "wrap");
        pnlTableCard.add(sp, "grow, h 600!"); // Fix chiều cao để không bị giãn quá đà

        // Add vào Panel chính
        add(pnlToolBar, "wrap");
        add(pnlTableCard);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable() {
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(232, 242, 252));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(245, 245, 245));
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 73, 94)); // Màu Header đậm giống các ảnh
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
    }
}

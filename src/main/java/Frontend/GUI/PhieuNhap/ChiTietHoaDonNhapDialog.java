package Frontend.GUI.PhieuNhap;

import Backend.BUS.ChiTietPhieuNhapBUS;
import Backend.DTO.ChiTietPhieuNhap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;

public class ChiTietHoaDonNhapDialog extends JDialog {
    private String maHD;
    private JTable tblDetails;
    private DefaultTableModel model;
    private JLabel lblImage;
    private JTextArea txtImeiList;
    private ChiTietPhieuNhapBUS ctBUS = new ChiTietPhieuNhapBUS();
    private ArrayList<ChiTietPhieuNhap> listChiTietPhieuNhap;

    public ChiTietHoaDonNhapDialog(JFrame parent, String maHD) {
        super(parent, "Chi tiết hóa đơn: " + maHD, true);
        this.maHD = maHD;
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        getRootPane().putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHeader.setBackground(Theme.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT HÓA ĐƠN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMain = new JPanel(new MigLayout("fill, insets 15", "[grow]15[300!]", "[grow]"));
        pnlMain.setBackground(Color.WHITE);

        String[] columns = { "STT", "Mã Phiên Bản", "Tên Sản Phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblDetails = new Table();
        tblDetails.setModel(model);

        JScrollPane scrollPane = new JScrollPane(tblDetails);
        pnlMain.add(scrollPane, "grow");

        JPanel pnlPreview = new JPanel(new MigLayout("wrap 1, fillx, insets 15", "[center]", "[]10[grow]10[]"));
        pnlPreview.setBackground(new Color(248, 249, 250));
        pnlPreview.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        lblImage = new JLabel();
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        lblImage.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblImeiTitle = new JLabel("Danh sách mã IMEI nhập về:");
        lblImeiTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtImeiList = new JTextArea(8, 20);
        txtImeiList.setEditable(false);
        txtImeiList.setLineWrap(true);
        txtImeiList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollImei = new JScrollPane(txtImeiList);

        pnlPreview.add(lblImage, "w 250!, h 200!");
        pnlPreview.add(lblImeiTitle, "left, gaptop 20");
        pnlPreview.add(scrollImei, "growx, h 180!");

        pnlMain.add(pnlPreview, "growy");
        add(pnlMain, BorderLayout.CENTER);

        // --- Sự kiện click bảng để đổi ảnh ---
        tblDetails.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblDetails.getSelectedRow();
                if (row != -1 && listChiTietPhieuNhap != null && row < listChiTietPhieuNhap.size()) {
                    ChiTietPhieuNhap ct = listChiTietPhieuNhap.get(row);
                    if (ct.getHinhAnh() != null && !ct.getHinhAnh().isEmpty()) {
                        loadProductImage(ct.getHinhAnh());
                    } else {
                        lblImage.setIcon(null);
                        lblImage.setText("Không có ảnh");
                    }
                }
                updatePreview();
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#, ###");
        listChiTietPhieuNhap = ctBUS.getByMaPhieu(maHD);

        int stt = 1;
        for(ChiTietPhieuNhap ct : listChiTietPhieuNhap) {
            model.addRow(new Object[] {
                stt++, 
                ct.getMaPhienBan(), 
                ct.getTenSP(),
                ct.getSoLuong(), 
                df.format(ct.getDonGia()), 
                df.format(ct.getThanhTien())
            });
        }
    }

    private void updatePreview() {
        int row = tblDetails.getSelectedRow();
        if (row != -1) {
            String maPhienBan = model.getValueAt(row, 1).toString();

            ArrayList<String> dsImei = ctBUS.getImeisByDetails(maHD, maPhienBan);

            StringBuilder sb = new StringBuilder();
            if(dsImei.isEmpty()) {
                sb.append("Không tìm thấy mã IMEI cho sản phẩm này.");
            } else {
                for (int i = 0; i < dsImei.size(); i++) {
                    sb.append(i + 1).append(". ").append(dsImei.get(i)).append("\n");
                }
            }
            txtImeiList.setText(sb.toString());
            txtImeiList.setCaretPosition(0);
        }
    }



    private void loadProductImage(String imgName) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource("images/product/" + imgName);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaled = icon.getImage().getScaledInstance(280, 250, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            lblImage.setText("Hiện tại ko thể tải ảnh");
        }

    }

}
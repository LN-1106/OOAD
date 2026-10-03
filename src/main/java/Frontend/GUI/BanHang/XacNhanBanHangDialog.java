package Frontend.GUI.BanHang;

import Frontend.Compoent.Theme;
import Frontend.Compoent.XuatPDFHoaDonXuat;
import Frontend.GUI.BaoHanh.BaoHanhTable;
import Frontend.GUI.PhieuXuat.PhieuXuatTable;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Backend.BUS.PhieuXuatBUS;
import Backend.DTO.PhieuXuat;
import Backend.DTO.ChiTietPhieuXuat;
import Backend.DTO.KhuyenMai;
import Backend.DTO.Session; 
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.sql.Timestamp;

public class XacNhanBanHangDialog extends JDialog {
    private JTable tblReview;
    private DefaultTableModel modelReview;
    private JLabel lblTongSL, lblTongTien;
    private String maKH;
    private PhieuXuatTable phieuXuatTable;
    private BaoHanhTable baoHanhTable;
    private BanHangSidebar sidebar;
    private String maKMSelected = null;
    private double phanTramGiam = 0;
    private double finalTotalValue = 0; 

    public XacNhanBanHangDialog(JFrame parent, DefaultTableModel sourceModel, String maKH,
                                PhieuXuatTable phieuXuatTable, BaoHanhTable baoHanhTable, BanHangSidebar sidebar) {
        super(parent, "Xác nhận hóa đơn bán hàng", true);
        this.maKH = maKH;
        this.phieuXuatTable = phieuXuatTable;
        this.baoHanhTable = baoHanhTable;
        this.sidebar = sidebar;
        
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow][]"));
        setSize(800, 550);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);

        initComponents(sourceModel);
        calculateTotal();
    }

    private void initComponents(DefaultTableModel sourceModel) {
        JLabel lblTitle = new JLabel("KIỂM TRA THÔNG TIN HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        add(lblTitle, "center, wrap, gapbottom 15");

        String[] cols = { "STT", "Mã SP", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền" };
        modelReview = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tblReview = new Table();
        tblReview.setModel(modelReview);
        tblReview.setRowSelectionAllowed(true);

        tblReview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = tblReview.getSelectedRow();
                if(e.getClickCount() == 2 && selected != -1) {
                    modelReview.removeRow(selected);
                    for(int i = 0; i < modelReview.getRowCount(); i++) {
                        modelReview.setValueAt(i + 1, i, 0);
                    } 
                    calculateTotal();
                }
            }
        });

        tblReview.getColumnModel().getColumn(0).setMaxWidth(50);
        tblReview.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblReview.getColumnModel().getColumn(3).setMaxWidth(60);
        tblReview.getColumnModel().getColumn(2).setPreferredWidth(250);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblReview.setDefaultRenderer(Object.class, centerRenderer);

        for (int i = 0; i < sourceModel.getRowCount(); i++) {
            String ma = sourceModel.getValueAt(i, 0).toString();
            String ten = sourceModel.getValueAt(i, 1).toString();
            int sl = Integer.parseInt(sourceModel.getValueAt(i, 2).toString());
            String giaStr = sourceModel.getValueAt(i, 3).toString().replaceAll("[^0-9]", "");
            double gia = Double.parseDouble(giaStr);
            double thanhTien = sl * gia;

            modelReview.addRow(new Object[] {
                    (i + 1), ma, ten, sl,
                    String.format("%,.0f", gia),
                    String.format("%,.0f", thanhTien)
            });
        }

        add(new JScrollPane(tblReview), "grow, wrap");

        JPanel pnlTotal = new JPanel(new MigLayout("fillx", "[grow][right]"));
        pnlTotal.setBackground(new Color(245, 245, 245));
        pnlTotal.putClientProperty("FlatLaf.style", "arc: 15");

        lblTongSL = new JLabel("Tổng số lượng: 0");
        lblTongSL.setFont(new Font("Segoe UI", Font.BOLD, 15));

        lblTongTien = new JLabel("TỔNG THANH TOÁN: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTongTien.setForeground(Color.RED);

        pnlTotal.add(lblTongSL);
        pnlTotal.add(lblTongTien);
        add(pnlTotal, "growx, wrap, gaptop 10");

        JPanel pnlButtons = new JPanel(new MigLayout("fillx, insets 5 0 0 0", "[left][grow][right]"));
        pnlButtons.setBackground(Color.WHITE);

        CustomButton btnKhuyenMai = new CustomButton("CHỌN KHUYẾN MÃI", Theme.ACCENT_COLOR);
        btnKhuyenMai.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ChonKhuyenMaiDialog dialog = new ChonKhuyenMaiDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.getSelectedKM() != null) {
                KhuyenMai km = dialog.getSelectedKM();
                this.maKMSelected = km.getMaKM();
                this.phanTramGiam = km.getPhanTramGiam();
                btnKhuyenMai.setText("KM: " + km.getTenKM() + " (-" + phanTramGiam + "%)");
                calculateTotal();
            }
        });

        CustomButton btnHuy = new CustomButton("HỦY BỎ", new Color(149, 165, 166));
        btnHuy.addActionListener(e -> dispose());

        CustomButton btnXacNhan = new CustomButton("XÁC NHẬN & XUẤT HÓA ĐƠN", Theme.PRIMARY_COLOR);
        btnXacNhan.addActionListener(e -> {
            if (modelReview.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Hóa đơn không có sản phẩm!");
                return;
            }

            try {
                PhieuXuatBUS pxBUS = new PhieuXuatBUS();
                String maPX = pxBUS.getNewMaPhieu();
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                String maNV = "NV01"; 
                if (Session.currentAccount != null) {
                    maNV = Session.currentAccount.getMaNV();
                }

                PhieuXuat phieuXuat = new PhieuXuat(maPX, currentTime, maNV, maKH, maKMSelected, finalTotalValue, true);

                ArrayList<ChiTietPhieuXuat> dsChiTiet = new ArrayList<>();
                for (int i = 0; i < modelReview.getRowCount(); i++) {
                    String maPB = modelReview.getValueAt(i, 1).toString();
                    int sl = Integer.parseInt(modelReview.getValueAt(i, 3).toString());
                    String giaStr = modelReview.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                    double gia = Double.parseDouble(giaStr);

                    dsChiTiet.add(new ChiTietPhieuXuat(maPX, maPB, sl, gia, 0.0));
                }

                if (pxBUS.thanhToan(phieuXuat, dsChiTiet)) {
                    int choice = JOptionPane.showConfirmDialog(this, 
                        "Bán hàng thành công! Bạn có muốn xuất hóa đơn PDF không?",
                        "Thành công", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        XuatPDFHoaDonXuat.xuatHoaDonNhap(phieuXuat, modelReview);
                    }

                    if (this.phieuXuatTable != null) this.phieuXuatTable.loadData("");
                    if (this.baoHanhTable != null) this.baoHanhTable.loadData();
                    if (this.sidebar != null) this.sidebar.clearCart();
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Kiểm tra lại tồn kho.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage());
            }
        });

        pnlButtons.add(btnKhuyenMai);
        pnlButtons.add(new JLabel(), "growx");
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnXacNhan);
        add(pnlButtons, "right, span");
    }

    private void calculateTotal() {
        int totalSL = 0;
        double subTotal = 0;

        for (int i = 0; i < modelReview.getRowCount(); i++) {
            int sl = Integer.parseInt(modelReview.getValueAt(i, 3).toString());
            String giaStr = modelReview.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
            double gia = Double.parseDouble(giaStr);
            
            totalSL += sl;
            subTotal += (sl * gia);
        }

        double moneyDiscount = subTotal * (this.phanTramGiam / 100.0);
        this.finalTotalValue = subTotal - moneyDiscount;

        lblTongSL.setText("Tổng số lượng: " + totalSL);
        lblTongTien.setText("TỔNG THANH TOÁN: " + String.format("%,.0f", finalTotalValue) + " VNĐ");
    }
}

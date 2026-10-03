package Frontend.GUI.Nhaphang;

import Frontend.Compoent.Theme;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Backend.BUS.PhieuNhapBUS;
import Backend.DTO.ChiTietPhieuNhap;
import Backend.DTO.PhieuNhap;
import Backend.BUS.ChiTietPhieuNhapBUS;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

import Frontend.Compoent.XuatPDFHoaDonNhap;

public class XacNhanNhapHangDialog extends JDialog {
    private JTable tblReview;
    private DefaultTableModel modelReview;
    private JLabel lblTongSL, lblTongTien;
    private NhapHangTable table;
    private NhapHangSidebar sidebar;

    public XacNhanNhapHangDialog(JFrame parent, DefaultTableModel sourceModel, NhapHangTable table, NhapHangSidebar sidebar) {
        super(parent, "Xác nhận phiếu nhập hàng", true);
        this.table = table;
        this.sidebar = sidebar;
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow][]"));
        setSize(800, 500);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);

        initComponents(sourceModel);
        calculateTotal();
    }

    private void initComponents(DefaultTableModel sourceModel) {
        JLabel lblTitle = new JLabel("KIỂM TRA THÔNG TIN NHẬP HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        add(lblTitle, "center, wrap, gapbottom 15");

        String[] cols = { "STT", "Mã Phiên Bản", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền" };
        modelReview = new DefaultTableModel(cols, 0);

        modelReview = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chặn chỉnh sửa ô, nhưng vẫn cho phép chọn dòng
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


        tblReview.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblReview.getColumnModel().getColumn(0).setMaxWidth(50);

        tblReview.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblReview.getColumnModel().getColumn(1).setMaxWidth(100);

        tblReview.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblReview.getColumnModel().getColumn(3).setMaxWidth(60);

        tblReview.getColumnModel().getColumn(2).setPreferredWidth(250);

        tblReview.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblReview.getColumnModel().getColumn(5).setPreferredWidth(120);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblReview.setDefaultRenderer(Object.class, centerRenderer);

        for (int i = 0; i < sourceModel.getRowCount(); i++) {
            // Cột 0: Mã, Cột 1: Tên, Cột 2: SL, Cột 3: Giá
            String ma = sourceModel.getValueAt(i, 0).toString();
            String ten = sourceModel.getValueAt(i, 1).toString();
            int sl = Integer.parseInt(sourceModel.getValueAt(i, 2).toString());
            String giaRaw = sourceModel.getValueAt(i, 3).toString().replaceAll("[^0-9]", "");
            double gia = Double.parseDouble(giaRaw);
            double thanhTien = sl * gia;

            modelReview.addRow(new Object[] {
                    (i + 1),
                    ma,
                    ten,
                    sl,
                    String.format("%,.0f", gia),
                    String.format("%,.0f", thanhTien)
            });
        }

        add(new JScrollPane(tblReview), "grow, wrap");

        JPanel pnlTotal = new JPanel(new MigLayout("fillx", "[grow][right]"));
        pnlTotal.setBackground(new Color(245, 245, 245));
        pnlTotal.putClientProperty("FlatLaf.style", "arc: 15");

        lblTongSL = new JLabel("Tổng số lượng: 0");
        lblTongSL.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongSL.setForeground(Color.RED);

        lblTongTien = new JLabel("Tổng tiền thanh toán: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);

        pnlTotal.add(lblTongSL);
        pnlTotal.add(lblTongTien);
        add(pnlTotal, "growx, wrap, gaptop 10");

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(Color.WHITE);

        CustomButton btnHuy = new CustomButton("HỦY BỎ", new Color(149, 165, 166));
        btnHuy.addActionListener(e -> dispose());

        CustomButton btnXacNhan = new CustomButton("XÁC NHẬN & XUẤT HÓA ĐƠN", Theme.ACCENT_COLOR);
        btnXacNhan.addActionListener(e -> {
            try {
                PhieuNhapBUS pnBUS = new PhieuNhapBUS();
                ChiTietPhieuNhapBUS ctpnBUS = new ChiTietPhieuNhapBUS();

                String maPN = pnBUS.getNewMaPhieu(); // Lấy mã phiếu nhập mới tự động

                String maNVThucTe = "NV001";
                if (Backend.DTO.Session.currentNhanVien != null) {
                    maNVThucTe = Backend.DTO.Session.currentNhanVien.getId();
                } else if (Backend.DTO.Session.currentAccount != null) {
                    maNVThucTe = Backend.DTO.Session.currentAccount.getMaNV();
                }

                String maNCCThucTe = "NCC001";
                if (this.sidebar != null) {
                    maNCCThucTe = this.sidebar.getSelectedMaNCC(); 
                }

                PhieuNhap phieuNhap = new PhieuNhap(maPN, null, maNVThucTe, maNCCThucTe, 0.0, true);

                // Chuyển dữ liệu từ bảng Review sang danh sách ChiTietPhieuNhap
                ArrayList<ChiTietPhieuNhap> dsChiTiet = new ArrayList<>();
                for (int i = 0; i < modelReview.getRowCount(); i++) {
                    String maPB = modelReview.getValueAt(i, 1).toString();
                    int sl = Integer.parseInt(modelReview.getValueAt(i, 3).toString());
                    String giaStr = modelReview.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                    double gia = Double.parseDouble(giaStr);

                    // Trigger trg_CalcThanhTienNhap sẽ tính ThanhTien
                    dsChiTiet.add(new ChiTietPhieuNhap(maPN, maPB, sl, gia, 0.0));
                }

                if (pnBUS.thanhToan(phieuNhap, dsChiTiet)) {
                    int choice = JOptionPane.showConfirmDialog(this, 
                    "Nhập hàng thành công! Bạn có muốn xuất hóa đơn PDF không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        XuatPDFHoaDonNhap.xuatHoaDonNhap(phieuNhap, modelReview);
                    }

                    if(table != null) {
                        table.loadData();
                    }

                    if(this.sidebar != null) {
                        this.sidebar.clearCart();
                    }

                    dispose();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage());
            }
        });

        pnlButtons.add(btnHuy);
        pnlButtons.add(btnXacNhan);

        add(pnlButtons, "right");
    }

    private void calculateTotal() {
        int totalSL = 0;
        double totalMoney = 0;
        for (int i = 0; i < modelReview.getRowCount(); i++) {
            totalSL += Integer.parseInt(modelReview.getValueAt(i, 3).toString());
            String thanhTienStr = modelReview.getValueAt(i, 5).toString().replaceAll("[^0-9]", "");
            totalMoney += Double.parseDouble(thanhTienStr);
        }
        lblTongSL.setText("Tổng số lượng: " + totalSL);
        lblTongTien.setText("TỔNG TIỀN THANH TOÁN: " + String.format("%,.0f", totalMoney) + " VNĐ");
    }
}
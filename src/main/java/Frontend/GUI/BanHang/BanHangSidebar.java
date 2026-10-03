package Frontend.GUI.BanHang;

import Frontend.Compoent.Theme;
import Frontend.GUI.BaoHanh.BaoHanhTable;
import Frontend.GUI.PhieuXuat.PhieuXuatTable;
import Frontend.Compoent.CustomButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import Backend.DTO.Session; 

public class BanHangSidebar extends JPanel {

    private JLabel lblTenSP, lblMaSP, lblGia, lblTonKho;
    private JSpinner spnSoLuongBan;
    private JTable tblBan;
    private DefaultTableModel modelBan;
    private JTextField txtNhanVien; 
    private PhieuXuatTable phieuXuatTable;
    private BaoHanhTable baoHanhTable;

    private String currentMa = "";
    private String currentGia = "";
    private JLabel lblKhachHang;
    private String maKHSelected = "";
    private String currentMauSac = "";

    public BanHangSidebar(PhieuXuatTable phieuXuatTable, BaoHanhTable baoHanhTable) {
        this.phieuXuatTable = phieuXuatTable;
        this.baoHanhTable = baoHanhTable;
        setLayout(new MigLayout("wrap 1, fillx, insets 20", "[fill]"));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(280, 0));
        putClientProperty("FlatLaf.style", "arc: 15");

        initHeader();
        add(new JSeparator(), "growx, gaptop 5, gapbottom 5");
        initProductSelection();
        initMiniTable();
        initConfirmButton();
    }

    private void initHeader() {
        add(new JLabel("Nhân viên bán"), "gaptop 5");

        String tenHienThi = "Chưa đăng nhập";
        if (Session.currentNhanVien != null) {
            tenHienThi = Session.currentNhanVien.getHoTen();
        } else if (Session.currentAccount != null) {
            tenHienThi = Session.currentAccount.getUsername();
        }

        txtNhanVien = new JTextField(tenHienThi);
        txtNhanVien.setEditable(false);
        txtNhanVien.setFocusable(false);
        txtNhanVien.setBackground(new Color(245, 245, 245));
        add(txtNhanVien, "h 30!");

        add(new JLabel("Khách hàng"), "gaptop 10");
        lblKhachHang = new JLabel("Chưa chọn khách hàng");
        lblKhachHang.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblKhachHang.setForeground(Color.GRAY);
        add(lblKhachHang);

        CustomButton btnChonKH = new CustomButton("Chọn khách hàng", new Color(127, 140, 141));
        btnChonKH.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            ChonKhachHangDialog dialog = new ChonKhachHangDialog(parent);
            dialog.setVisible(true);

            if (dialog.getSelectedKH() != null) {
                lblKhachHang.setText(dialog.getSelectedKH().tenKH);
                lblKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblKhachHang.setForeground(Theme.PRIMARY_COLOR);
                this.maKHSelected = dialog.getSelectedKH().maKH;
            }
        });
        add(btnChonKH, "h 30!");
    }

    private void initProductSelection() {
        lblTenSP = new JLabel("Chọn sản phẩm");
        lblTenSP.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTenSP.setForeground(Theme.PRIMARY_COLOR);

        lblTonKho = new JLabel("Tồn kho hiện tại: 0");

        add(lblTenSP);
        add(lblTonKho);
        add(new JSeparator(), "growx, gaptop 10, gapbottom 10");

        add(new JLabel("Số lượng bán:"), "gaptop 5");

        spnSoLuongBan = new JSpinner(new SpinnerNumberModel(1, -10000, Integer.MAX_VALUE, 1));

        add(spnSoLuongBan, "split 2, w 100!, h 35!");

        CustomButton btnThem = new CustomButton("THÊM", new Color(0, 153, 255));
        btnThem.addActionListener(e -> addProductToTable());
        add(btnThem, "growx, h 35!");
    }

    // private void validateSpinnerInput(JTextField spinnerTxt) {
    // if (currentMa.isEmpty())
    // return;

    // String cleanTon = lblTonKho.getText().replaceAll("[^0-9]", "");
    // if (cleanTon.isEmpty())
    // return;
    // int tonKho = Integer.parseInt(cleanTon);

    // try {
    // int nhap = Integer.parseInt(spinnerTxt.getText().trim());
    // if (nhap > tonKho) {
    // JOptionPane.showMessageDialog(
    // BanHangSidebar.this,
    // "Số lượng bán vượt quá tồn kho! Tồn kho hiện tại: " + tonKho);
    // spnSoLuongBan.setValue(tonKho);
    // }
    // } catch (NumberFormatException ex) {
    // spnSoLuongBan.setValue(1);
    // }
    // }

    private void initMiniTable() {
        add(new JLabel("Danh sách chờ bán:"), "gaptop 10");
        String[] cols = { "Mã SP", "Tên SP", "SL", "Đơn giá" };

        modelBan = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBan = new JTable(modelBan);
        tblBan.setRowHeight(30);
        tblBan.setRowSelectionAllowed(true);

        tblBan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = tblBan.getSelectedRow();
                if (e.getClickCount() == 2 && selected != -1) {
                    modelBan.removeRow(selected);
                }
            }
        });

        tblBan.getColumnModel().getColumn(1).setMinWidth(0);
        tblBan.getColumnModel().getColumn(1).setMaxWidth(0);
        tblBan.getColumnModel().getColumn(3).setMinWidth(0);
        tblBan.getColumnModel().getColumn(3).setMaxWidth(0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblBan.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scroll = new JScrollPane(tblBan);
        add(scroll, "h 110!, gaptop 5");
    }

    private void initConfirmButton() {
        CustomButton btnXacNhan = new CustomButton("XÁC NHẬN BÁN", Theme.ACCENT_COLOR);
        btnXacNhan.addActionListener(e -> {
            if (modelBan.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Danh sách chờ bán đang trống!");
                return;
            }

            if (maKHSelected == null || maKHSelected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng trước khi xác nhận bán!",
                        "Yêu cầu chọn khách hàng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            XacNhanBanHangDialog dialog = new XacNhanBanHangDialog(
                    parent,
                    modelBan,
                    maKHSelected,
                    this.phieuXuatTable,
                    this.baoHanhTable,
                    this);
            dialog.setVisible(true);
        });
        add(btnXacNhan, "gaptop 5, growx, h 40!, pushy, aligny bottom");
    }

    private void addProductToTable() {
        if (currentMa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trước!");
            return;
        }

        try {
            spnSoLuongBan.commitEdit();
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            return;
        }

        String cleanTon = lblTonKho.getText().replaceAll("[^0-9]", "");
        int tonKho = Integer.parseInt(cleanTon);

        if (tonKho <= 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm này đã hết hàng!", "Hết hàng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int slBanMoi = (int) spnSoLuongBan.getValue();

        if (slBanMoi <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng bán phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            spnSoLuongBan.setValue(1);
            return;
        }

        if (slBanMoi > tonKho) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng bán vượt quá tồn kho! Tồn kho hiện tại: " + tonKho,
                    "Không đủ hàng",
                    JOptionPane.ERROR_MESSAGE);
            return; 
        }

        boolean found = false;
        String ten = lblTenSP.getText();

        for (int i = 0; i < modelBan.getRowCount(); i++) {
            String maTrongBang = modelBan.getValueAt(i, 0).toString();
            if (maTrongBang.equals(currentMa)) {
                int slCu = (int) modelBan.getValueAt(i, 2);

                if (slCu + slBanMoi > tonKho) {
                    JOptionPane.showMessageDialog(this,
                            "Bạn đã có " + slCu + " món trong giỏ. Vượt quá tồn kho (" + tonKho + ")!",
                            "Không đủ hàng",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                modelBan.setValueAt(slCu + slBanMoi, i, 2);
                found = true;
                break;
            }
        }

        if (!found) {
            modelBan.addRow(new Object[] { currentMa, ten, slBanMoi, currentGia });
        }

        resetSelection();
    }

    private void resetSelection() {
        this.currentMa = "";
        this.currentGia = "";
        lblTenSP.setText("Chưa chọn sản phẩm");
        lblTonKho.setText("Tồn kho hiện tại: 0");
        spnSoLuongBan.setValue(1);
    }

    public void clearCart() {
        modelBan.setRowCount(0);
        maKHSelected = "";
        lblKhachHang.setText("Chưa chọn khách hàng");
        lblKhachHang.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblKhachHang.setForeground(Color.GRAY);
        resetSelection();
    }

    public void updateInfo(String ma, String ten, String gia, String ton) {
        this.currentMa = ma;
        this.currentGia = gia;
        lblTenSP.setText(ten);

        try {
            String cleanTon = ton.replaceAll("[^0-9]", "");
            int tonInt = Integer.parseInt(cleanTon);

            lblTonKho.setText("Tồn kho hiện tại: " + tonInt);
            lblTonKho.setForeground(tonInt > 0 ? Color.BLACK : Color.RED);

            if (tonInt > 0) {
                spnSoLuongBan.setModel(new SpinnerNumberModel(1, -10000, Integer.MAX_VALUE, 1));
                spnSoLuongBan.setEnabled(true);
            } else {
                spnSoLuongBan.setModel(new SpinnerNumberModel(0, 0, 0, 0));
                spnSoLuongBan.setEnabled(false);
            }
        } catch (Exception e) {
            lblTonKho.setText("Tồn kho hiện tại: 0");
            spnSoLuongBan.setEnabled(false);
        }

        this.repaint();
    }
}

package Frontend.GUI.Nhaphang;

import Frontend.Compoent.Theme; 
import Frontend.Compoent.CustomButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Backend.BUS.NhaCungCapBUS;
import Backend.BUS.TaiKhoanBUS;
import Backend.DTO.TaiKhoan;
import Backend.DTO.NhaCungCap;
import Backend.DTO.Session;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NhapHangSidebar extends JPanel {
    private JLabel lblTenSP, lblMaSP, lblGia, lblTonKho;
    private JSpinner spnSoLuongNhap;
    private JTable tblNhap;
    private DefaultTableModel modelNhap;
    private JTextField txtNhanVien;
    private JComboBox<NhaCungCap> cbxNhaCungCap;

    private String currenMa = "";
    private String currenTen = "";
    private String currenGia = "";

    private NhapHangTable table;

    public NhapHangSidebar(NhapHangTable table) {
        this.table = table;
        setLayout(new MigLayout("wrap 1, fillx, insets 20", "[fill]"));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(280, 0));
        putClientProperty("FlatLaf.style", "arc: 15");

        initHeader();
        initProductSelection();
        initMiniTable();
        initConfirmButton();
    }

    private void initHeader() {
        add(new JLabel("Nhân viên nhập:"), "gaptop 5");
        
        String hoTenNV = "Chưa xác định";
        if (Session.currentNhanVien != null) {
            hoTenNV = Session.currentNhanVien.getHoTen();
        } else if (Session.currentAccount != null) {
            hoTenNV = Session.currentAccount.getUsername();
        }

        txtNhanVien = new JTextField(hoTenNV);
        txtNhanVien.setEditable(false);
        txtNhanVien.setFocusable(false);
        txtNhanVien.setBackground(new Color(245, 245, 245));
        add(txtNhanVien, "h 30!");

        add(new JLabel("Nhà cung cấp:"), "gaptop 10");
        cbxNhaCungCap = new JComboBox<>();

        cbxNhaCungCap.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhaCungCap) {
                    NhaCungCap ncc = (NhaCungCap) value;
                    if (ncc.getMaNCC().equals("All")) {
                        setText(ncc.getTenNCC());
                    } else {
                        setText(ncc.getTenNCC() + " (" + ncc.getMaNCC() + ")");
                    }
                }
                return this;
            }
        });
        
        NhaCungCapBUS nccBUS = new NhaCungCapBUS();
        ArrayList<NhaCungCap> list = nccBUS.getAllNhaCungCap();
        DefaultComboBoxModel<NhaCungCap> model = new DefaultComboBoxModel<>();
        model.addElement(new NhaCungCap("All", "Tất cả nhà cung cấp", "", ""));

        for(NhaCungCap ncc : list) {
            model.addElement(ncc);
        }
        cbxNhaCungCap.setModel(model);

        cbxNhaCungCap.addActionListener(e -> {
            NhaCungCap selected = (NhaCungCap) cbxNhaCungCap.getSelectedItem();
            if(selected != null) {
                table.loadDataByNCC(selected.getMaNCC());
            }
        });

        add(cbxNhaCungCap, "h 30!");
        add(new JSeparator(), "gaptop 10, gapbottom 5");
    }


    private void initProductSelection() {
        lblTenSP = new JLabel("Chọn sản phẩm");
        lblTenSP.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTenSP.setForeground(Theme.PRIMARY_COLOR);

        lblTonKho = new JLabel("Tồn kho hiện tại: 0");

        add(lblTenSP);
        add(lblTonKho);
        add(new JSeparator(), "growx, gaptop 5, gapbottom 5");

        add(new JLabel("Số lượng nhập:"), "gaptop 5");
        spnSoLuongNhap = new JSpinner(new SpinnerNumberModel(1, -10000, 10000, 1));


        add(spnSoLuongNhap, "split 2, w 120!, h 30!");

        CustomButton btnThem = new CustomButton("THÊM", new Color(52, 152, 219));
        btnThem.addActionListener(e -> addProductToTable());
        add(btnThem, "growx, h 30!");
    }

    private void initMiniTable() {
        add(new JLabel("Danh sách chờ nhập:"), "gaptop 15");
        String[] cols = {"Mã SP", "Tên SP", "SL", "Đơn giá"};
        
        modelNhap = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNhap = new JTable(modelNhap);
        tblNhap.setRowHeight(25);
        tblNhap.setRowSelectionAllowed(true);

        tblNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected = tblNhap.getSelectedRow();
                if(e.getClickCount() == 2 && selected != -1) {
                    modelNhap.removeRow(selected);
                }
            }
        });

        tblNhap.getColumnModel().getColumn(1).setMinWidth(0);
        tblNhap.getColumnModel().getColumn(1).setMaxWidth(0);
        tblNhap.getColumnModel().getColumn(3).setMinWidth(0);
        tblNhap.getColumnModel().getColumn(3).setMaxWidth(0);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblNhap.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scroll = new JScrollPane(tblNhap);
        add(scroll, "h 140!, gaptop 5");
    }

    private void initConfirmButton() {
        CustomButton btnXacNhan = new CustomButton("XÁC NHẬN NHẬP", Theme.ACCENT_COLOR);
        btnXacNhan.addActionListener(e -> {
            if (modelNhap.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Danh sách chờ nhập đang trống!");
                return;
            }
            
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            XacNhanNhapHangDialog dialog = new XacNhanNhapHangDialog(parent, modelNhap, this.table, this);
            dialog.setVisible(true);
        });
        add(btnXacNhan, "gaptop 5, growx, h 40!, pushy, aligny bottom");
    }

    private void addProductToTable() {
        if(currenMa == null || currenMa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ bảng bên phải!");
            return;
        }

        try {
            spnSoLuongNhap.commitEdit();
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            return;
        }

        int slNhapMoi = (int) spnSoLuongNhap.getValue();

        if (slNhapMoi <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            spnSoLuongNhap.setValue(1);
            return;
        }

        boolean found = false;   
        for (int i = 0; i < modelNhap.getRowCount(); i++) {
            String maTrongBang = modelNhap.getValueAt(i, 0).toString();
            if(maTrongBang.equals(currenMa)) {
                int slCu = (int) modelNhap.getValueAt(i, 2);
                modelNhap.setValueAt(slCu + slNhapMoi, i, 2);
                found = true;
                break;
            }
        }

        if (!found) {
            modelNhap.addRow(new Object[]{currenMa, currenTen, slNhapMoi, currenGia});
        }

        // int sl = (int) spnSoLuongNhap.getValue();
        // modelNhap.addRow(new Object[]{currenMa, currenTen, sl, currenGia});
        resetSelection();
    }

    private void resetSelection() {
        currenMa = "";
        currenTen = "";
        currenGia = "";
        lblTenSP.setText("Chưa chọn sản phẩm");
        lblTonKho.setText("Tồn kho hiện tại: 0");
        spnSoLuongNhap.setValue(1);
    }

    public void clearCart() {
        modelNhap.setRowCount(0);
        resetSelection();
    }

    public void updateInfo(String ma, String ten, String gia, String ton) {
        this.currenMa = ma;
        this.currenTen = ten;
        this.currenGia = gia;
        lblTenSP.setText(ten);
        lblTonKho.setText("Tồn kho hiện tại: " + ton);
    }

    public String getSelectedMaNCC() {
        if (cbxNhaCungCap != null && cbxNhaCungCap.getSelectedItem() !=null) {
            NhaCungCap selected = (NhaCungCap) cbxNhaCungCap.getSelectedItem();
            if (!selected.getMaNCC().equals("All")) {
                return selected.getMaNCC();
            }
        }
        return "NCC001";
    }

    public void nhapTuExcel(ArrayList<Object[]> dataExcel) {
        if (dataExcel == null || dataExcel.isEmpty()) return;

        int soDongThemThanhCong = 0;

        for (Object[] rowData : dataExcel) {
            String maExcel = rowData[0].toString();
            String tenExcel = rowData[1].toString();
            int slExcel = (int) rowData[2];
            double giaExcel = (double) rowData[3];

            boolean daCoTrongBang = false;

            for (int i = 0; i < modelNhap.getRowCount(); i++) {
                String maTrongBang = modelNhap.getValueAt(i, 0).toString();

                if (maTrongBang.equals(maExcel)) {
                    int slCu = (int) modelNhap.getValueAt(i, 2);
                    modelNhap.setValueAt(slCu + slExcel, i, 2);
                    
                    modelNhap.setValueAt(String.format("%,.0f", giaExcel), i, 3);
                    daCoTrongBang = true;
                    soDongThemThanhCong++;
                    break;
                }
            }

            if (!daCoTrongBang) {
                String giaFormatted = String.format("%,.0f", giaExcel);
                modelNhap.addRow(new Object[]{maExcel, tenExcel, slExcel, giaFormatted});
                soDongThemThanhCong++;
            }
        }
        
        if (soDongThemThanhCong > 0) {
            JOptionPane.showMessageDialog(this, "Đã tải thành công " + soDongThemThanhCong + " sản phẩm từ Excel vào danh sách chờ!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

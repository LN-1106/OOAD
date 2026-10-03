package Frontend.GUI.PhanQuyen;

import Backend.BUS.NhomQuyenBUS;
import Backend.BUS.ChucNangBUS;
import Backend.DTO.NhomQuyen;
import Backend.DTO.ChucNang;
import Backend.DTO.ChiTietQuyen;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class NhomQuyenFixDialog extends JDialog {
    private JTextField txtTen;
    private JTable tblQuyen;
    private DefaultTableModel model;
    private String maNhomQuyen;
    private NhomQuyenBUS nqBUS = new NhomQuyenBUS();
    private ChucNangBUS cnBUS = new ChucNangBUS();
    private ArrayList<ChucNang> dsChucNang;

    public NhomQuyenFixDialog(String ma, String ten, String moTa) {
        setTitle("Chỉnh sửa nhóm quyền: " + ma);
        setModal(true);
        setSize(500, 600); // Đổi size cho vừa với 2 cột giống form Gán quyền
        setLocationRelativeTo(null);
        
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[]20[]10[grow]20[]"));
        getContentPane().setBackground(Color.WHITE);
        this.maNhomQuyen = ma;

        add(new JLabel("Tên nhóm quyền"), "split 2, w 120!");
        txtTen = new JTextField(ten);
        txtTen.putClientProperty("FlatLaf.style", "arc: 10");
        // Ở form này thì được phép sửa tên Nhóm quyền
        add(txtTen, "growx, h 35!, wrap");

        JLabel lblTitle = new JLabel("Danh mục chức năng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(lblTitle, "wrap");

        // BƯỚC 1: Cấu hình lại Model chỉ còn 2 cột
        String[] cols = { "Danh mục chức năng", "Truy cập" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? String.class : Boolean.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 1; // Chỉ cho phép sửa cột Checkbox "Truy cập"
            }
        };

        tblQuyen = new Table();
        tblQuyen.setModel(model);
        tblQuyen.setRowHeight(40);
        loadData(); 

        add(new JScrollPane(tblQuyen), "grow, wrap");

        CustomButton btnUpdate = new CustomButton("LƯU THAY ĐỔI", Theme.PRIMARY_COLOR);
        CustomButton btnCancel = new CustomButton("Huỷ bỏ", new Color(217, 83, 79));

        btnCancel.addActionListener(e -> dispose());
        btnUpdate.addActionListener(e -> handleUpdate());

        add(btnCancel, "split 2, center, w 150!, h 40!");
        add(btnUpdate, "w 150!, h 40!");
    }

    private void loadData() {
        dsChucNang = cnBUS.getAll();
        ArrayList<ChiTietQuyen> listQuyenHienTai = nqBUS.getQuyenCuaNhom(maNhomQuyen);

        for (ChucNang cn : dsChucNang) {
            String maCN = cn.getMaChucNang();
            
            // BƯỚC 2: Chỉ check hành động "read" để quyết định tick hay không tick
            model.addRow(new Object[] {
                "Quản lý " + cn.getTenChucNang().toLowerCase(),
                checkInList(listQuyenHienTai, maCN, "read")
            });
        }
    }

    private boolean checkInList(ArrayList<ChiTietQuyen> list, String maCN, String action) {
        for (ChiTietQuyen q : list) {
            if (q.getMaChucNang().equalsIgnoreCase(maCN) && q.getHanhDong().equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;
    }

    private void handleUpdate() {
        String tenMoi = txtTen.getText().trim();
        if (tenMoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhóm quyền không được để trống!");
            return;
        }

        ArrayList<ChiTietQuyen> dsQuyenMoi = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String maCN = dsChucNang.get(i).getMaChucNang();
            
            // BƯỚC 3: Chỉ kiểm tra cột 1 (Truy cập)
            boolean isGranted = (Boolean) model.getValueAt(i, 1);
            
            if (isGranted) {
                 // Nếu có tick thì lưu quyền "read"
                 dsQuyenMoi.add(new ChiTietQuyen(maNhomQuyen, maCN, "read"));
            }
        }

        // Tạo đối tượng NhomQuyen để update tên mới (nếu có đổi)
        NhomQuyen nq = new NhomQuyen(maNhomQuyen, tenMoi, "");
        
        if (nqBUS.updateRoleWithPermissions(nq, dsQuyenMoi)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
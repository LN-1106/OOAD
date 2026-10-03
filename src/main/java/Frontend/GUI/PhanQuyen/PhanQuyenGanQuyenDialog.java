package Frontend.GUI.PhanQuyen;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import Backend.BUS.NhomQuyenBUS;
import Backend.BUS.ChucNangBUS;
import Backend.DTO.ChucNang;
import Backend.DTO.ChiTietQuyen;
import Frontend.Compoent.Table;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;

public class PhanQuyenGanQuyenDialog extends JDialog {
    private JTable tblQuyen;
    private DefaultTableModel model;
    private JTextField txtTenNhomQuyen;
    private String maNhomQuyen;
    private NhomQuyenBUS nqBUS = new NhomQuyenBUS();
    private ChucNangBUS cnBUS = new ChucNangBUS();
    private ArrayList<ChucNang> allCN;

    public PhanQuyenGanQuyenDialog(JFrame parent, String maNQ, String tenNQ) {
        super(parent, "Chi tiết nhóm quyền", true);
        this.maNhomQuyen = maNQ;
        setSize(500, 600); // Đã thu nhỏ lại chiều rộng vì giờ chỉ còn 2 cột
        setLocationRelativeTo(parent);
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[]20[]20[grow]20[]"));
        getContentPane().setBackground(Color.WHITE);

        add(new JLabel("Tên nhóm quyền"), "split 2, w 120!");
        txtTenNhomQuyen = new JTextField(tenNQ);
        txtTenNhomQuyen.putClientProperty("FlatLaf.style", "arc: 10");
        txtTenNhomQuyen.setEditable(false); // Thường thì tên nhóm quyền không nên sửa ở đây
        add(txtTenNhomQuyen, "growx, h 35!, wrap");

        JLabel lblTableTitle = new JLabel("Danh mục chức năng");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        add(lblTableTitle, "wrap");

        // BƯỚC 1: Sửa lại số lượng cột
        String[] cols = { "Danh mục chức năng", "Truy cập" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                // Cột 0 là chữ, cột 1 là checkbox
                return col == 0 ? String.class : Boolean.class; 
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                // Chỉ cho phép tick/bỏ tick ở cột 1
                return col == 1; 
            }
        };

        tblQuyen = new Table();
        tblQuyen.setModel(model);
        tblQuyen.setRowHeight(40);
        tblQuyen.setShowGrid(false);
        tblQuyen.setIntercellSpacing(new Dimension(0, 0));

        loadData();
        add(new JScrollPane(tblQuyen), "grow, wrap");

        JPanel pnlButtons = new JPanel(new MigLayout("insets 0", "[]10[]", ""));
        pnlButtons.setBackground(Color.WHITE);

        CustomButton btnSave = new CustomButton("LƯU THAY ĐỔI", Theme.PRIMARY_COLOR);
        CustomButton btnCancel = new CustomButton("Huỷ bỏ", new Color(217, 83, 79)); 

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        add(btnCancel, "split 2, center, w 150!, h 40!");
        add(btnSave, "w 150!, h 40!");
    }

    private void loadData() {
        model.setRowCount(0);
        allCN = cnBUS.getAll();
        ArrayList<ChiTietQuyen> listQuyenHienTai = nqBUS.getQuyenCuaNhom(maNhomQuyen);

        for (ChucNang cn : allCN) {
            String maCN = cn.getMaChucNang();

            // Nếu là Admin (NQ01) mà chưa có dữ liệu thì mặc định tick hết
            boolean isDefaultAll = maNhomQuyen.equals("NQ01") && listQuyenHienTai.isEmpty();

            // BƯỚC 2: Chỉ lấy 1 trạng thái cho hành động "read" (Xem)
            model.addRow(new Object[] {
                "Quản lý " + cn.getTenChucNang().toLowerCase(),
                isDefaultAll || checkInList(listQuyenHienTai, maCN, "read")
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

    private void handleSave() {
        ArrayList<ChiTietQuyen> dsQuyenMoi = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String maCN = allCN.get(i).getMaChucNang();
            
            // BƯỚC 3: Cột 1 chứa checkbox (Truy cập)
            boolean isGranted = (Boolean) model.getValueAt(i, 1);
            
            if (isGranted) {
                // Nếu được tick, chúng ta chỉ cần lưu hành động "read"
                dsQuyenMoi.add(new ChiTietQuyen(maNhomQuyen, maCN, "read"));
            }
        }

        if (nqBUS.saveQuyen(maNhomQuyen, dsQuyenMoi)) {
            JOptionPane.showMessageDialog(this, "Cập nhật phân quyền thành công!");
            dispose();
            
            // LƯU Ý: Nếu người dùng đổi quyền của chính họ (ví dụ tắt quyền xem Thống kê), 
            // bạn có thể cần refresh lại Sidebar ngay lập tức.
        } else {
             JOptionPane.showMessageDialog(this, "Lỗi cập nhật phân quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
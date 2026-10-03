package Frontend.GUI.NhanVien;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Backend.BUS.NhomQuyenBUS;
import Backend.DTO.Session;
import Backend.BUS.NhanVienBUS;
import Frontend.Compoent.ButtonAdd;
import Frontend.Compoent.ButtonDele;
import Frontend.Compoent.ButtonFix;
import Frontend.Compoent.ButtonXuatExcel;
import Frontend.Compoent.SearchTextField;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class NhanVienToolbar extends JPanel {
    private NhanVienTable table;
    private NhanVienBUS nvBUS = new NhanVienBUS();
    private NhomQuyenBUS nqBUS = new NhomQuyenBUS();

    public NhanVienToolbar(NhanVienTable table) {
        this.table = table;
        setLayout(new MigLayout("fillx, insets 10", "[grow]10[]10[]10[]"));
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: " + Theme.ROUNDING_ARC);

        SearchTextField txtSearch = new SearchTextField("Tìm kiếm nhân viên ...");
        ButtonAdd btnAdd = new ButtonAdd("Thêm");
        ButtonFix btnFix = new ButtonFix("Sửa");
        ButtonDele btnDele = new ButtonDele("Xóa");
        ButtonXuatExcel btnXuatExcel = new ButtonXuatExcel("Xuất Excel");

        String maNQ = "NQ03";
        if (Session.currentAccount != null) {
            maNQ = Session.currentAccount.getMaNhomQuyen();
        }

        btnAdd.setEnabled(nqBUS.checkQuyen(maNQ, "NHANVIEN", "create"));
        btnFix.setEnabled(nqBUS.checkQuyen(maNQ, "NHANVIEN", "update"));
        btnDele.setEnabled(nqBUS.checkQuyen(maNQ, "NHANVIEN", "delete"));

        add(txtSearch, "growx, h 35!");
        add(btnAdd, "w 95!, h 35!");
        add(btnFix, "w 95!, h 35!");
        add(btnDele, "w 95!, h 35!");
        add(btnXuatExcel, "w 105!, h 35!");

        btnXuatExcel.addActionListener(e -> {
            Frontend.Compoent.XuatExcel.xuat(table.getTbl());
        });

        // txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
        // @Override
        // public void keyReleased(java.awt.event.KeyEvent e) {
        // String query = txtSearch.getText().toLowerCase().trim();
        // table.loadDataBySearch(query);
        // }
        // });

        btnAdd.addActionListener(e -> {
            // Lấy JFrame chứa cái Toolbar này
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Truyền parent vào Dialog
            NhanVienAddDialog dialog = new NhanVienAddDialog(parent);

            dialog.setVisible(true);
            table.loadData();
        });

        btnFix.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hàng để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ma = table.getTbl().getValueAt(selectedRow, 1).toString();
            String ten = table.getTbl().getValueAt(selectedRow, 2).toString();
            String sdt = table.getTbl().getValueAt(selectedRow, 3).toString();
            String diaChi = table.getTbl().getValueAt(selectedRow, 4).toString();
            String chucVu = table.getTbl().getValueAt(selectedRow, 5).toString();
            String email = table.getTbl().getValueAt(selectedRow, 6).toString();

            double luong = Double.parseDouble(table.getTbl().getValueAt(selectedRow, 7).toString().replace(",", ""));

            NhanVienFixDialog dialog = new NhanVienFixDialog(ma, ten, sdt, diaChi, chucVu, email, luong);
            dialog.setVisible(true);
            table.loadData();
            // table.loadComboBox();
        });

        btnDele.addActionListener(e -> {
            int selectedRow = table.getTbl().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa!");
                return;
            }

            String ma = table.getTbl().getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(this,
                    "Xóa nhân viên: " + ma + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                nvBUS.delete(ma);
                table.loadData();
                // table.loadComboBox();
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                table.filterByKeyword(txtSearch.getText());
            }
        });

    }
}

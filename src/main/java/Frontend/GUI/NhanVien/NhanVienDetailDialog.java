package Frontend.GUI.NhanVien;

import javax.swing.*;
import Backend.BUS.TaiKhoanBUS;
import Backend.DTO.TaiKhoan;
import Backend.DTO.NhanVien;
import Frontend.Compoent.BaseThaoTacDialog;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.Color;

public class NhanVienDetailDialog extends BaseThaoTacDialog {
    private NhanVien nv;
    private TaiKhoanBUS tkBUS = new TaiKhoanBUS();

    public NhanVienDetailDialog(NhanVien nv) {
        super("THÔNG TIN CHI TIẾT NHÂN VIÊN", 500, 520);
        this.nv = nv;
        
        // Ẩn nút xác nhận vì đây là màn hình xem chi tiết
        btnXacNhan.setVisible(false);
        btnHuy.setText("Đóng");
        
        displayData();
    }

    @Override
    protected void initForm() {
        // Form chính được cấu trúc trong displayData
    }

    private void displayData() {
        pnlContent.setLayout(new MigLayout("wrap 2, fillx, insets 25", "[120]20[grow]"));

        // Mục thông tin nhân viên
        addHeaderSection("THÔNG TIN NHÂN VIÊN");
        addLabelValue("Mã nhân viên:", nv.getId());
        addLabelValue("Họ và tên:", nv.getHoTen());
        addLabelValue("Chức vụ:", nv.getChucVu());
        addLabelValue("Số điện thoại:", nv.getSdt());
        addLabelValue("Email:", nv.getEmail());
        addLabelValue("Địa chỉ:", nv.getDiaChi());

        // Mục thông tin tài khoản
        addHeaderSection("TÀI KHOẢN HỆ THỐNG");
        
        // Truy vấn tài khoản theo mã nhân viên
        TaiKhoan tk = tkBUS.getTaiKhoanByMaNV(nv.getId()); 

        if (tk != null) {
            addLabelValue("Tên đăng nhập:", tk.getUsername());
            addLabelValue("Mã nhóm quyền:", tk.getMaNhomQuyen());
            String statusText = (tk.getStatus() == 1) ? "Đang hoạt động" : "Bị khóa";
            addLabelValue("Trạng thái:", statusText);
        } else {
            JLabel lblNoAccount = new JLabel("Chưa cấp tài khoản");
            lblNoAccount.setForeground(Color.GRAY);
            lblNoAccount.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            pnlContent.add(lblNoAccount, "span 2");
        }
    }

    private void addHeaderSection(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Theme.PRIMARY_COLOR);
        pnlContent.add(lbl, "span 2, gaptop 15, gapbottom 5");
    }

    private void addLabelValue(String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlContent.add(lbl);
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlContent.add(val);
    }

    @Override
    protected void logicXacNhan() {
        dispose();
    }
}

package Frontend.GUI;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import Backend.BUS.ChiTietQuyenBUS;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import Frontend.GUI.BanHang.BanHangPanel;
import Frontend.GUI.BaoHanh.BaoHanhPanel;
import Frontend.GUI.DoiTra.MainHienThiDoiTra;
import Frontend.GUI.KhachHang.FromHienThiKhachHang;
import Frontend.GUI.KhuyenMai.MainKhuyenMai;
import Frontend.GUI.NhaCungCap.MainHienThiNCC;
import Frontend.GUI.NhanVien.FromHienThiNV;
import Frontend.GUI.Nhaphang.MainHienThiNhapHang;
import Frontend.GUI.PhanQuyen.PhanQuyenPanel;
import Frontend.GUI.PhieuNhap.FromHienThiPhieuNhap;
import Frontend.GUI.PhieuXuat.PhieuXuatPanel;
import Frontend.GUI.SanPham.SanPhamPanel;
import Frontend.GUI.ThongKe.ThongKePanel;
import net.miginfocom.swing.MigLayout;

public class Sidebar extends JPanel {
    private MainFrame parent;
    private ChiTietQuyenBUS qBUS = new ChiTietQuyenBUS();
    private String maNQ;
    private CustomButton activeButton = null;  // Lưu button đang active

    public Sidebar(MainFrame parent, String maNQ) {
        this.parent = parent;
        this.maNQ = maNQ;
        setBackground(Theme.SECONDARY_COLOR);
        setLayout(new MigLayout("wrap 1, fillx, insets 20", "[fill]", ""));

        String[] menuItems = { "Sản phẩm", "Bán hàng", "Nhập hàng", "Phiếu nhập", "Phiếu xuất", "Khuyến mãi",
                "Bảo hành", "Nhà cung cấp", "Nhân viên", "Khách hàng", "Thống kê", "Phân quyền","Đổi trả" };
        for (String item : menuItems) {
            String code = getChucNangCode(item);

            if(qBUS.checkQuyen(maNQ, code, "read")) {
                 add(createMenubtn(item), "h 35:40:45");
            }
        }
           
        FlatSVGIcon logoutIcon = new FlatSVGIcon("images/icon/log-out.svg", 20, 20);
        CustomButton btnLogout = new CustomButton("Đăng xuất", Theme.DANGER_COLOR);
        btnLogout.setIcon(logoutIcon);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(parent,
                    "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                parent.dispose();
                new Frontend.GUI.LogIn.LoginFrame().setVisible(true);
            }
        });
        add(btnLogout, "pushy, aligny bottom, h 40!, gaptop 20");
    }

    private JButton createMenubtn(String text) {
        String iconPath = "images/icon/" + getIconName(text) + ".svg";
        FlatSVGIcon icon = new FlatSVGIcon(iconPath, 20, 20);
        CustomButton btn = new CustomButton(text, Theme.SECONDARY_COLOR);
        btn.setIcon(icon);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            // Deactivate button cũ
            if (activeButton != null) {
                activeButton.setActive(false);
            }
            // Activate button mới
            btn.setActive(true);
            activeButton = btn;
            
            switch (text) {
                case "Sản phẩm":
                    parent.setPage(new SanPhamPanel());
                    break;
                case "Bán hàng":
                    parent.setPage(new BanHangPanel());
                    break;
                case "Nhập hàng":
                    parent.setPage(new MainHienThiNhapHang());
                    break;
                case "Phiếu nhập":
                    parent.setPage(new FromHienThiPhieuNhap());
                    break;
                case "Phiếu xuất":
                    parent.setPage(new PhieuXuatPanel());
                    break;
                case "Nhân viên":
                    parent.setPage(new FromHienThiNV());
                    break;

                case "Khách hàng":
                    parent.setPage(new FromHienThiKhachHang());
                    break;
                case "Khuyến mãi":
                    parent.setPage(new MainKhuyenMai());
                    break;
                case "Bảo hành":
                    parent.setPage(new BaoHanhPanel());
                    break;
                case "Nhà cung cấp":
                    parent.setPage(new MainHienThiNCC());
                    break;
                case "Phân quyền":
                    parent.setPage(new PhanQuyenPanel());
                    break;
                case "Thống kê":
                    parent.setPage(new ThongKePanel());
                    break;
                case "Đổi trả":
                    parent.setPage(new MainHienThiDoiTra());
                    break;
                case "Đăng xuất":
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(parent, "Chức năng " + text + " đang phát triển!");
                    break;
            }
        });
        return btn;
    }
    private String getIconName(String text) {
        switch (text) {
            case "Sản phẩm":
                return "shopping-basket";
            case "Bán hàng":
                return "shield-minus";
            case "Nhập hàng":
                return "shield-plus";
            case "Phiếu nhập":
                return "tickets";
            case "Phiếu xuất":
                return "ticket";
            case "Khuyến mãi":
                return "ticket-slash";
            case "Bảo hành":
                return "shield-check";
            case "Nhà cung cấp":
                return "dock";
            case "Nhân viên":
                return "square-user";
            case "Khách hàng":
                return "file-user";
            case "Thống kê":
                return "chart-line";
            case "Phân quyền":
                return "user-round-pen";
            case "Đổi trả":
                return "panel-top-close";
            default:
                return "help-circle";
        }
    }

    private String getChucNangCode(String text) {
        switch (text) {
            case "Sản phẩm":
                return "SANPHAM";
            case "Bán hàng":
                return "BANHANG";
            case "Nhập hàng":
                return "NHAPHANG";
            case "Phiếu nhập":
                return "PHIEUNHAP";
            case "Phiếu xuất":
                return "PHIEUXUAT";
            case "Khuyến mãi":
                return "KHUYENMAI";
            case "Bảo hành":
                return "BAOHANH";
            case "Nhà cung cấp":
                return "NHACUNGCAP";
            case "Nhân viên":
                return "NHANVIEN";
            case "Khách hàng":
                return "KHACHHANG";
            case "Thống kê":
                return "THONGKE";
            case "Phân quyền":
                return "PHANQUYEN";
            case "Đổi trả":
                return "DOITRA";
            default:
                return "";
        }
    }
}

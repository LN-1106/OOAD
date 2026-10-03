package Backend.DTO;

import Backend.BUS.NhomQuyenBUS;
import Backend.DTO.ChiTietQuyen;
import java.util.ArrayList;

import javax.swing.JButton;

public class Session {
    public static TaiKhoan currentAccount;
    public static NhanVien currentNhanVien;

    // Thêm danh sách quyền
    private static ArrayList<ChiTietQuyen> danhSachQuyen = new ArrayList<>();

    // Gọi sau khi login thành công
    public static void loadQuyen() {
        if (currentAccount != null) {
            NhomQuyenBUS nqBUS = new NhomQuyenBUS();
            danhSachQuyen = nqBUS.getQuyenCuaNhom(currentAccount.getMaNhomQuyen());
        }
    }

    // Kiểm tra quyền
    public static boolean hasPermission(String maChucNang, String hanhDong) {
        for (ChiTietQuyen q : danhSachQuyen) {
            if (q.getMaChucNang().equalsIgnoreCase(maChucNang) &&
                    q.getHanhDong().equalsIgnoreCase(hanhDong)) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        currentAccount = null;
        currentNhanVien = null;
        danhSachQuyen.clear();
    }

    // Trong Session.java, thêm method này:
    public static void applyButtonPermissions(String maChucNang,
            JButton btnThem, JButton btnSua, JButton btnXoa) {
        if (btnThem != null)
            btnThem.setEnabled(hasPermission(maChucNang, "create"));
        if (btnSua != null)
            btnSua.setEnabled(hasPermission(maChucNang, "update"));
        if (btnXoa != null)
            btnXoa.setEnabled(hasPermission(maChucNang, "delete"));
    }
}
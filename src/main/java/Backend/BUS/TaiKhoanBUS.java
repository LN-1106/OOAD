package Backend.BUS;

import Backend.DAO.TaiKhoanDAO;
import Backend.DTO.TaiKhoan;

public class TaiKhoanBUS {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public TaiKhoan login(String username, String password) {
        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        return taiKhoanDAO.login(username, password);
    }

    // THÊM PHƯƠNG THỨC NÀY ĐỂ HẾT LỖI MAVEN
    public TaiKhoan getTaiKhoanByMaNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return null;
        }
        return taiKhoanDAO.getTaiKhoanByMaNV(maNV);
    }

    public boolean isTenDangNhapTonTai(String tenDN) {
        return taiKhoanDAO.existsByUsername(tenDN);
    }
}

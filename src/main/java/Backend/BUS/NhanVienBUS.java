package Backend.BUS;

import java.util.ArrayList;

import Backend.DAO.NhanVienDAO;
import Backend.DTO.ConNguoi;
import Backend.DTO.NhanVien;
import Backend.DTO.TaiKhoan;

public class NhanVienBUS {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public ArrayList<NhanVien> getAllNhanVien() {
        return nhanVienDAO.selectAll();
    }

    public String getNewMa() {
        return nhanVienDAO.generateMaNV();
    }

    public boolean add(NhanVien nv) {
        return nhanVienDAO.insert(nv) > 0;
    }

    public boolean update(NhanVien nv) {
        return nhanVienDAO.update(nv) > 0;
    }

    public boolean delete(String id) {
        return nhanVienDAO.delete(id) > 0;
    }

    public NhanVien getById(String id) {
        if (id == null)
            return null;
        ArrayList<NhanVien> list = getAllNhanVien();
        for (NhanVien nv : list) {
            if (nv.getId().equalsIgnoreCase(id)) {
                return nv;
            }
        }
        return null;
    }

    // Ràng buộc
    public String validate(NhanVien nv, boolean isNew) {
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            return "Tên nhân viên không được để trống!";
        }
        if (!nv.getHoTen().matches("^[\\p{L} ]+$")) {
            return "Tên nhân viên chỉ được chứa chữ cái và khoảng trắng!";
        }

        if (!nv.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            return "Email phải đúng định dạng (ví dụ: abc@gmail.com)!";
        }

        if (nv.getLuong() < 0) {
            return "Lương phải là số dương!";
        }

        if (nv.getChucVu() == null || nv.getChucVu().trim().isEmpty()) {
            return "Chức vụ không được để trống!";
        }
        if (nv.getChucVu().matches(".*\\d.*")) {
            return "Chức vụ không được chứa chữ số!";
        }
        if (!nv.getSdt().matches("^0\\d{9}$")) {
            return "SĐT phải có 10 chữ số và bắt đầu bằng số 0!";
        }
        ArrayList<NhanVien> list = getAllNhanVien();
        for (NhanVien oldNv : list) {
            if (oldNv.getSdt().equals(nv.getSdt())) {
                if (isNew || !oldNv.getId().equals(nv.getId())) {
                    return "Số điện thoại này đã được sử dụng bởi nhân viên khác!";
                }
            }
        }
        return "OK";
    }

    public boolean addNhanVienWithAccount(NhanVien nv, TaiKhoan tk) {
        return nhanVienDAO.insertWithAccount(nv, tk);
    }

    public String getNextMa() {
        return nhanVienDAO.generateMaNV();
    }
}

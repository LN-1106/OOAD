package Backend.BUS;

import java.util.ArrayList;

import Backend.DAO.KhachHangDAO;
import Backend.DTO.KhachHang;

public class KhachHangBUS {
    private final KhachHangDAO khDAO = new KhachHangDAO();

    public ArrayList<KhachHang> getAllKhachHang() {
        return khDAO.selectAll();
    }
    public String getNewMa() {
        return khDAO.generateMaKH();
    }

    public boolean add(KhachHang kh) {
        return khDAO.insert(kh) > 0;
    }

    public boolean update(KhachHang kh) {
        return khDAO.update(kh) > 0;
    }

    public boolean delete(String id) {
        return khDAO.delete(id) > 0;
    }
    
    public String validate(KhachHang kh, boolean isNew) {
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            return "Tên khách hàng không được để trống!";
        }
        if (!kh.getHoTen().matches("^[\\p{L} ]+$")) {
            return "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng!";
        }
        if (!kh.getSdt().matches("^0\\d{9}$")) {
            return "Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!";
        }
        ArrayList<KhachHang> list = getAllKhachHang();
        for (KhachHang oldKh : list) {
            if (oldKh.getSdt().equals(kh.getSdt())) {
                if (isNew || !oldKh.getId().equals(kh.getId())) {
                    return "Số điện thoại này đã được đăng ký cho khách hàng khác!";
                }
            }
        }
        return "OK";
    }
}

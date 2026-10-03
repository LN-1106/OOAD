package Backend.BUS;

import Backend.DAO.KhuyenMaiDAO;
import Backend.DTO.KhuyenMai;
import java.util.ArrayList;

public class KhuyenMaiBUS {
    private KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
    private ArrayList<KhuyenMai> dsKM = new ArrayList<>();

    public KhuyenMaiBUS() {
        refreshData();
    }

    public void refreshData() {
        kmDAO.autoUpdateExpiredStatus();
        dsKM = kmDAO.getAll();
    }

    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        return dsKM;
    }

    /**
     * TẠO MÃ MỚI TỰ ĐỘNG (Fix lỗi compilation: cannot find symbol getNewMaKM)
     */
    public String getNewMaKM() {
        int max = 0;
        for (KhuyenMai km : dsKM) {
            // Tách phần số từ mã "KMxxx"
            try {
                int num = Integer.parseInt(km.getMaKM().replace("KM", ""));
                if (num > max) max = num;
            } catch (Exception e) {
                // Bỏ qua nếu mã không đúng định dạng KMxxx
            }
        }
        return String.format("KM%03d", max + 1);
    }

    /**
     * KIỂM TRA DỮ LIỆU (Fix lỗi compilation: cannot find symbol validate)
     */
    public String validate(KhuyenMai km, boolean isNew) {
        if (km.getTenKM().isEmpty()) return "Tên chương trình không được để trống!";
        if (km.getPhanTramGiam() <= 0 || km.getPhanTramGiam() > 100) return "% Giảm phải từ 1 đến 100!";
        if (km.getNgayBD() == null || km.getNgayKT() == null) return "Vui lòng chọn đầy đủ ngày!";
        if (km.getNgayKT().before(km.getNgayBD())) return "Ngày kết thúc phải sau ngày bắt đầu!";
        
        // Kiểm tra mã: khi thêm mới, mã không được trùng; khi sửa, mã không được trùng với record khác
        for (KhuyenMai existing : dsKM) {
            if (existing.getMaKM().equalsIgnoreCase(km.getMaKM()) && !existing.getMaKM().equals(km.getMaKM())) {
                return "Mã KM đã tồn tại!";
            }
        }
        return "OK";
    }

    public boolean delete(String maKM) {
        if (kmDAO.updateStatus(maKM, 0)) {
            refreshData();
            return true;
        }
        return false;
    }

    public String addKhuyenMai(KhuyenMai km) {
        if (kmDAO.add(km)) {
            refreshData();
            return "Thêm thành công!";
        }
        return "Thêm thất bại!";
    }

    public String updateKhuyenMai(KhuyenMai km) {
        if (kmDAO.update(km)) {
            refreshData();
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public ArrayList<KhuyenMai> search(String text) {
        text = text.toLowerCase().trim();
        ArrayList<KhuyenMai> result = new ArrayList<>();
        for (KhuyenMai km : dsKM) {
            if (km.getMaKM().toLowerCase().contains(text) ||
                km.getTenKM().toLowerCase().contains(text)) {
                result.add(km);
            }
        }
        return result;
    }

    public KhuyenMai getById(String maKM) {
        for (KhuyenMai km : dsKM) {
            if (km.getMaKM().equals(maKM)) {
                return km;
            }
        }
        return null;
    }

    public ArrayList<KhuyenMai> getActiveKhuyenMai() {
        ArrayList<KhuyenMai> result = new ArrayList<>();
        for (KhuyenMai km : dsKM) {
            if (km.getTrangThai() == 1) {
                result.add(km);
            }
        }
        return result;
    }
}

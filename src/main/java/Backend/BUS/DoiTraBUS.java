package Backend.BUS;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import Backend.DAO.DoiTraDAO;
import Backend.DTO.DoiTra;

public class DoiTraBUS {
    private final DoiTraDAO dtDAO = new DoiTraDAO();

    public ArrayList<DoiTra> getAll() {
        return dtDAO.selectAll();
    }

    public String generateMaDoiTra() {
        ArrayList<DoiTra> list = dtDAO.selectAll();
        int max = 0;
        for (DoiTra dt : list) {
            try {
                int num = Integer.parseInt(dt.getMaDoiTra().substring(2));
                if (num > max) max = num;
            } catch (Exception e) {
            }
        }
        return String.format("DT%03d", max + 1);
    }

    // public String add(DoiTra dt) {
    //     String validateMsg = validate(dt);
    //     if (!validateMsg.equals("OK")) {
    //         return validateMsg;
    //     }
    //     int result = dtDAO.insert(dt);
    //     if (result > 0) {
    //         boolean isUpdatedStock = dtDAO.updateImeiReturn(dt.getMaImei());
    //         if (isUpdatedStock) {
    //             return "OK";
    //         } else {
    //             return "Phiếu đã tạo nhưng không thể cập nhật trạng thái kho!";
    //         }
    //     }
    //     return "Lỗi hệ thống: Không thể thêm phiếu đổi trả.";
    // }

    public String add(DoiTra dt, String imeiMoi) {
        String validateMsg = validate(dt);
        if (!validateMsg.equals("OK")) return validateMsg;

        // Lấy thông tin phiên bản của máy cũ và máy mới để so sánh
        String maPB_Cu = dtDAO.getMaPhienBanByImei(dt.getMaImei());
        String maPB_Moi = (imeiMoi != null) ? dtDAO.getMaPhienBanByImei(imeiMoi) : null;

        if (dtDAO.getMaDoiTraByImei(dt.getMaImei()) != null) {
            return "Lỗi: Máy này đã được thực hiện đổi trả trước đó!";
        }
        int result = dtDAO.insert(dt); 
        
        if (result > 0) {
            // 1. Thu hồi máy cũ (Set MaPhieuXuat = NULL)
            dtDAO.updateOldImeiStatus(dt.getMaImei(), "Máy lỗi đổi trả");
            
            if (imeiMoi != null && !imeiMoi.isEmpty()) {
                // 2. Giao máy mới (Gán MaPhieuXuat cũ vào IMEI mới)
                dtDAO.assignNewImeiToInvoice(imeiMoi, dt.getMaPhieuXuat());

                // 3. XỬ LÝ ĐỔI KHÁC PHIÊN BẢN
                if (!maPB_Cu.equals(maPB_Moi)) {
                    // Giảm số lượng dòng cũ trong ChiTietPhieuXuat
                    dtDAO.updateQuantityInChiTietPX(dt.getMaPhieuXuat(), maPB_Cu, -1);
                    // Tăng (hoặc thêm mới) dòng phiên bản mới vào ChiTietPhieuXuat
                    dtDAO.updateQuantityInChiTietPX(dt.getMaPhieuXuat(), maPB_Moi, 1);
                }
            }
            return "OK";
        }
        return "Lỗi hệ thống";
    }

    // Sửa hàm delete để khi xóa phiếu đổi trả thì máy phải được "hồi sinh" về trạng thái Đã bán
public boolean delete(String maDT) {
    String imeiLoi = dtDAO.getImeiByMaDoiTra(maDT);
    boolean isDeleted = dtDAO.softDelete(maDT) > 0;
    if (isDeleted && imeiLoi != null) {
        // Trả máy lỗi về trạng thái Đã bán (coi như chưa từng đổi trả)
        dtDAO.restoreImeiStatus(imeiLoi);
        // Lưu ý: Nếu có máy mới đã giao, bạn cần logic thu hồi máy mới đó về kho (tùy độ phức tạp)
    }
    return isDeleted;
}

    public String update(DoiTra dt) {
        String validateMsg = validate(dt);
        if (!validateMsg.equals("OK")) {
            return validateMsg;
        }
        
        return dtDAO.update(dt) > 0 ? "OK" : "Cập nhật thất bại!";
    }

    public String validate(DoiTra dt) {
        if (dt.getMaPhieuXuat() == null || dt.getMaPhieuXuat().isEmpty()) 
            return "Chưa chọn phiếu xuất!";
        if (dt.getMaImei() == null || dt.getMaImei().isEmpty()) 
            return "Chưa chọn mã IMEI sản phẩm!";
        if (dt.getLyDo() == null || dt.getLyDo().trim().isEmpty()) 
            return "Lý do đổi trả không được để trống!";

        // Check IMEI belongs to invoice
        if (!dtDAO.checkImeiInvoice(dt.getMaImei(), dt.getMaPhieuXuat())) {
            return "Lỗi: IMEI không thuộc phiếu xuất đã chọn!";
        }

        LocalDate ngayMua = dtDAO.getNgayMuaByMaPX(dt.getMaPhieuXuat());
        if (ngayMua == null) return "Không tìm thấy thông tin ngày mua của phiếu xuất này!";

        long soNgay = ChronoUnit.DAYS.between(ngayMua, dt.getNgayDoiTra());

        if (soNgay < 0) {
            return "Lỗi: Ngày đổi trả không được trước ngày mua hàng!";
        }
        if (soNgay > 30) {
            return "Lỗi: Đã quá hạn đổi trả (Quá 30 ngày kể từ ngày mua).";
        }

        return "OK";
    }

    public ArrayList<DoiTra> search(String keyword) {
        ArrayList<DoiTra> all = getAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return all;
        }

        String[] keywords = keyword.toLowerCase().trim().split("\\s+");
        ArrayList<DoiTra> result = new ArrayList<>();

        for (DoiTra dt : all) {
            String searchTarget = (dt.getMaDoiTra() + " " + 
                                dt.getMaPhieuXuat() + " " + 
                                dt.getMaImei() + " " + 
                                dt.getTenKH() + " " + 
                                dt.getLyDo()).toLowerCase();

            boolean matchesAll = true;
            for (String k : keywords) {
                if (!searchTarget.contains(k)) {
                    matchesAll = false;
                    break;
                }
            }

            if (matchesAll) {
                result.add(dt);
            }
        }
        return result;
    }

    public java.util.HashMap<String, String> getProductInfoByImei(String imei) {
        return dtDAO.getProductInfoByImei(imei);
    }
}
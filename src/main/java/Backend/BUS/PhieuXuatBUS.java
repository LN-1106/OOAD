package Backend.BUS;

import Backend.DAO.ChiTietPhieuXuatDAO;
import Backend.DAO.PhieuXuatDAO;
import Backend.DAO.SanPhamDAO;
import Backend.DTO.ChiTietPhieuXuat;
import Backend.DTO.PhieuXuat;
import java.util.ArrayList;

public class PhieuXuatBUS {
    private final PhieuXuatDAO pxDAO = new PhieuXuatDAO();
    private final ChiTietPhieuXuatDAO ctpxDAO = new ChiTietPhieuXuatDAO();

    public ArrayList<PhieuXuat> getAllPhieuXuat() {
        return pxDAO.selectAll();
    }

    public ArrayList<PhieuXuat> search(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return pxDAO.selectAll();
        }
        return pxDAO.search(keyword);
    }

    public ArrayList<PhieuXuat> selectAll() {
        return pxDAO.selectAll();
    }

    public String getNewMaPhieu() {
        return pxDAO.generateMaPhieuXuat();
    }

    public boolean thanhToan(PhieuXuat px, ArrayList<ChiTietPhieuXuat> dsCT) {
        return pxDAO.thanhToan(px, dsCT);
    }

    public ArrayList<PhieuXuat> filter(java.util.Date start, java.util.Date end, String maNV, double min, double max) {
        ArrayList<PhieuXuat> all = pxDAO.selectAll();
        ArrayList<PhieuXuat> result = new ArrayList<>();

        for (PhieuXuat px : all) {
            boolean matchDate = true;
            if (start != null && px.getNgayXuat().before(start))
                matchDate = false;
            if (end != null && px.getNgayXuat().after(end))
                matchDate = false;

            boolean matchNV = maNV.equals("Tất cả") || px.getMaNV().equalsIgnoreCase(maNV);

            boolean matchPrice = px.getTongTien() >= min && px.getTongTien() <= max;

            if (matchDate && matchNV && matchPrice) {
                result.add(px);
            }
        }
        return result;
    }

    public double getPhanTramGiamCuaPhieu(String maPX) {
        return pxDAO.getDiscountPercentage(maPX);
    }

    public String getTenKhachHang(String maKH) {
        return pxDAO.getTenKhachHang(maKH);
    }
}
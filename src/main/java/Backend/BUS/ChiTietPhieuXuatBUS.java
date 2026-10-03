package Backend.BUS;

import Backend.DAO.ChiTietPhieuXuatDAO;
import Backend.DTO.ChiTietPhieuXuat;
import Backend.DAO.ChiTietSPDAO;
import java.util.ArrayList;

public class ChiTietPhieuXuatBUS {
    private final ChiTietPhieuXuatDAO ctpxDAO = new ChiTietPhieuXuatDAO();
    private final ChiTietSPDAO ctspDAO = new ChiTietSPDAO();

    public ArrayList<ChiTietPhieuXuat> getAllChiTietPhieuXuat(String maPX) {
        return ctpxDAO.selectAll(maPX);
    }

    public ArrayList<String> getImeisByMaPX(String maPX, String maPB) {
        return ctspDAO.getImeisByMaPX(maPX, maPB);
    }
}

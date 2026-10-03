package Backend.BUS;

import Backend.DAO.ChiTietPhieuNhapDAO;
import Backend.DAO.ChiTietSPDAO;
import Backend.DTO.ChiTietPhieuNhap;
import java.util.ArrayList;

public class ChiTietPhieuNhapBUS {
    private final ChiTietPhieuNhapDAO ctDAO = new ChiTietPhieuNhapDAO();
    private final ChiTietSPDAO ctspDAO = new ChiTietSPDAO();

    public ArrayList<ChiTietPhieuNhap> getByMaPhieu(String maPN) {
        return ctDAO.selectAll(maPN);
    }

    public ArrayList<String> getImeisByDetails(String maPN, String maPB) {
        return ctspDAO.getImeisByDetails(maPN, maPB);
    }
}

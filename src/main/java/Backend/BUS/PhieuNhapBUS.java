package Backend.BUS;

import Backend.DAO.ChiTietSPDAO;
import Backend.DAO.ChiTietPhieuNhapDAO;
import Backend.DAO.PhieuNhapDAO;
import Backend.DTO.ChiTietPhieuNhap;
import Backend.DTO.PhieuNhap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.HashSet;


public class PhieuNhapBUS {
    private final PhieuNhapDAO pnDAO = new PhieuNhapDAO();
    private final ChiTietPhieuNhapDAO ctDAO = new ChiTietPhieuNhapDAO();
    private final ChiTietSPDAO ctspDAO = new ChiTietSPDAO();

    public ArrayList<PhieuNhap> getAllPhieuNhap() {
        return pnDAO.selectAll();
    }

    public ArrayList<PhieuNhap> getByNCC(String maNCC) {
        return pnDAO.selectByNCC(maNCC);
    }

    public ArrayList<PhieuNhap> getFilteredPhieuNhap(/*String maNCC,*/ Date from, Date to, long min, long max) {
        return pnDAO.selectByFilter(/*maNCC,*/ from, to, min, max);
    }

    public String getNewMaPhieu() {
        return pnDAO.generateMaPhieuNhap();
    }

    public String generateRandomImei() {
        Random rand = new Random();
        String imei;
        do {
            long number = (long)(rand.nextDouble() * 900_000_000L) + 100_000_000L;
            imei = String.valueOf(number);
        } while (ctspDAO.checkImeiExists(imei));
        return imei;
    }

    public boolean thanhToan(PhieuNhap pn, ArrayList<ChiTietPhieuNhap> dsCT) {
        // Insert phiếu nhập
        if(pnDAO.insert(pn) > 0) {
            if(ctDAO.insert(dsCT) > 0) {
                // Sau khi insert chi tiết phiếu nhập, sinh IMEI cho từng sản phẩm
                for(ChiTietPhieuNhap ct : dsCT) {
                    for(int i = 0; i < ct.getSoLuong(); i++) {
                        String imei = generateRandomImei();
                        // Lưu vào bảng ChiTietSP (IMEI, MaPB, MaPN, MaPX=null, TinhTrang='Trong kho')
                        ctspDAO.insertImei(imei, ct.getMaPhienBan(), pn.getmaPhieuNhap());
                    }
                }
                return true;
            }
        }
        return false;
    }
}

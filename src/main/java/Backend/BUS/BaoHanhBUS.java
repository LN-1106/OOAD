package Backend.BUS;

import Backend.DAO.BaoHanhDAO;
import Backend.DTO.BaoHanh;
import java.util.ArrayList;

public class BaoHanhBUS {
    private final BaoHanhDAO bhDAO = new BaoHanhDAO();

    public ArrayList<BaoHanh> getAllBaoHanh() {
        return bhDAO.selectAllWithDetails();
    }

    public boolean add(BaoHanh bh) {
        if (!bhDAO.checkImeiExists(bh.getMaImei())) {
            System.err.println("Mã Imei không tồn tại: " + bh.getMaImei());
            return false;
        }
        return bhDAO.insert(bh) > 0;
    }

    public boolean update(BaoHanh bh) {
        return bhDAO.update(bh) > 0;
    }

    public boolean delete(String id) {
        ChiTietBaoHanhBUS ctbhBUS = new ChiTietBaoHanhBUS();

        ctbhBUS.delete(id);

        return bhDAO.delete(id) > 0;
    }

    public String getNewMaBH() {
        return bhDAO.generateMaBH();
    }

    public String getMaBHByImei(String maImei) {
        return bhDAO.getMaBHByImei(maImei);
    }
}
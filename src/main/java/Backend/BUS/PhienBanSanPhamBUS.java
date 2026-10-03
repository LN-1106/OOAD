package Backend.BUS;

import Backend.DAO.PhienBanSanPhamDAO;
import Backend.DAO.ChiTietSPDAO;
import Backend.DTO.PhienBanSanPham;
import java.util.ArrayList;

public class PhienBanSanPhamBUS {
    private final PhienBanSanPhamDAO pbspDAO = new PhienBanSanPhamDAO();
    private final ChiTietSPDAO ctDAO = new ChiTietSPDAO();

    public ArrayList<PhienBanSanPham> getAllPhienBanSanPham() {
        return pbspDAO.selectAll();
    }

    public ArrayList<PhienBanSanPham> getByNCC(String ma) {
        return pbspDAO.selectByNCC(ma);
    }

    public ArrayList<PhienBanSanPham> getByMaSP(String ma) {
        return pbspDAO.selectByMaSP(ma);
    }

    public boolean update(PhienBanSanPham pbsp) {
        return pbspDAO.update(pbsp) > 0;
    }

    public boolean add(PhienBanSanPham pbsp) {
        return pbspDAO.insert(pbsp) > 0;
    }

    public boolean delete(String id) {
        if (pbspDAO.delete(id) > 0) {
            ctDAO.deleteByMaPB(id);
            return true;
        }
        return false;
    }

    public String getNextID() {
        return pbspDAO.getNextID();
    }

    public ArrayList<PhienBanSanPham> search(String text) {
        return pbspDAO.search(text);
    }

    public ArrayList<PhienBanSanPham> getByLoai(String tenLoai) {
        return pbspDAO.selectByLoai(tenLoai);
    }

    public PhienBanSanPham getByMaPhienBan(String maPB) {
        return pbspDAO.selectById(maPB);
    }

    public ArrayList<PhienBanSanPham> getFilteredListNhapHang(String maNCC, String maLoai, String query) {
        return pbspDAO.selectByFilterNhapHang(maNCC, maLoai, query != null ?  query.trim() : "");
    }

    public ArrayList<PhienBanSanPham> getFilteredListBanHang(String maLoai, String query) {
        return pbspDAO.selectByFilterBanHang(maLoai, query != null ? query.trim() : "");
    }

}

package Backend.BUS;

import Backend.DAO.PhienBanSanPhamDAO;
import Backend.DAO.SanPhamDAO;
import Backend.DTO.SanPham;
import java.util.ArrayList;

public class SanPhamBUS {
    private final SanPhamDAO spDAO = new SanPhamDAO();
    private final PhienBanSanPhamDAO pbDAO = new PhienBanSanPhamDAO();

    public ArrayList<SanPham> getAll() {
        return spDAO.selectAll();
    }

    public boolean update(SanPham sp){
        return spDAO.update(sp)>0;
    }

    public boolean add(SanPham sp){
        return spDAO.insert(sp)>0;
    }

    public boolean delete(String id){
        if(spDAO.delete(id)>0){
            pbDAO.deleteByMaSP(id);
            return true;
        }
        return false;
    }

    public String getNextID() {
        return spDAO.getNextID();
    }
    
    public ArrayList<String> getDanhSachLoai(){
        return spDAO.getDanhSachLoai();
    }
    public ArrayList<String> getDanhSachHang(){
        return spDAO.getDanhSachHang();
    }
}


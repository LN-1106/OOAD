package Backend.BUS;

import Backend.DAO.LoaiSPDAO;
import Backend.DTO.ThuocTinhSanPham.LoaiSP;
import java.util.ArrayList;

public class LoaiSPBUS {
    private final LoaiSPDAO loaispDAO = new LoaiSPDAO();

    public ArrayList<LoaiSP> getAll() {
        return loaispDAO.selectAll();
    }
}

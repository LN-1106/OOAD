package Backend.BUS;

import Backend.DAO.ConNguoiDAO;
import Backend.DTO.ConNguoi;
import java.util.ArrayList;

public class ConNguoiBUS {
    
    private final ConNguoiDAO conNguoiDAO = new ConNguoiDAO();

    public ArrayList<ConNguoi> getAllConNguoi() {
        return conNguoiDAO.selectAll();
    }

    public boolean add(ConNguoi cn) {
        return conNguoiDAO.insert(cn) > 0;
    }

    public boolean update(ConNguoi cn) {
        return conNguoiDAO.update(cn) > 0;
    }

    // public boolean delete(String id) {
    //     return conNguoiDAO.delete(id) > 0;
    // }
}
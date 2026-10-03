package Backend.BUS;

import Backend.DAO.ChucNangDAO;
import Backend.DTO.ChucNang;
import java.util.ArrayList;

public class ChucNangBUS {
      private final ChucNangDAO cnDAO = new ChucNangDAO();

      public ArrayList<ChucNang> getAll() {
            return cnDAO.selectAll();
      }

      public ChucNang getById(String id) {
            return cnDAO.selectById(id);
      }
}
package Backend.BUS;

import Backend.DAO.ChiTietSPDAO;
import Backend.DTO.ChiTietSP;

import java.util.ArrayList;
public class ChiTietSPBUS {
        private final ChiTietSPDAO ctDAO = new ChiTietSPDAO();

        public ArrayList<ChiTietSP> getAllChiTietSP() {
                return ctDAO.selectAll();
        }
        public ArrayList<ChiTietSP> getByMaPB(String ma){
                return ctDAO.selectByMaPB(ma);
        }

        public boolean update(ChiTietSP ct){
                return ctDAO.update(ct)>0;
        }

        public boolean delete(String id){
                return ctDAO.delete(id)>0;
        }
        
        public boolean add(ChiTietSP ct){
                return ctDAO.insert(ct)>0;
        }


}

package Backend.BUS;

import Backend.DAO.ChiTietQuyenDAO;
import Backend.DTO.ChiTietQuyen;
import java.util.ArrayList;

public class ChiTietQuyenBUS {
      private final ChiTietQuyenDAO ctqDAO = new ChiTietQuyenDAO();

      public ArrayList<ChiTietQuyen> getQuyenCuaNhom(String maNQ) {
            return ctqDAO.selectByMaNQ(maNQ);
      }

      public boolean checkQuyen(String maNQ, String maCN, String hanhDong) {
            ArrayList<ChiTietQuyen> list = ctqDAO.selectByMaNQ(maNQ);
            for (ChiTietQuyen ct : list) {
                  if (ct.getMaChucNang().equalsIgnoreCase(maCN) &&
                              ct.getHanhDong().equalsIgnoreCase(hanhDong)) {
                        return true;
                  }
            }
            return false;
      }

}
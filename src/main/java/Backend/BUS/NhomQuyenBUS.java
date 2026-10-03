package Backend.BUS;

import Backend.DAO.NhomQuyenDAO;
import Backend.DAO.ChiTietQuyenDAO;
import Backend.DTO.NhomQuyen;
import Backend.DTO.ChiTietQuyen;
import java.util.ArrayList;

public class NhomQuyenBUS {
      private final NhomQuyenDAO nqDAO = new NhomQuyenDAO();
      private final ChiTietQuyenDAO ctqDAO = new ChiTietQuyenDAO();

      public ArrayList<NhomQuyen> getAll() {
            return nqDAO.selectAll();
      }

      public NhomQuyen getById(String id) {
            return nqDAO.selectById(id);
      }

      public String getNextMa() {
            return nqDAO.generateMaNQ();
      }

      public boolean saveQuyen(String maNQ, ArrayList<ChiTietQuyen> dsQuyenMoi) {
            return ctqDAO.savePermissions(maNQ, dsQuyenMoi);
      }

      public ArrayList<NhomQuyen> search(String keyword) {
            ArrayList<NhomQuyen> all = nqDAO.selectAll();
            ArrayList<NhomQuyen> result = new ArrayList<>();
            for (NhomQuyen nq : all) {
                  if (nq.getMaNhomQuyen().toLowerCase().contains(keyword.toLowerCase()) ||
                              nq.getTenNhomQuyen().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(nq);
                  }
            }
            return result;
      }

      public boolean checkQuyen(String maNQ, String maCN, String hanhDong) {
            if (maNQ.equalsIgnoreCase("NQ01")) {
                  return true;
            }
            ArrayList<ChiTietQuyen> list = ctqDAO.selectByMaNQ(maNQ);
            for (ChiTietQuyen q : list) {
                  if (q.getMaChucNang().equalsIgnoreCase(maCN) &&
                              q.getHanhDong().equalsIgnoreCase(hanhDong)) {
                        return true;
                  }
            }
            return false;
      }

      public ArrayList<ChiTietQuyen> getQuyenCuaNhom(String maNQ) {
            return ctqDAO.selectByMaNQ(maNQ);
      }

      public boolean add(NhomQuyen nq) {
            return nqDAO.insert(nq) > 0;
      }

      public boolean update(NhomQuyen nq) {
            return nqDAO.update(nq) > 0;
      }

      public boolean addNewRoleWithPermissions(NhomQuyen nq, ArrayList<ChiTietQuyen> dsQuyen) {
            if (nqDAO.insert(nq) > 0) {
                  return ctqDAO.savePermissions(nq.getMaNhomQuyen(), dsQuyen);
            }
            return false;
      }

      public boolean updateRoleWithPermissions(NhomQuyen nq, ArrayList<ChiTietQuyen> dsQuyenMoi) {
            if (nqDAO.update(nq) >= 0) {
                  return ctqDAO.savePermissions(nq.getMaNhomQuyen(), dsQuyenMoi);
            }
            return false;
      }

      public ArrayList<ChiTietQuyen> getQuyenByNhom(String maNQ) {
            return nqDAO.selectQuyenByNhom(maNQ);
      }
}
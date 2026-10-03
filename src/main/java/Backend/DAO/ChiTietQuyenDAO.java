package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietQuyen;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietQuyenDAO {

      public ArrayList<ChiTietQuyen> selectByMaNQ(String maNQ) {
            ArrayList<ChiTietQuyen> list = new ArrayList<>();
            String sql = "SELECT * FROM ChiTietQuyen WHERE MaNhomQuyen = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, maNQ);
                  ResultSet rs = stmt.executeQuery();
                  while (rs.next()) {
                        list.add(new ChiTietQuyen(rs.getString(1), rs.getString(2), rs.getString(3)));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return list;
      }

      public boolean savePermissions(String maNQ, ArrayList<ChiTietQuyen> dsQuyen) {
            Connection conn = null;
            try {
                  conn = DatabaseHelper.getConnection();
                  conn.setAutoCommit(false); 

                  String sqlDelete = "DELETE FROM ChiTietQuyen WHERE MaNhomQuyen = ?";
                  try (PreparedStatement pstDel = conn.prepareStatement(sqlDelete)) {
                        pstDel.setString(1, maNQ);
                        pstDel.executeUpdate();
                  }

                  String sqlInsert = "INSERT INTO ChiTietQuyen (MaNhomQuyen, MaChucNang, HanhDong) VALUES (?, ?, ?)";
                  try (PreparedStatement pstIns = conn.prepareStatement(sqlInsert)) {
                        for (ChiTietQuyen q : dsQuyen) {
                              pstIns.setString(1, q.getMaNhomQuyen());
                              pstIns.setString(2, q.getMaChucNang());
                              pstIns.setString(3, q.getHanhDong());
                              pstIns.addBatch();
                        }
                        pstIns.executeBatch();
                  }

                  conn.commit(); 
                  return true;
            } catch (Exception e) {
                  if (conn != null) {
                        try {
                              conn.rollback();
                        } catch (SQLException ex) {
                              ex.printStackTrace();
                        }
                  }
                  e.printStackTrace();
                  return false;
            }
      }
}
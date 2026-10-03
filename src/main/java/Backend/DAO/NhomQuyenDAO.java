package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietQuyen;
import Backend.DTO.NhomQuyen;
import java.sql.*;
import java.util.ArrayList;

public class NhomQuyenDAO implements DAOInterface<NhomQuyen> {

      @Override
      public int insert(NhomQuyen nq) {
            String sql = "INSERT INTO NhomQuyen (MaNhomQuyen, TenNhomQuyen, MoTa) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, nq.getMaNhomQuyen());
                  stmt.setString(2, nq.getTenNhomQuyen());
                  stmt.setString(3, nq.getMoTa());
                  return stmt.executeUpdate();
            } catch (Exception e) {
                  e.printStackTrace();
                  return 0;
            }
      }

      @Override
      public ArrayList<NhomQuyen> selectAll() {
            ArrayList<NhomQuyen> list = new ArrayList<>();
            String sql = "SELECT * FROM NhomQuyen";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {
                  while (rs.next()) {
                        list.add(new NhomQuyen(
                                    rs.getString("MaNhomQuyen"),
                                    rs.getString("TenNhomQuyen"),
                                    rs.getString("MoTa")));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return list;
      }

      @Override
      public NhomQuyen selectById(String id) {
            String sql = "SELECT * FROM NhomQuyen WHERE MaNhomQuyen = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, id);
                  ResultSet rs = stmt.executeQuery();
                  if (rs.next()) {
                        return new NhomQuyen(rs.getString(1), rs.getString(2), rs.getString(3));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return null;
      }

      @Override
      public int update(NhomQuyen nq) {
            String sql = "UPDATE NhomQuyen SET TenNhomQuyen = ?, MoTa = ? WHERE MaNhomQuyen = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, nq.getTenNhomQuyen());
                  stmt.setString(2, nq.getMoTa());
                  stmt.setString(3, nq.getMaNhomQuyen());
                  return stmt.executeUpdate();
            } catch (Exception e) {
                  e.printStackTrace();
                  return 0;
            }
      }

      @Override
      public int delete(String id) {
            String sql = "DELETE FROM NhomQuyen WHERE MaNhomQuyen = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, id);
                  return stmt.executeUpdate();
            } catch (Exception e) {
                  e.printStackTrace();
                  return 0;
            }
      }

      public String generateMaNQ() {
            String sql = "SELECT MaNhomQuyen FROM NhomQuyen ORDER BY CAST(SUBSTRING(MaNhomQuyen, 3) AS UNSIGNED) DESC LIMIT 1";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {
                  if (rs.next()) {
                        String lastMa = rs.getString(1);
                        int num = Integer.parseInt(lastMa.substring(2));
                        return String.format("NQ%02d", num + 1);
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return "NQ01";
      }

      public ArrayList<ChiTietQuyen> selectQuyenByNhom(String maNQ) {
            ArrayList<ChiTietQuyen> list = new ArrayList<>();
            String sql = "SELECT * FROM ChiTietQuyen WHERE MaNhomQuyen = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, maNQ);
                  ResultSet rs = stmt.executeQuery();
                  while (rs.next()) {
                        list.add(new ChiTietQuyen(
                                    rs.getString("MaNhomQuyen"),
                                    rs.getString("MaChucNang"),
                                    rs.getString("HanhDong")));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return list;
      }
}
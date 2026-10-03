package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChucNang;
import java.sql.*;
import java.util.ArrayList;

public class ChucNangDAO implements DAOInterface<ChucNang> {

      @Override
      public ArrayList<ChucNang> selectAll() {
            ArrayList<ChucNang> list = new ArrayList<>();
            String sql = "SELECT * FROM ChucNang";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {
                  while (rs.next()) {
                        list.add(new ChucNang(rs.getString(1), rs.getString(2), rs.getString(3)));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return list;
      }

      @Override
      public ChucNang selectById(String id) {
            String sql = "SELECT * FROM ChucNang WHERE MaChucNang = ?";
            try (Connection conn = DatabaseHelper.getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, id);
                  ResultSet rs = stmt.executeQuery();
                  if (rs.next()) {
                        return new ChucNang(rs.getString(1), rs.getString(2), rs.getString(3));
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
            return null;
      }

      @Override
      public int insert(ChucNang cn) {
            return 0;
      }

      @Override
      public int update(ChucNang cn) {
            return 0;
      }

      @Override
      public int delete(String id) {
            return 0;
      }
}
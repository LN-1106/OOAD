package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ConNguoi;
import java.sql.*;
import java.util.ArrayList;

public class ConNguoiDAO {
    
    public ArrayList<ConNguoi> selectAll() {
        ArrayList<ConNguoi> list = new ArrayList<>();
        String sql = "SELECT * FROM ConNguoi";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new ConNguoi(
                        rs.getString("ID"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int insert(ConNguoi cn) {
        String sql = "INSERT INTO ConNguoi(ID, HoTen, SDT, DiaChi) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cn.getId());
            ps.setString(2, cn.getHoTen());
            ps.setString(3, cn.getSdt());
            ps.setString(4, cn.getDiaChi());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int update(ConNguoi cn) {
        String sql = "UPDATE ConNguoi SET HoTen=?, SDT=?, DiaChi=? WHERE ID=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cn.getHoTen());
            ps.setString(2, cn.getSdt());
            ps.setString(3, cn.getDiaChi());
            ps.setString(4, cn.getId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0;}
    }

    // public int delete(String id) {
    //     String sql = "DELETE FROM ConNguoi WHERE ID=?";
    //     try (Connection conn = DatabaseHelper.getConnection();
    //          PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setString(1, id);
    //         return ps.executeUpdate();
    //     } catch (Exception e) { e.printStackTrace(); }
    //     return 0;
    // }
}
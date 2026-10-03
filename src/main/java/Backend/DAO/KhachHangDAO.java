package Backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Backend.DTO.KhachHang;
import Backend.DatabaseHelper;

public class KhachHangDAO implements DAOInterface<KhachHang> {

    public ArrayList<KhachHang> selectAll() {
        ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT c.ID, c.HoTen, c.SDT, c.DiaChi, k.TrangThai " +
                "FROM ConNguoi c JOIN KhachHang k ON c.ID = k.ID " +
                "WHERE k.TrangThai = TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new KhachHang(
                        rs.getString("ID"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getBoolean("TrangThai")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int insert(KhachHang kh) {
        // Insert vào ConNguoi trước
        new ConNguoiDAO().insert(kh);
        String sql = "INSERT INTO KhachHang(ID, TrangThai) VALUES(?,TRUE)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int update(KhachHang kh) {
        // Update bảng ConNguoi trước
        new ConNguoiDAO().update(kh);
        String sql = "UPDATE KhachHang SET TrangThai = ? WHERE ID=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, kh.isTrangThai());
            ps.setString(2, kh.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(String id) {
        String sql = "UPDATE KhachHang SET TrangThai=FALSE WHERE ID=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String generateMaKH() {
        String sql = "SELECT ID FROM KhachHang ORDER BY CAST(SUBSTRING(ID, 4) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if(rs.next()) {
                String lastMa = rs.getString("ID");
                int num = Integer.parseInt(lastMa.substring(3));
                return String.format("KH%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KH00";
    }

    @Override
    public KhachHang selectById(String id) {
        String sql = "SELECT c.ID, c.HoTen, c.SDT, c.DiaChi, k.TrangThai " +
                "FROM ConNguoi c JOIN KhachHang k ON c.ID = k.ID WHERE c.ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new KhachHang(
                        rs.getString("ID"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getBoolean("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
package Backend.DAO;

import Backend.DTO.KhuyenMai;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class KhuyenMaiDAO {
    // Sửa lại các hàm trong KhuyenMaiDAO.java
    // Trong KhuyenMaiDAO.java
    public ArrayList<KhuyenMai> getAll() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        // BỎ DieuKienGiam khỏi câu SELECT
        String sql = "SELECT MaKM, TenKM, PhanTramGiam, NgayBatDau, NgayKetThuc, TrangThai FROM KhuyenMai";
        try (Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new KhuyenMai(
                        rs.getString("MaKM"),
                        rs.getString("TenKM"),
                        rs.getDouble("PhanTramGiam"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc"),
                        rs.getInt("TrangThai")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean add(KhuyenMai km) {
        // BỎ DieuKienGiam khỏi câu INSERT
        String sql = "INSERT INTO KhuyenMai (MaKM, TenKM, PhanTramGiam, NgayBatDau, NgayKetThuc, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setDate(4, new java.sql.Date(km.getNgayBD().getTime()));
            ps.setDate(5, new java.sql.Date(km.getNgayKT().getTime()));
            ps.setInt(6, 1);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET TenKM=?, PhanTramGiam=?, NgayBatDau=?, NgayKetThuc=?, TrangThai=? WHERE MaKM=?";
        try (Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getPhanTramGiam());
            ps.setDate(3, new java.sql.Date(km.getNgayBD().getTime()));
            ps.setDate(4, new java.sql.Date(km.getNgayKT().getTime()));
            ps.setInt(5, km.getTrangThai());
            ps.setString(6, km.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateStatus(String maKM, int status) {
        String sql = "UPDATE KhuyenMai SET TrangThai = ? WHERE MaKM = ?";
        try (Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, maKM);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Thêm vào KhuyenMaiDAO.java
    public int autoUpdateExpiredStatus() {
        // Cập nhật trạng thái về 0 cho những mã có ngày kết thúc nhỏ hơn ngày hiện tại
        String sql = "UPDATE KhuyenMai SET TrangThai = 0 WHERE NgayKetThuc < CURRENT_DATE AND TrangThai = 1";
        try (Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}

package Backend.DAO;

import Backend.DTO.TaiKhoan;
import Backend.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoanDAO {

    public TaiKhoan login(String username, String password) {
        TaiKhoan tk = null;
        String sql = """
                    SELECT TenDangNhap, MatKhau, MaNV, MaNhomQuyen, TrangThai
                    FROM TaiKhoan
                    WHERE TenDangNhap = ? AND MatKhau = ? AND TrangThai = 1
                """;
        try (
                Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tk = new TaiKhoan();
                tk.setUsername(rs.getString("TenDangNhap"));
                tk.setPassword(rs.getString("MatKhau"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setMaNhomQuyen(rs.getString("MaNhomQuyen"));
                tk.setStatus(rs.getBoolean("TrangThai") ? 1 : 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }

    public TaiKhoan getTaiKhoanByMaNV(String maNV) {
        TaiKhoan tk = null;
        String sql = "SELECT TenDangNhap, MatKhau, MaNV, MaNhomQuyen, TrangThai FROM TaiKhoan WHERE MaNV = ?";

        try (
                Connection con = DatabaseHelper.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tk = new TaiKhoan();
                tk.setUsername(rs.getString("TenDangNhap"));
                tk.setPassword(rs.getString("MatKhau"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setMaNhomQuyen(rs.getString("MaNhomQuyen"));
                tk.setStatus(rs.getBoolean("TrangThai") ? 1 : 0);
            }
        } catch (Exception e) {
            System.err.println("Lỗi lấy tài khoản theo mã NV: " + e.getMessage());
            e.printStackTrace();
        }
        return tk;
    }

    public int insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, MaNhomQuyen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getUsername());
            ps.setString(2, tk.getPassword());
            ps.setString(3, tk.getMaNV());
            ps.setString(4, tk.getMaNhomQuyen());
            ps.setInt(5, tk.getStatus());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE TenDangNhap = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

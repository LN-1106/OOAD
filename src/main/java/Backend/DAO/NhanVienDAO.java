package Backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.ConNguoi;
import Backend.DTO.NhanVien;
import Backend.DTO.TaiKhoan;
import Backend.DatabaseHelper;

public class NhanVienDAO implements DAOInterface<NhanVien> {

    public ArrayList<NhanVien> selectAll() {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = "SELECT c.ID, c.HoTen, c.SDT, c.DiaChi, n.ChucVu, n.Email, n.Luong, n.TrangThai " +
                "FROM ConNguoi c JOIN NhanVien n ON c.ID = n.ID " +
                "WHERE n.TrangThai = TRUE";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new NhanVien(
                        rs.getString("ID"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getString("ChucVu"),
                        rs.getString("Email"),
                        rs.getDouble("Luong"),
                        rs.getBoolean("TrangThai")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(NhanVien nv) {
        // Insert vào ConNguoi trước
        new ConNguoiDAO().insert(nv);
        String sql = "INSERT INTO NhanVien(ID, ChucVu, Email, Luong, TrangThai) VALUES(?,?,?,?,TRUE)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getId());
            ps.setString(2, nv.getChucVu());
            ps.setString(3, nv.getEmail());
            ps.setDouble(4, nv.getLuong());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int update(NhanVien nv) {
        new ConNguoiDAO().update(nv);
        String sql = "UPDATE NhanVien SET ChucVu=?, Email=?, Luong=? WHERE ID=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getChucVu());
            ps.setString(2, nv.getEmail());
            ps.setDouble(3, nv.getLuong());
            ps.setString(4, nv.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(String id) {
        String sql = "UPDATE NhanVien SET TrangThai=FALSE WHERE ID=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String generateMaNV() {
        String sql = "SELECT ID FROM NhanVien ORDER BY CAST(SUBSTRING(ID, 4) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("ID");
                int num = Integer.parseInt(lastMa.substring(3));
                return String.format("NV%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NV00";
    }

    @Override
    public NhanVien selectById(String id) {
        String sql = "SELECT c.ID, c.HoTen, c.SDT, c.DiaChi, n.ChucVu, n.Email, n.Luong, n.TrangThai " +
                "FROM ConNguoi c JOIN NhanVien n ON c.ID = n.ID WHERE c.ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new NhanVien(
                        rs.getString("ID"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getString("ChucVu"),
                        rs.getString("Email"),
                        rs.getDouble("Luong"),
                        rs.getBoolean("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertWithAccount(NhanVien nv, TaiKhoan tk) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            String sqlCN = "INSERT INTO ConNguoi (ID, HoTen, SDT, DiaChi) VALUES (?, ?, ?, ?)";
            PreparedStatement psCN = conn.prepareStatement(sqlCN);
            psCN.setString(1, nv.getId());
            psCN.setString(2, nv.getHoTen());
            psCN.setString(3, nv.getSdt());
            psCN.setString(4, nv.getDiaChi());
            psCN.executeUpdate();

            String sqlNV = "INSERT INTO NhanVien (ID, ChucVu, Email, Luong, TrangThai) VALUES (?, ?, ?, ?, TRUE)";
            PreparedStatement psNV = conn.prepareStatement(sqlNV);
            psNV.setString(1, nv.getId());
            psNV.setString(2, nv.getChucVu());
            psNV.setString(3, nv.getEmail());
            psNV.setDouble(4, nv.getLuong());
            psNV.executeUpdate();

            String sqlTK = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, MaNhomQuyen, TrangThai) VALUES (?, ?, ?, ?, 1)";
            PreparedStatement psTK = conn.prepareStatement(sqlTK);
            psTK.setString(1, tk.getUsername());
            psTK.setString(2, tk.getPassword());
            psTK.setString(3, tk.getMaNV());
            psTK.setString(4, tk.getMaNhomQuyen());
            psTK.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            e.printStackTrace();
            return false;
        }
    }
}
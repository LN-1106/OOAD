package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietSP;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietSPDAO implements DAOInterface<ChiTietSP> {

    @Override
    public ArrayList<ChiTietSP> selectAll() {
        return new ArrayList<>();
    }

    @Override
    public ChiTietSP selectById(String id) {
        ChiTietSP ketQua = null;
        String sql = "SELECT * FROM ChiTietSP WHERE MaImei=? WHERE TrangThai=TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ketQua = new ChiTietSP(
                        rs.getString("MaImei"),
                        rs.getString("MaPhienBan"),
                        rs.getString("MaPhieuNhap"),
                        rs.getString("MaPhieuXuat"),
                        rs.getString("TinhTrang"),
                        rs.getBoolean("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<ChiTietSP> selectByMaPB(String maPhienBan) {
        ArrayList<ChiTietSP> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietSP WHERE MaPhienBan=? AND TrangThai=TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhienBan);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietSP ct = new ChiTietSP(
                        rs.getString("MaImei"),
                        rs.getString("MaPhienBan"),
                        rs.getString("MaPhieuNhap"),
                        rs.getString("MaPhieuXuat"),
                        rs.getString("TinhTrang"),
                        rs.getBoolean("TrangThai"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insert(ChiTietSP ct) {
        String sql = "INSERT INTO ChiTietSP(MaImei, MaPhienBan, MaPhieuNhap, MaPhieuXuat, TinhTrang) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ct.getMaImei());
            stmt.setString(2, ct.getMaPhienBan());
            stmt.setString(3, ct.getMaPhieuNhap());
            stmt.setString(4, ct.getMaPhieuXuat());
            stmt.setString(5, ct.getTinhTrang());

            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(ChiTietSP ct) {
        // Chỉ cho phép cập nhật Tình Trạng dựa theo IMEI
        String sql = "UPDATE ChiTietSP SET TinhTrang=? WHERE MaImei=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ct.getTinhTrang());
            stmt.setString(2, ct.getMaImei());

            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE ChiTietSP SET TrangThai=FALSE WHERE MaImei=? ";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteByMaPB(String maSP) {
        String sql = "UPDATE ChiTietSP SET TrangThai = FALSE WHERE MaPhienBan = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean checkImeiExists(String imei) {
        String sql = "SELECT 1 FROM ChiTietSP WHERE MaImei = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imei);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertImei(String imei, String maPB, String maPN) {
        String sql = "INSERT INTO ChiTietSP (MaImei, MaPhienBan, MaPhieuNhap, TinhTrang, TrangThai) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imei);
            stmt.setString(2, maPB);
            stmt.setString(3, maPN);
            stmt.setString(4, "Trong kho");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getImeisByDetails(String maPhieuNhap, String maPhienBan) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaImei FROM ChiTietSP WHERE MaPhieuNhap = ? AND MaPhienBan = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuNhap);
            stmt.setString(2, maPhienBan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("MaImei"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getImeisByMaPX(String maPhieuXuat, String maPhienBan) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaImei FROM ChiTietSP WHERE MaPhieuXuat = ? AND MaPhienBan = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuXuat);
            stmt.setString(2, maPhienBan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("MaImei"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

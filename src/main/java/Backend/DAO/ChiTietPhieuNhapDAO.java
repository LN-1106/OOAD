package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietPhieuNhap;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietPhieuNhapDAO implements ChiTietInterface<ChiTietPhieuNhap> {
    @Override
    public int insert(ArrayList<ChiTietPhieuNhap> list) {
        int count = 0;
        String sql = "INSERT INTO ChiTietPhieuNhap VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ChiTietPhieuNhap ct : list) {
                stmt.setString(1, ct.getMaPhieuNhap());
                stmt.setString(2, ct.getMaPhienBan());
                stmt.setInt(3, ct.getSoLuong());
                stmt.setDouble(4, ct.getDonGia());
                stmt.setDouble(5, ct.getThanhTien());
                count += stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public ArrayList<ChiTietPhieuNhap> selectAll(String maPN) {
        ArrayList<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, sp.HinhAnh FROM ChiTietPhieuNhap ct " +
                "JOIN PhienBanSP pb ON ct.MaPhienBan = pb.MaPhienBan " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE ct.MaPhieuNhap = ?";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPN);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap(
                        rs.getString("MaPhieuNhap"),
                        rs.getString("MaPhienBan"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("DonGia"),
                        rs.getDouble("ThanhTien"));
                ct.setTenSP(rs.getString("TenSP"));
                ct.setHinhAnh(rs.getString("HinhAnh"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public int update(ArrayList<ChiTietPhieuNhap> t, String pk) {
        return 0;
    }
}

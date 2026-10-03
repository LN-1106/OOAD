package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietPhieuXuat;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietPhieuXuatDAO implements ChiTietInterface<ChiTietPhieuXuat> {

    @Override
    public int insert(ArrayList<ChiTietPhieuXuat> list) {
        int count = 0;
        String sql = "INSERT INTO ChiTietPhieuXuat VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ChiTietPhieuXuat ct : list) {
                stmt.setString(1, ct.getMaPhieuXuat());
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
    public ArrayList<ChiTietPhieuXuat> selectAll(String maPX) {
        ArrayList<ChiTietPhieuXuat> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, sp.HinhAnh FROM ChiTietPhieuXuat ct " +
                "JOIN PhienBanSP pb ON ct.MaPhienBan = pb.MaPhienBan " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE ct.MaPhieuXuat = ?";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPX);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuXuat ctpx = new ChiTietPhieuXuat();
                    ctpx.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
                    ctpx.setMaPhienBan(rs.getString("MaPhienBan"));
                    ctpx.setSoLuong(rs.getInt("SoLuong"));
                    ctpx.setDonGia(rs.getDouble("DonGia"));
                    ctpx.setThanhTien(rs.getDouble("ThanhTien"));

                    ctpx.setTenSP(rs.getString("TenSP"));
                    ctpx.setHinhAnh(rs.getString("HinhAnh"));
                    list.add(ctpx);
                }
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
    public int update(ArrayList<ChiTietPhieuXuat> t, String pk) {
        return 0;
    }
}
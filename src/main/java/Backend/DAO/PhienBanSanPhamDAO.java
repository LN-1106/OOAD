package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.PhienBanSanPham;
import java.sql.*;
import java.util.ArrayList;

public class PhienBanSanPhamDAO implements DAOInterface<PhienBanSanPham> {
    
    @Override
    public ArrayList<PhienBanSanPham> selectAll() {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " + "JOIN SanPham sp ON pb.MaSP = sp.MaSP "
                + "WHERE pb.TrangThai = TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PhienBanSanPham pbsp = new PhienBanSanPham();
                pbsp.setMaPhienBan(rs.getString("MaPhienBan"));
                pbsp.setMaSP(rs.getString("MaSP"));
                pbsp.setMauSac(rs.getString("MauSac"));
                pbsp.setCongSuat(rs.getString("CongSuat"));
                pbsp.setPin(rs.getString("Pin"));
                pbsp.setKetNoi(rs.getString("KetNoi"));
                pbsp.setGiaNhap(rs.getDouble("GiaNhap"));
                pbsp.setGiaBan(rs.getDouble("GiaBan"));
                pbsp.setSoLuongTon(rs.getInt("SoLuongTon"));
                pbsp.setTrangThai(rs.getBoolean("TrangThai"));
                pbsp.setHinhAnh(rs.getString("HinhAnh"));

                pbsp.setTenSP(rs.getString("TenSP"));
                list.add(pbsp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PhienBanSanPham selectById(String maPhienBan) {
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE pb.MaPhienBan = ? AND pb.TrangThai = TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhienBan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham(
                        rs.getString("MaPhienBan"),
                        rs.getString("MaSP"),
                        rs.getString("MauSac"),
                        rs.getString("CongSuat"),
                        rs.getString("Pin"),
                        rs.getString("KetNoi"),
                        rs.getDouble("GiaNhap"),
                        rs.getDouble("GiaBan"),
                        rs.getInt("SoLuongTon"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("HinhAnh"));
                
                pb.setTenSP(rs.getString("TenSP"));
                return pb; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(PhienBanSanPham pbsp) {
        int result = 0;
        String sql = "INSERT INTO PhienBanSP VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pbsp.getMaPhienBan());
            stmt.setString(2, pbsp.getMaSP());
            stmt.setString(3, pbsp.getMauSac());
            stmt.setString(4, pbsp.getCongSuat());
            stmt.setString(5, pbsp.getPin());
            stmt.setString(6, pbsp.getKetNoi());
            stmt.setDouble(7, pbsp.getGiaNhap());
            stmt.setDouble(8, pbsp.getGiaBan());
            stmt.setInt(9, pbsp.getSoLuongTon());
            stmt.setBoolean(10, true);
            stmt.setString(11, pbsp.getHinhAnh());

            result = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(PhienBanSanPham pbsp) {
        String sql = "UPDATE PhienBanSP SET MauSac=?, CongSuat=?, Pin=?, KetNoi=?, GiaNhap=?, GiaBan=?, SoLuongTon=?, HinhAnh=? WHERE MaPhienBan=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pbsp.getMauSac());
            stmt.setString(2, pbsp.getCongSuat());
            stmt.setString(3, pbsp.getPin());
            stmt.setString(4, pbsp.getKetNoi());
            stmt.setDouble(5, pbsp.getGiaNhap());
            stmt.setDouble(6, pbsp.getGiaBan());
            stmt.setInt(7, pbsp.getSoLuongTon());
            stmt.setString(8, pbsp.getHinhAnh());
            stmt.setString(9, pbsp.getMaPhienBan());

            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE PhienBanSP SET TrangThai=FALSE WHERE MaPhienBan=? ";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteByMaSP(String maSP) {
        String sql = "UPDATE PhienBanSP SET TrangThai = FALSE WHERE MaSP = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Chọn theo nhà cung cấp
    public ArrayList<PhienBanSanPham> selectByNCC(String maNCC) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "JOIN NCC_SanPham ncc_sp ON sp.MaSP = ncc_sp.MaSP " +
                "WHERE ncc_sp.MaNCC = ?";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNCC);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham(
                        rs.getString("MaPhienBan"),
                        rs.getString("MaSP"),
                        rs.getString("MauSac"),
                        rs.getString("CongSuat"),
                        rs.getString("Pin"),
                        rs.getString("KetNoi"),
                        rs.getDouble("GiaNhap"),
                        rs.getDouble("GiaBan"),
                        rs.getInt("SoLuongTon"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("HinhAnh"));
                pb.setTenSP(rs.getString("TenSP"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy Phiên Bản từ mã sản phẩm
    public ArrayList<PhienBanSanPham> selectByMaSP(String maSP) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE pb.MaSP = ? AND pb.TrangThai = TRUE";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSP);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham(
                        rs.getString("MaPhienBan"),
                        rs.getString("MaSP"),
                        rs.getString("MauSac"),
                        rs.getString("CongSuat"),
                        rs.getString("Pin"),
                        rs.getString("KetNoi"),
                        rs.getDouble("GiaNhap"),
                        rs.getDouble("GiaBan"),
                        rs.getInt("SoLuongTon"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("HinhAnh"));
                pb.setTenSP(rs.getString("TenSP"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNextID() {
        String sql = "SELECT MaPhienBan FROM PhienBanSP";
        int maxId = 0;
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPB = rs.getString("MaPhienBan");
                if (maPB != null) {
                    try {
                        int id = Integer.parseInt(maPB.replaceAll("[^0-9]", ""));
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PB" + String.format("%03d", maxId + 1);
    }

    public ArrayList<PhienBanSanPham> selectByLoai(String maLoai) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE sp.MaLoai = ? AND pb.TrangThai = TRUE";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maLoai);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham();
                pb.setMaPhienBan(rs.getString("MaPhienBan"));
                pb.setMaSP(rs.getString("MaSP"));
                pb.setTenSP(rs.getString("TenSP"));
                pb.setMauSac(rs.getString("MauSac"));

                pb.setGiaBan(rs.getDouble("GiaBan"));
                pb.setHinhAnh(rs.getString("HinhAnh"));

                pb.setGiaNhap(rs.getDouble("GiaNhap"));
                pb.setSoLuongTon(rs.getInt("SoLuongTon"));
                pb.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<PhienBanSanPham> selectByFilterNhapHang(String maNCC, String maLoai, String query) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                        "JOIN SanPham sp ON pb.MaSP = sp.MaSP "
        );

        if (!maNCC.equals("All"))
            sql.append(" JOIN NCC_SanPham ncc_sp ON sp.MaSP = ncc_sp.MaSP");
        sql.append(" WHERE pb.TrangThai = TRUE");
        if (!maNCC.equals("All"))
            sql.append(" AND ncc_sp.MaNCC = ?");
        if (!maLoai.equals("All"))
            sql.append(" AND sp.MaLoai = ?");
        if (query != null && !query.trim().isEmpty()) {
            String[] words = query.toLowerCase().split("\\s+");
            for (int i = 0; i < words.length; i++) {
                sql.append(" AND (LOWER(sp.TenSP) LIKE ? OR LOWER(pb.MaPhienBan) LIKE ? " +
                        "OR LOWER(pb.MauSac) LIKE ? OR CAST(pb.GiaNhap AS CHAR) LIKE ? " +
                        "OR CAST(pb.SoLuongTon AS CHAR) LIKE ?)");
            }
        }

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (!maNCC.equals("All"))
                stmt.setString(index++, maNCC);
            if (!maLoai.equals("All"))
                stmt.setString(index++, maLoai);
            if (query != null && !query.trim().isEmpty()) {
                String[] words = query.toLowerCase().split("\\s+");
                for (String word : words) {
                    String p = "%" + word + "%";
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham(
                        rs.getString("MaPhienBan"), rs.getString("MaSP"), rs.getString("MauSac"),
                        rs.getString("CongSuat"), rs.getString("Pin"), rs.getString("KetNoi"),
                        rs.getDouble("GiaNhap"), rs.getDouble("GiaBan"), rs.getInt("SoLuongTon"),
                        rs.getBoolean("TrangThai"), rs.getString("HinhAnh"));
                pb.setTenSP(rs.getString("TenSP"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<PhienBanSanPham> selectByFilterBanHang(String maLoai, String query) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                        "JOIN SanPham sp ON pb.MaSP = sp.MaSP "
        );

        sql.append(" WHERE pb.TrangThai = TRUE");
        if (!maLoai.equals("All"))
            sql.append(" AND sp.MaLoai = ?");
        if (query != null && !query.trim().isEmpty()) {
            String[] words = query.toLowerCase().split("\\s+");
            for (int i = 0; i < words.length; i++) {
                sql.append(" AND (LOWER(sp.TenSP) LIKE ? OR LOWER(pb.MaPhienBan) LIKE ? " +
                        "OR LOWER(pb.MauSac) LIKE ? OR CAST(pb.GiaBan AS CHAR) LIKE ? " +
                        "OR CAST(pb.SoLuongTon AS CHAR) LIKE ?)");
            }
        }

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (!maLoai.equals("All"))
                stmt.setString(index++, maLoai);
            if (query != null && !query.trim().isEmpty()) {
                String[] words = query.toLowerCase().split("\\s+");
                for (String word : words) {
                    String p = "%" + word + "%";
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                    stmt.setString(index++, p);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhienBanSanPham pb = new PhienBanSanPham(
                    rs.getString("MaPhienBan"), rs.getString("MaSP"), rs.getString("MauSac"),
                        rs.getString("CongSuat"), rs.getString("Pin"), rs.getString("KetNoi"),
                        rs.getDouble("GiaNhap"), rs.getDouble("GiaBan"), rs.getInt("SoLuongTon"),
                        rs.getBoolean("TrangThai"), rs.getString("HinhAnh"));
                pb.setTenSP(rs.getString("TenSP"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<PhienBanSanPham> search(String text) {
        ArrayList<PhienBanSanPham> list = new ArrayList<>();
        String sql = "SELECT pb.*, sp.TenSP FROM PhienBanSP pb " +
                "JOIN SanPham sp ON pb.MaSP = sp.MaSP " +
                "WHERE pb.TrangThai = TRUE AND (pb.MaPhienBan LIKE ? OR sp.TenSP LIKE ? OR pb.MauSac LIKE ?)";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String query = "%" + text + "%";
            stmt.setString(1, query);
            stmt.setString(2, query);
            stmt.setString(3, query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhienBanSanPham pbsp = new PhienBanSanPham();
                pbsp.setMaPhienBan(rs.getString("MaPhienBan"));
                pbsp.setMaSP(rs.getString("MaSP"));
                pbsp.setMauSac(rs.getString("MauSac"));
                pbsp.setCongSuat(rs.getString("CongSuat"));
                pbsp.setPin(rs.getString("Pin"));
                pbsp.setKetNoi(rs.getString("KetNoi"));
                pbsp.setGiaNhap(rs.getDouble("GiaNhap"));
                pbsp.setGiaBan(rs.getDouble("GiaBan"));
                pbsp.setSoLuongTon(rs.getInt("SoLuongTon"));
                pbsp.setTrangThai(rs.getBoolean("TrangThai"));
                pbsp.setHinhAnh(rs.getString("HinhAnh"));
                pbsp.setTenSP(rs.getString("TenSP"));

                list.add(pbsp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
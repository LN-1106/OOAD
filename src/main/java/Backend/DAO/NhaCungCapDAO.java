package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.NhaCungCap;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCapDAO implements DAOInterface<NhaCungCap> {
    @Override
    public int insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, Sdt, TrangThai) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ncc.getMaNCC());
            stmt.setString(2, ncc.getTenNCC());
            stmt.setString(3, ncc.getDiaChi());
            stmt.setString(4, ncc.getSdt());
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public ArrayList<NhaCungCap> selectAll() {
        ArrayList<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE TrangThai = TRUE ORDER BY TenNCC DESC";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new NhaCungCap(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int update(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET TenNCC=?, DiaChi=?, SDT=? WHERE MaNCC=?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ncc.getTenNCC());
            stmt.setString(2, ncc.getDiaChi());
            stmt.setString(3, ncc.getSdt());
            stmt.setString(4, ncc.getMaNCC());
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE NhaCungCap SET TrangThai = FAlSE WHERE MaNCC = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public NhaCungCap selectById(String id) {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNCC = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new NhaCungCap(
                        rs.getString("MaNCC"),
                        rs.getString("TenNCC"),
                        rs.getString("DiaChi"),
                        rs.getString("Sdt"),
                        rs.getBoolean("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateMaNCC() {
        String sql = "SELECT MaNCC FROM NhaCungCap ORDER BY CAST(SUBSTRING(MaNCC, 4) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if(rs.next()) {
                String lastMa = rs.getString("MaNCC");
                int num = Integer.parseInt(lastMa.substring(3));
                return String.format("NCC%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NCC000";
    }

    public ArrayList<String> getMaSPByNCC(String maNCC) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaSP FROM NCC_SanPham WHERE MaNCC = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maNCC);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) list.add(rs.getString("MaSP"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateSanPhamCungCap(String maNCC, ArrayList<String> dsMaSP) {
        String sqlDelete = "DELETE FROM NCC_SanPham WHERE MaNCC = ?";
        String sqlInsert = "INSERT INTO NCC_SanPham (MaNCC, MaSP) VALUES (?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection()) {
            conn.setAutoCommit(false);
            
            // Xóa hết cũ
            try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setString(1, maNCC);
                psDel.executeUpdate();
            }
            
            // Thêm mới
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
                for (String maSP : dsMaSP) {
                    psIns.setString(1, maNCC);
                    psIns.setString(2, maSP);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }
            
            conn.commit();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}

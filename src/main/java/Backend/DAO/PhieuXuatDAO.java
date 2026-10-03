package Backend.DAO;

import Backend.DatabaseHelper;
import Backend.DTO.ChiTietPhieuXuat;
import Backend.DTO.PhieuXuat;
import java.util.ArrayList;
import java.sql.*;

public class PhieuXuatDAO implements DAOInterface<PhieuXuat> {
    @Override
    public ArrayList<PhieuXuat> selectAll() {
        ArrayList<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PhieuXuat px = new PhieuXuat();

                px.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
                px.setNgayXuat(rs.getTimestamp("NgayXuat"));
                px.setMaNV(rs.getString("MaNV"));
                px.setMaKH(rs.getString("MaKH"));
                px.setMaKM(rs.getString("MaKM"));
                px.setTongTien(rs.getDouble("TongTien"));
                px.setTrangThai(rs.getBoolean("TrangThai"));

                list.add(px);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insert(PhieuXuat px) {
        String sql = "INSERT INTO PhieuXuat (MaPhieuXuat, NgayXuat, MaNV, MaKH, MaKM, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, px.getMaPhieuXuat());
            stmt.setTimestamp(2, px.getNgayXuat());
            stmt.setString(3, px.getMaNV());
            stmt.setString(4, px.getMaKH());
            stmt.setString(5, px.getMaKM());
            stmt.setDouble(6, px.getTongTien());
            stmt.setBoolean(7, px.getTrangThai());
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public PhieuXuat selectById(String id) {
        String sql = "SELECT * FROM PhieuXuat WHERE MaPhieuXuat = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PhieuXuat(
                        rs.getString("MaPhieuXuat"),
                        rs.getTimestamp("NgayXuat"),
                        rs.getString("MaNV"),
                        rs.getString("MaKH"),
                        rs.getString("MaKM"),
                        rs.getDouble("TongTien"),
                        rs.getBoolean("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(PhieuXuat px) {
        return 0;
    }

    @Override
    public int delete(String id) {
        throw new UnsupportedOperationException("Không được xóa phiếu!");
    }

    public String generateMaPhieuXuat() {
        String sql = "SELECT MaPhieuXuat FROM PhieuXuat ORDER BY CAST(SUBSTRING(MaPhieuXuat, 3) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("MaPhieuXuat");
                int num = Integer.parseInt(lastMa.substring(2));
                return String.format("PX%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PX000";
    }

    public ArrayList<PhieuXuat> search(String keyword) {
        ArrayList<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat WHERE MaPhieuXuat LIKE ? OR MaKH LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhieuXuat px = new PhieuXuat();
                    px.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
                    px.setNgayXuat(rs.getTimestamp("NgayXuat"));
                    px.setMaNV(rs.getString("MaNV"));
                    px.setMaKH(rs.getString("MaKH"));
                    px.setMaKM(rs.getString("MaKM"));
                    px.setTongTien(rs.getDouble("TongTien"));
                    list.add(px);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean thanhToan(PhieuXuat px, ArrayList<ChiTietPhieuXuat> dsChiTiet) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            String sqlPX = "INSERT INTO PhieuXuat (MaPhieuXuat, NgayXuat, MaNV, MaKH, MaKM, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstPX = conn.prepareStatement(sqlPX);
            pstPX.setString(1, px.getMaPhieuXuat());
            pstPX.setTimestamp(2, px.getNgayXuat());
            pstPX.setString(3, px.getMaNV());
            pstPX.setString(4, px.getMaKH());
            pstPX.setString(5, px.getMaKM());
            pstPX.setDouble(6, px.getTongTien());
            pstPX.setBoolean(7, true);
            pstPX.executeUpdate();

            String sqlCT = "INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaPhienBan, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            String sqlSelectImei = "SELECT MaImei FROM ChiTietSP WHERE MaPhienBan = ? AND TinhTrang = 'Trong kho' LIMIT ?";
            String sqlUpdateImei = "UPDATE ChiTietSP SET TinhTrang = 'Đã bán', MaPhieuXuat = ? WHERE MaImei = ?";
            String sqlInsertBH = "INSERT INTO BaoHanh (MaBH, MaImei, MaPhieuXuat, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?, ?)";

            String sqlInsertCTBH = "INSERT INTO ChiTietBaoHanh (MaCTBH, MaBH, NoiDung, TinhTrang) VALUES (?, ?, ?, ?)";

            PreparedStatement pstCT = conn.prepareStatement(sqlCT);
            PreparedStatement pstUpImei = conn.prepareStatement(sqlUpdateImei);
            PreparedStatement pstInsBH = conn.prepareStatement(sqlInsertBH);
            PreparedStatement pstGetImei = conn.prepareStatement(sqlSelectImei);
            PreparedStatement pstInsCTBH = conn.prepareStatement(sqlInsertCTBH);

            int currentNumBH = Integer.parseInt(generateMaBaoHanh().substring(2));
            int currentNumCTBH = Integer.parseInt(new ChiTietBaoHanhDAO().generateMaCTBH().substring(4));

            for (ChiTietPhieuXuat ct : dsChiTiet) {
                pstCT.setString(1, ct.getMaPhieuXuat());
                pstCT.setString(2, ct.getMaPhienBan());
                pstCT.setInt(3, ct.getSoLuong());
                pstCT.setDouble(4, ct.getDonGia());
                pstCT.addBatch();

                pstGetImei.setString(1, ct.getMaPhienBan());
                pstGetImei.setInt(2, ct.getSoLuong());
                ResultSet rsImei = pstGetImei.executeQuery();

                int countFound = 0;
                while (rsImei.next()) {
                    countFound++;
                    String imeiReal = rsImei.getString("MaImei");

                    pstUpImei.setString(1, px.getMaPhieuXuat());
                    pstUpImei.setString(2, imeiReal);
                    pstUpImei.addBatch();

                    String maBH = String.format("BH%03d", currentNumBH++);
                    pstInsBH.setString(1, maBH);
                    pstInsBH.setString(2, imeiReal);
                    pstInsBH.setString(3, px.getMaPhieuXuat());
                    pstInsBH.setTimestamp(4, px.getNgayXuat());

                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(px.getNgayXuat());
                    cal.add(java.util.Calendar.MONTH, 12);
                    pstInsBH.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
                    pstInsBH.addBatch();

                    String maCTBH = "CT" + maBH;
                    pstInsCTBH.setString(1, maCTBH);
                    pstInsCTBH.setString(2, maBH);
                    pstInsCTBH.setString(3, "Thiết bị mới xuất kho - Kích hoạt bảo hành điện tử");
                    pstInsCTBH.setString(4, "Còn bảo hành");
                    pstInsCTBH.addBatch();
                }

                if (countFound < ct.getSoLuong()) {
                    throw new Exception("Sản phẩm mã " + ct.getMaPhienBan() + " không đủ máy trong kho!");
                }
            }

            pstCT.executeBatch();
            pstUpImei.executeBatch();
            pstInsBH.executeBatch();
            pstInsCTBH.executeBatch();

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            throw new RuntimeException(e.getMessage());
        }
    }

    public String generateMaBaoHanh() {
        String sql = "SELECT MaBH FROM BaoHanh ORDER BY CAST(SUBSTRING(MaBH, 3) AS UNSIGNED) DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("MaBH");
                int num = Integer.parseInt(lastMa.substring(2));
                return String.format("BH%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "BH000";
    }

    public ArrayList<PhieuXuat> filterSQL(Timestamp from, Timestamp to, Double min, Double max) {
        ArrayList<PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuXuat WHERE NgayXuat BETWEEN ? AND ? AND TongTien BETWEEN ? AND ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setTimestamp(1, from != null ? from : Timestamp.valueOf("1970-01-01 00:00:00"));
            pst.setTimestamp(2, to != null ? to : new Timestamp(System.currentTimeMillis()));
            pst.setDouble(3, min);
            pst.setDouble(4, max);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getDiscountPercentage(String maPX) {
        String sql = "SELECT km.PhanTramGiam FROM PhieuXuat px " +
                "JOIN KhuyenMai km ON px.MaKM = km.MaKM " +
                "WHERE px.MaPhieuXuat = ?";

        try (Connection conn = Backend.DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPX);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("PhanTramGiam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTenKhachHang(String maKH) {
        String sql = "SELECT HoTen FROM ConNguoi WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("HoTen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Khách vãng lai";
    }
}
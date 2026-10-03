package Backend.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import Backend.DTO.DoiTra;
import Backend.DatabaseHelper;

public class DoiTraDAO {

    public ArrayList<DoiTra> selectAll() {
        ArrayList<DoiTra> list = new ArrayList<>();
        String sql = """
            SELECT dt.*, px.NgayXuat, cn.HoTen 
            FROM DoiTra dt
            JOIN PhieuXuat px ON dt.MaPhieuXuat = px.MaPhieuXuat
            JOIN KhachHang kh ON dt.MaKH = kh.ID
            JOIN ConNguoi cn ON kh.ID = cn.ID
            WHERE dt.TrangThai = 1
        """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DoiTra dt = new DoiTra(
                    rs.getString("MaDoiTra"),
                    rs.getString("MaKH"),
                    rs.getString("MaPhieuXuat"),
                    rs.getString("MaImei"),
                    rs.getDate("NgayDoiTra").toLocalDate(),
                    rs.getString("LyDo"),
                    rs.getBoolean("TrangThai")
                );
                dt.setTenKH(rs.getString("HoTen"));
                LocalDate ngayMua = rs.getDate("NgayXuat").toLocalDate();
                dt.setNgayMua(ngayMua);
                // ngayHetHan sẽ auto calculate trong setNgayMua (ngayMua + 30)
                list.add(dt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(DoiTra dt) {
        String sql = "INSERT INTO DoiTra (MaDoiTra, MaKH, MaPhieuXuat, MaImei, NgayDoiTra, LyDo, TrangThai) VALUES (?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dt.getMaDoiTra());
            ps.setString(2, dt.getMaKH());
            ps.setString(3, dt.getMaPhieuXuat());
            ps.setString(4, dt.getMaImei());
            ps.setDate(5, Date.valueOf(dt.getNgayDoiTra()));
            ps.setString(6, dt.getLyDo());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(DoiTra dt) {
        // Lưu ý: Việc update IMEI trong phiếu đổi trả rất phức tạp vì liên quan đến kho.
    // Ở mức cơ bản, ta chỉ nên cho phép sửa Ngày và Lý do.
    String sql = "UPDATE DoiTra SET NgayDoiTra = ?, LyDo = ? WHERE MaDoiTra = ? AND TrangThai = 1";
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDate(1, Date.valueOf(dt.getNgayDoiTra()));
        ps.setString(2, dt.getLyDo());
        ps.setString(3, dt.getMaDoiTra());
        return ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

    public int softDelete(String maDT) {
        String sql = "UPDATE DoiTra SET TrangThai = 0 WHERE MaDoiTra = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maDT);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getImeiByMaDoiTra(String maDT) {
        String sql = "SELECT MaImei FROM DoiTra WHERE MaDoiTra = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maDT);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaImei");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean restoreImeiStatus(String imei) {
        // Restore máy trả về trạng thái 'Đã bán' 
        String sql = "UPDATE ChiTietSP SET TinhTrang = 'Đã bán' WHERE MaImei = ? AND TinhTrang = 'Máy lỗi đổi trả'";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, imei);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // public boolean updateImeiReturn(String maImei) {
    //     String sql = """
    //         UPDATE ChiTietSP 
    //         SET TinhTrang = 'Trong kho', 
    //             MaPhieuXuat = NULL 
    //         WHERE MaImei = ?
    //     """;
    //     try (Connection conn = DatabaseHelper.getConnection();
    //          PreparedStatement ps = conn.prepareStatement(sql)) {
            
    //         ps.setString(1, maImei);
    //         return ps.executeUpdate() > 0;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }

    public LocalDate getNgayMuaByMaPX(String maPX) {
        String sql = "SELECT NgayXuat FROM PhieuXuat WHERE MaPhieuXuat = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPX);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate("NgayXuat").toLocalDate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateOldImeiStatus(String imeiCu, String tinhTrangMoi) {
        // Chúng ta set MaPhieuXuat = NULL để máy này không còn thuộc về hóa đơn đó nữa
        String sql = "UPDATE ChiTietSP SET TinhTrang = ?, MaPhieuXuat = NULL WHERE MaImei = ?";
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tinhTrangMoi); // Ví dụ: "Hàng lỗi chờ xử lý" hoặc "Đã đổi trả"
            ps.setString(2, imeiCu);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean assignNewImeiToInvoice(String imeiMoi, String maPX) {
        // Chuyển trạng thái máy mới từ 'Trong kho' sang 'Đã bán' và gán vào phiếu xuất
        String sql = "UPDATE ChiTietSP SET TinhTrang = 'Đã bán', MaPhieuXuat = ? WHERE MaImei = ?";
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPX);
            ps.setString(2, imeiMoi);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if IMEI belongs to the invoice (PhieuXuat)
    public boolean checkImeiInvoice(String imei, String maPX) {
        String sql = "SELECT COUNT(*) FROM ChiTietSP WHERE MaImei = ? AND MaPhieuXuat = ?";
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, imei);
            ps.setString(2, maPX);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if IMEI exists in kho (Trong kho)
    public boolean checkImeiInStock(String imei) {
        String sql = "SELECT COUNT(*) FROM ChiTietSP WHERE MaImei = ? AND TinhTrang = 'Trong kho'";
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, imei);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get product detail info from IMEI
    public java.util.HashMap<String, String> getProductInfoByImei(String imei) {
        java.util.HashMap<String, String> result = new java.util.HashMap<>();
        String sql = """
            SELECT 
                sp.TenSP,
                sp.HinhAnh,
                pb.MauSac,
                pb.CongSuat,
                pb.GiaBan,
                ct.TinhTrang
            FROM ChiTietSP ct
            JOIN PhienBanSP pb ON ct.MaPhienBan = pb.MaPhienBan
            JOIN SanPham sp ON pb.MaSP = sp.MaSP
            WHERE ct.MaImei = ?
            """;
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, imei);
            
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result.put("tenSP", rs.getString("TenSP") != null ? rs.getString("TenSP") : "N/A");
                result.put("hinhAnh", rs.getString("HinhAnh") != null ? rs.getString("HinhAnh") : "");
                result.put("mauSac", rs.getString("MauSac") != null ? rs.getString("MauSac") : "N/A");
                result.put("congSuat", rs.getString("CongSuat") != null ? rs.getString("CongSuat") : "N/A");
                result.put("giaBan", String.valueOf(rs.getDouble("GiaBan")));
                result.put("tinhTrang", rs.getString("TinhTrang") != null ? rs.getString("TinhTrang") : "N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    // Lấy mã DoiTra từ IMEI (nếu IMEI này đã được đổi)
    public String getMaDoiTraByImei(String imei) {
        String sql = "SELECT MaDoiTra FROM DoiTra WHERE MaImei = ? AND TrangThai = 1 LIMIT 1";
        try (java.sql.Connection conn = Backend.DatabaseHelper.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, imei);
            
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaDoiTra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy mã phiên bản từ IMEI để biết máy đó là máy gì
    public String getMaPhienBanByImei(String imei) {
        String sql = "SELECT MaPhienBan FROM ChiTietSP WHERE MaImei = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, imei);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("MaPhienBan");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Cập nhật số lượng trong ChiTietPhieuXuat (Tăng/Giảm khi đổi hàng)
    public void updateQuantityInChiTietPX(String maPX, String maPB, int thayDoi) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            // 1. Kiểm tra xem phiên bản này đã có trong ChiTietPhieuXuat của hóa đơn này chưa
            String checkSql = "SELECT SoLuong, DonGia FROM ChiTietPhieuXuat WHERE MaPhieuXuat = ? AND MaPhienBan = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, maPX);
            checkPs.setString(2, maPB);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Đã tồn tại -> Cập nhật số lượng
                int currentQty = rs.getInt("SoLuong");
                double unitPrice = rs.getDouble("DonGia");
                int newQty = rs.getInt("SoLuong") + thayDoi;

                if (newQty > 0) {
                    String updateSql = "UPDATE ChiTietPhieuXuat SET SoLuong = ?, ThanhTien = ? WHERE MaPhieuXuat = ? AND MaPhienBan = ?";
                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setInt(1, newQty);
                    updatePs.setDouble(2, newQty * unitPrice);
                    updatePs.setString(3, maPX);
                    updatePs.setString(4, maPB);
                    updatePs.executeUpdate();
                } else {
                    // Nếu số lượng về 0 thì xóa luôn dòng này khỏi hóa đơn
                    String deleteSql = "DELETE FROM ChiTietPhieuXuat WHERE MaPhieuXuat = ? AND MaPhienBan = ?";
                    PreparedStatement deletePs = conn.prepareStatement(deleteSql);
                    deletePs.setString(1, maPX);
                    deletePs.setString(2, maPB);
                    deletePs.executeUpdate();
                }
            } else if (thayDoi > 0) {
                // Chưa tồn tại và là lệnh TĂNG -> Thêm dòng mới vào ChiTietPhieuXuat
                // Lấy đơn giá hiện tại từ bảng PhienBanSP
                String getPriceSql = "SELECT GiaBan FROM PhienBanSP WHERE MaPhienBan = ?";
                PreparedStatement getPricePs = conn.prepareStatement(getPriceSql);
                getPricePs.setString(1, maPB);
                ResultSet rsPrice = getPricePs.executeQuery();
                
                if (rsPrice.next()) {
                    double giaBan = rsPrice.getDouble("GiaBan");
                    String insertSql = "INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaPhienBan, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertPs = conn.prepareStatement(insertSql);
                    insertPs.setString(1, maPX);
                    insertPs.setString(2, maPB);
                    insertPs.setInt(3, thayDoi);
                    insertPs.setDouble(4, giaBan);
                    insertPs.setDouble(5, thayDoi * giaBan);
                    insertPs.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
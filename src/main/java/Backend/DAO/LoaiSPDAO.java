package Backend.DAO;

import Backend.DTO.ThuocTinhSanPham.LoaiSP;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class LoaiSPDAO {
    public ArrayList<LoaiSP> selectAll() {
        ArrayList<LoaiSP> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSP";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new LoaiSP(rs.getString("MaLoai"), rs.getString("TenLoai")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

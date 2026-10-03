package Backend.BUS;

import Backend.DAO.NhaCungCapDAO;
import Backend.DTO.NhaCungCap;
import java.util.ArrayList;

public class NhaCungCapBUS {
    private final NhaCungCapDAO nccDAO = new NhaCungCapDAO();

    public ArrayList<NhaCungCap> getAllNhaCungCap() {
        return nccDAO.selectAll();
    }

    public String getNewMaNCC() {
        return nccDAO.generateMaNCC();
    }
 
    public boolean add(NhaCungCap ncc) {
        // Thêm kiểm tra mã NCC đã tồn tại hay chưa
        return nccDAO.insert(ncc) > 0;
    }

    public boolean update(NhaCungCap ncc) {
        return nccDAO.update(ncc) > 0;
    }

    public boolean delete(String id) {
        return nccDAO.delete(id) > 0;
    }

    public ArrayList<String> getMaSPByNCC(String maNCC) {
        return nccDAO.getMaSPByNCC(maNCC);
    }

    public boolean saveSanPhamCungCap(String maNCC, ArrayList<String> dsMaSP) {
        return nccDAO.updateSanPhamCungCap(maNCC, dsMaSP);
    }

    public String validate(NhaCungCap ncc, boolean isNew) {
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }

        if (ncc.getDiaChi() == null || ncc.getDiaChi().trim().isEmpty()) {
            return "Địa chỉ nhà cung cấp không được để trống!";
        }

        if (ncc.getSdt() == null || ncc.getSdt().trim().isEmpty()) {
            return "SĐT không được để trống!";
        }
        
        if (!ncc.getSdt().matches("^0\\d{9}$")) {
            return "SĐT phải có 10 chữ số và bắt đầu bằng số 0!";
        }
        ArrayList<NhaCungCap> list = getAllNhaCungCap();
        for (NhaCungCap oldNCC : list) {
            if (oldNCC.getSdt().equals(ncc.getSdt())) {
                if (isNew || !oldNCC.getMaNCC().equals(ncc.getMaNCC())) {
                    return "Số điện thoại này đã được sử dụng bởi nhà cung cấp khác!";
                }
            }
        }
        return "OK";
    }
}
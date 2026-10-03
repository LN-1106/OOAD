package Backend.BUS;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import Backend.DAO.ThongKeDAO;

public class ThongKeBUS {
    private final ThongKeDAO tkDAO = new ThongKeDAO();

    public double getDoanhThuNgay() { 
        return tkDAO.getDoanhThuNgay(); 
    }

    public int getDonHangMoiNgay() { 
        return tkDAO.getDonHangMoiNgay(); 
    }

    public double getVonNhapThang() { 
        return tkDAO.getVonNhapThang(); 
    }

    public double getLoiNhuanThang() { 
        return tkDAO.getLoiNhuanThang(); 
    }

    public void napDuLieuTop5(DefaultCategoryDataset dataset, String type) {
        tkDAO.getTop5SanPham(dataset, type);
    }

    public void napDuLieuTiTrong(DefaultPieDataset dataset, String type) {
        tkDAO.getTiTrongDoanhThuLoai(dataset, type);
    }

    public void napDuLieuXuHuong(DefaultCategoryDataset dataset) {
        tkDAO.getXuHuongTrongThang(dataset);
    }
}

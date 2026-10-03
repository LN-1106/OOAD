package Backend.DTO.ThongKe;

public class ThongKeTungNgayTrongThang {
    private String ngay; // Định dạng dd/MM để hiển thị trên trục X
    private int soDonHang;
    private int soKhachHang;
    private double doanhThuNgay;

    public ThongKeTungNgayTrongThang() {}

    public ThongKeTungNgayTrongThang(String ngay, int soDonHang, int soKhachHang, double doanhThuNgay) {
        this.ngay = ngay;
        this.soDonHang = soDonHang;
        this.soKhachHang = soKhachHang;
        this.doanhThuNgay = doanhThuNgay;
    }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }

    public int getSoKhachHang() { return soKhachHang; }
    public void setSoKhachHang(int soKhachHang) { this.soKhachHang = soKhachHang; }

    public double getDoanhThuNgay() { return doanhThuNgay; }
    public void setDoanhThuNgay(double doanhThuNgay) { this.doanhThuNgay = doanhThuNgay; }
}

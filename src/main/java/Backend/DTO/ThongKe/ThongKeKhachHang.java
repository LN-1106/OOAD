package Backend.DTO.ThongKe;

public class ThongKeKhachHang {
    private String maKH;
    private String hoTen;
    private int soDonHang;
    private double tongChiTieu;

    public ThongKeKhachHang() {}

    public ThongKeKhachHang(String maKH, String hoTen, int soDonHang, double tongChiTieu) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soDonHang = soDonHang;
        this.tongChiTieu = tongChiTieu;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }

    public double getTongChiTieu() { return tongChiTieu; }
    public void setTongChiTieu(double tongChiTieu) { this.tongChiTieu = tongChiTieu; }
}

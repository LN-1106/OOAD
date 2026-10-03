package Backend.DTO.ThongKe;

public class ThongKeSanPham {
    private String maSP;
    private String tenSP;
    private int soLuongBan;
    private double doanhThu;

    public ThongKeSanPham() {}

    public ThongKeSanPham(String maSP, String tenSP, int soLuongBan, double doanhThu) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
    }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public int getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(int soLuongBan) { this.soLuongBan = soLuongBan; }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }
}

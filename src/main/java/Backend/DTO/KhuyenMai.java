package Backend.DTO;

import java.util.Date;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private double phanTramGiam;
    private Date ngayBD;
    private Date ngayKT;
    private int trangThai; // 1: Đang hoạt động, 0: Ngừng hoạt động

    public KhuyenMai() {}

    public KhuyenMai(String maKM, String tenKM, double phanTramGiam, Date ngayBD, Date ngayKT, int trangThai) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.phanTramGiam = phanTramGiam;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
    }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }
    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }
    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double phanTramGiam) { this.phanTramGiam = phanTramGiam; }
    public Date getNgayBD() { return ngayBD; }
    public void setNgayBD(Date ngayBD) { this.ngayBD = ngayBD; }
    public Date getNgayKT() { return ngayKT; }
    public void setNgayKT(Date ngayKT) { this.ngayKT = ngayKT; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}

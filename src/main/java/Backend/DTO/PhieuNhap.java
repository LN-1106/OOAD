package Backend.DTO;

import java.sql.Timestamp;
import java.util.Objects;
public class PhieuNhap {
    private String maPhieuNhap;
    private Timestamp ngayNhap;
    private String maNV;
    private String maNCC;
    private double tongTien;
    private boolean trangThai;

    public PhieuNhap() {
    }
    
    public PhieuNhap(String maPhieuNhap, Timestamp ngayNhap, String maNV, String maNCC, double tongTien, boolean trangThai) {
        this.maPhieuNhap = maPhieuNhap;
        this.ngayNhap = ngayNhap;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public String getmaPhieuNhap() {
        return maPhieuNhap;
    }
    public void setmaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }
    public Timestamp getngayNhap() {
        return ngayNhap;
    }
    public void setngayNhap(Timestamp ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    public String getmaNV() {
        return maNV;
    }
    public void setmaNV(String maNV) {
        this.maNV = maNV;
    }
    public String getmaNCC() {
        return maNCC;
    }
    public void setmaNCC(String maNCC) {
        this.maNCC = maNCC;
    }
    public double getTongTien() {
        return tongTien;
    }
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    public boolean isTrangThai() {
        return trangThai;
    }
    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        PhieuNhap that = (PhieuNhap) obj;
        return Objects.equals(maPhieuNhap, that.maPhieuNhap);
    }

    @Override
    public String toString() {
        return "PhieuNhap [maPhieuNhap=" + maPhieuNhap + ", ngayNhap=" + ngayNhap + ", maNV=" + maNV + ", maNCC=" + maNCC
                + ", TongTien=" + tongTien + ", TrangThai=" + trangThai + "]";
    }
}

package Backend.DTO;

import java.sql.Timestamp;
import java.util.Objects;

public class PhieuXuat {
    private String maPhieuXuat; 
    private Timestamp ngayXuat;
    private String maNV;
    private String maKH;
    private String maKM;
    private double tongTien;
    private boolean trangThai; 

    public PhieuXuat() {
    }

    public PhieuXuat(String maPhieuXuat, Timestamp ngayXuat, String maNV, String maKH, String maKM, double tongTien, boolean trangThai) {
        this.maPhieuXuat = maPhieuXuat;
        this.ngayXuat = ngayXuat;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public String getMaPhieuXuat() { return maPhieuXuat; }
    public void setMaPhieuXuat(String maPhieuXuat) { this.maPhieuXuat = maPhieuXuat; }

    public Timestamp getNgayXuat() { return ngayXuat; }
    public void setNgayXuat(Timestamp ngayXuat) { this.ngayXuat = ngayXuat; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public boolean getTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhieuXuat other = (PhieuXuat) obj;
        return Objects.equals(this.maPhieuXuat, other.maPhieuXuat);
    }

    @Override
    public String toString() {
        return "PhieuXuat{" + "maPhieuXuat=" + maPhieuXuat + ", tongTien=" + tongTien + ", trangThai=" + trangThai + '}';
    }
}
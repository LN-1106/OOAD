package Backend.DTO;

import java.time.LocalDate;
import java.util.Objects;

public class BaoHanh {
    private String maBH;
    private String maImei;
    private String maPhieuXuat;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String tenSP;
    private String tinhTrang;

    public BaoHanh() {
    }

    public BaoHanh(String maBH, String maImei, String maPhieuXuat, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.maBH = maBH;
        this.maImei = maImei;
        this.maPhieuXuat = maPhieuXuat;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMaBH() {
        return maBH;
    }

    public void setMaBH(String maBH) {
        this.maBH = maBH;
    }

    public String getMaImei() {
        return maImei;
    }

    public void setMaImei(String maImei) {
        this.maImei = maImei;
    }

    public String getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(String maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BaoHanh that = (BaoHanh) obj;
        return Objects.equals(maBH, that.maBH);
    }

    @Override
    public String toString() {
        return "BaoHanh{" +
                "maBH='" + maBH + '\'' +
                ", maImei='" + maImei + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }
}
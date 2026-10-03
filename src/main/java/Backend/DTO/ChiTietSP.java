package Backend.DTO;

import java.util.Objects;

public class ChiTietSP {
    private String maImei;
    private String maPhienBan;
    private String maPhieuNhap;
    private String maPhieuXuat;
    private String tinhTrang;
    private boolean trangThai;

    public ChiTietSP() {
    }

    public ChiTietSP(String maImei, String maPhienBan, String maPhieuNhap, String maPhieuXuat, String tinhTrang,boolean trangThai) {
        this.maImei = maImei;
        this.maPhienBan = maPhienBan;
        this.maPhieuNhap = maPhieuNhap;
        this.maPhieuXuat = maPhieuXuat;
        this.tinhTrang = tinhTrang;
        this.trangThai=trangThai;
    }

    public String getMaImei() { return maImei; }
    public void setMaImei(String maImei) { this.maImei = maImei; }

    public String getMaPhienBan() { return maPhienBan; }
    public void setMaPhienBan(String maPhienBan) { this.maPhienBan = maPhienBan; }

    public String getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(String maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public String getMaPhieuXuat() { return maPhieuXuat; }
    public void setMaPhieuXuat(String maPhieuXuat) { this.maPhieuXuat = maPhieuXuat; }

    public String getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }

    public boolean isTrangThai() {return trangThai;}
    public void setTrangThai(boolean trangThai) {this.trangThai = trangThai;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChiTietSP that = (ChiTietSP) obj;
        return Objects.equals(maImei, that.maImei);
    }

    @Override
    public String toString() {
        return "ChiTietSP{" +
                "maImei='" + maImei + '\'' +
                ", maPhienBan='" + maPhienBan + '\'' +
                ", maPhieuNhap='" + maPhieuNhap + '\'' +
                ", maPhieuXuat='" + maPhieuXuat + '\'' +
                ", tinhTrang='" + tinhTrang + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}

package Backend.DTO;

import java.util.Objects;

public class ChiTietPhieuNhap {
    private String maPhieuNhap;
    private String maPhienBan;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String tenSP;
    private String hinhAnh;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(String maPhieuNhap, String maPhienBan, int soLuong, double donGia, double thanhTien) {
        this.maPhieuNhap = maPhieuNhap;
        this.maPhienBan = maPhienBan;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(String maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public String getMaPhienBan() { return maPhienBan; }
    public void setMaPhienBan(String maPhienBan) { this.maPhienBan = maPhienBan; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        ChiTietPhieuNhap that = (ChiTietPhieuNhap) obj;
        return Objects.equals(maPhieuNhap, that.maPhieuNhap) &&
               Objects.equals(maPhienBan, that.maPhienBan);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhapDTO{" +
                "maPhieuNhap='" + maPhieuNhap + '\'' +
                ", maPhienBan='" + maPhienBan + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}

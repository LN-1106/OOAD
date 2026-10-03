package Backend.DTO;

import java.util.Objects;

public class PhienBanSanPham {
    private String maPhienBan;
    private String maSP;
    private String mauSac;
    private String congSuat;
    private String pin;
    private String ketNoi;
    private double giaNhap;
    private double giaBan;
    private int soLuongTon;
    private String tenSP;
    private boolean trangThai;
    private String hinhAnh;

    public PhienBanSanPham() {
    }

    public PhienBanSanPham(String maPhienBan, String maSP, String mauSac,
            String congSuat, String pin, String ketNoi,
            double giaNhap, double giaBan, int soLuongTon, boolean trangThai, String hinhAnh) {
        this.maPhienBan = maPhienBan;
        this.maSP = maSP;
        this.mauSac = mauSac;
        this.congSuat = congSuat;
        this.pin = pin;
        this.ketNoi = ketNoi;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.trangThai = trangThai;
        this.hinhAnh = hinhAnh;
    }

    // Getters & Setters
    public String getMaPhienBan() {
        return maPhienBan;
    }

    public void setMaPhienBan(String maPhienBan) {
        this.maPhienBan = maPhienBan;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getCongSuat() {
        return congSuat;
    }

    public void setCongSuat(String congSuat) {
        this.congSuat = congSuat;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getKetNoi() {
        return ketNoi;
    }

    public void setKetNoi(String ketNoi) {
        this.ketNoi = ketNoi;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PhienBanSanPham that = (PhienBanSanPham) obj;
        return Objects.equals(maPhienBan, that.maPhienBan);
    }

    @Override
    public String toString() {
        return "PhienBanSPDTO{" +
                "maPhienBan='" + maPhienBan + '\'' +
                ", maSP='" + maSP + '\'' +
                ", mauSac='" + mauSac + '\'' +
                ", giaNhap=" + giaNhap +
                ", giaBan=" + giaBan +
                ", soLuongTon=" + soLuongTon +
                ", trangThai=" + trangThai +
                '}';
    }
}

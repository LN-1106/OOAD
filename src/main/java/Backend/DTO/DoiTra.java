package Backend.DTO;

import java.time.LocalDate;

public class DoiTra {
    private String maDoiTra;
    private String maKH;
    private String maPhieuXuat;
    private String maImei;
    private LocalDate ngayDoiTra;
    private String lyDo;
    private boolean trangThai;

    private String tenKH;
    private LocalDate ngayMua;
    private LocalDate ngayHetHan;

    public DoiTra() {
    }

    public DoiTra(String maDoiTra, String maKH, String maPhieuXuat, String maImei, LocalDate ngayDoiTra, String lyDo, boolean trangThai) {
        this.maDoiTra = maDoiTra;
        this.maKH = maKH;
        this.maPhieuXuat = maPhieuXuat;
        this.maImei = maImei;
        this.ngayDoiTra = ngayDoiTra;
        this.lyDo = lyDo;
        this.trangThai = trangThai;
    }

    public String getMaDoiTra() {
        return maDoiTra;
    }

    public void setMaDoiTra(String maDoiTra) {
        this.maDoiTra = maDoiTra;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(String maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public String getMaImei() {
        return maImei;
    }

    public void setMaImei(String maImei) {
        this.maImei = maImei;
    }

    public LocalDate getNgayDoiTra() {
        return ngayDoiTra;
    }

    public void setNgayDoiTra(LocalDate ngayDoiTra) {
        this.ngayDoiTra = ngayDoiTra;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public LocalDate getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(LocalDate ngayMua) {
        this.ngayMua = ngayMua;
        if (ngayMua != null) {
            this.ngayHetHan = ngayMua.plusDays(30);
        }
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }
}
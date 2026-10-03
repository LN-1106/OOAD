package Backend.DTO.ThuocTinhSanPham;

import java.util.Objects;

public class HangSX {
    private String maHang;
    private String tenHang;
    private String quocGia;

    public HangSX() {
    }

    public HangSX(String maHang, String tenHang, String quocGia) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.quocGia = quocGia;
    }

    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public String getQuocGia() { return quocGia; }
    public void setQuocGia(String quocGia) { this.quocGia = quocGia; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HangSX that = (HangSX) obj;
        return Objects.equals(maHang, that.maHang);
    }

    @Override
    public String toString() {
        return "HangSX{" +
                "maHang='" + maHang + '\'' +
                ", tenHang='" + tenHang + '\'' +
                ", quocGia='" + quocGia + '\'' +
                '}';
    }
}

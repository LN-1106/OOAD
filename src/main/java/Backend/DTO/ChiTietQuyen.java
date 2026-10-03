package Backend.DTO;

import java.util.Objects;

public class ChiTietQuyen {
    private String maNhomQuyen;
    private String maChucNang;
    private String hanhDong;

    public ChiTietQuyen() {
    }

    public ChiTietQuyen(String maNhomQuyen, String maChucNang, String hanhDong) {
        this.maNhomQuyen = maNhomQuyen;
        this.maChucNang = maChucNang;
        this.hanhDong = hanhDong;
    }

    public String getMaNhomQuyen() { return maNhomQuyen; }
    public void setMaNhomQuyen(String maNhomQuyen) { this.maNhomQuyen = maNhomQuyen; }

    public String getMaChucNang() { return maChucNang; }
    public void setMaChucNang(String maChucNang) { this.maChucNang = maChucNang; }

    public String getHanhDong() { return hanhDong; }
    public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChiTietQuyen that = (ChiTietQuyen) obj;
        return Objects.equals(maNhomQuyen, that.maNhomQuyen) &&
               Objects.equals(maChucNang, that.maChucNang) &&
               Objects.equals(hanhDong, that.hanhDong);
    }

    @Override
    public String toString() {
        return "ChiTietQuyen{" +
                "maNhomQuyen='" + maNhomQuyen + '\'' +
                ", maChucNang='" + maChucNang + '\'' +
                ", hanhDong='" + hanhDong + '\'' +
                '}';
    }
}

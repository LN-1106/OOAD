package Backend.DTO;

import java.util.Objects;

public class NhomQuyen {
    private String maNhomQuyen;
    private String tenNhomQuyen;
    private String moTa;

    public NhomQuyen() {
    }

    public NhomQuyen(String maNhomQuyen, String tenNhomQuyen, String moTa) {
        this.maNhomQuyen = maNhomQuyen;
        this.tenNhomQuyen = tenNhomQuyen;
        this.moTa = moTa;
    }

    public String getMaNhomQuyen() { return maNhomQuyen; }
    public void setMaNhomQuyen(String maNhomQuyen) { this.maNhomQuyen = maNhomQuyen; }

    public String getTenNhomQuyen() { return tenNhomQuyen; }
    public void setTenNhomQuyen(String tenNhomQuyen) { this.tenNhomQuyen = tenNhomQuyen; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhomQuyen that = (NhomQuyen) obj;
        return Objects.equals(maNhomQuyen, that.maNhomQuyen);
    }

    @Override
    public String toString() {
        return "NhomQuyen{" +
                "maNhomQuyen='" + maNhomQuyen + '\'' +
                ", tenNhomQuyen='" + tenNhomQuyen + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}

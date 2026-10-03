package Backend.DTO;

import java.util.Objects;

public class ChucNang {
    private String maChucNang;
    private String tenChucNang;
    private String moTa;

    public ChucNang() {
    }

    public ChucNang(String maChucNang, String tenChucNang, String moTa) {
        this.maChucNang = maChucNang;
        this.tenChucNang = tenChucNang;
        this.moTa = moTa;
    }

    public String getMaChucNang() { return maChucNang; }
    public void setMaChucNang(String maChucNang) { this.maChucNang = maChucNang; }

    public String getTenChucNang() { return tenChucNang; }
    public void setTenChucNang(String tenChucNang) { this.tenChucNang = tenChucNang; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChucNang that = (ChucNang) obj;
        return Objects.equals(maChucNang, that.maChucNang);
    }

    @Override
    public String toString() {
        return "ChucNang{" +
                "maChucNang='" + maChucNang + '\'' +
                ", tenChucNang='" + tenChucNang + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}

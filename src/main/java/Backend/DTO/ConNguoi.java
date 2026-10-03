package Backend.DTO;

import java.util.Objects;

public class ConNguoi {
    private String id;
    private String hoTen;
    private String sdt;
    private String diaChi;

    public ConNguoi() {
    }

    public ConNguoi(String id, String hoTen, String sdt, String diaChi) {
        this.id = id;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConNguoi that = (ConNguoi) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "ConNguoi{" +
                "id='" + id + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", sdt='" + sdt + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}

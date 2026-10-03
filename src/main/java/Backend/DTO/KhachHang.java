package Backend.DTO;

import java.util.Objects;

public class KhachHang extends ConNguoi {
    private boolean trangThai;

    public KhachHang() {
        super();
    }

    public KhachHang(String id, String hoTen, String sdt, String diaChi, boolean trangThai) {
        super(id, hoTen, sdt, diaChi);
        this.trangThai = trangThai;
    }

    public KhachHang(String id, String hoTen, String sdt, String diaChi) {
        super(id, hoTen, sdt, diaChi);
        this.trangThai = true;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KhachHang that = (KhachHang) obj;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "id='" + getId() + '\'' +
                ", hoTen='" + getHoTen() + '\'' +
                ", sdt='" + getSdt() + '\'' +
                ", diaChi='" + getDiaChi() + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}

package Backend.DTO;

import java.util.Objects;

public class NhanVien extends ConNguoi {
    private String chucVu;
    private String email;
    private double luong;
    private boolean trangThai;

    public NhanVien() {
        super();
    }

    public NhanVien(String id, String hoTen, String sdt, String diaChi, String chucVu, String email, double luong, boolean trangThai) {
        super(id, hoTen, sdt, diaChi);
        this.chucVu = chucVu;
        this.email = email;
        this.luong = luong;
        this.trangThai = trangThai;
    }
    public NhanVien(String id, String hoTen, String sdt, String diaChi, String chucVu, String email, double luong) {
        super(id, hoTen, sdt, diaChi);
        this.chucVu =chucVu;
        this.email = email;
        this.luong = luong;
        this.trangThai = true;
    }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhanVien that = (NhanVien) obj;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id='" + getId() + '\'' +
                ", hoTen='" + getHoTen() + '\'' +
                ", sdt='" + getSdt() + '\'' +
                ", diaChi='" + getDiaChi() + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", email='" + email + '\'' +
                ", luong=" + luong +
                ", trangThai=" + trangThai +
                '}';
    }
}

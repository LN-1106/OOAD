package Backend.DTO.ThuocTinhSanPham;

import java.util.Objects;

public class LoaiSP {
    private String maLoai;
    private String tenLoai;

    public LoaiSP() {
    }

    public LoaiSP(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoaiSP that = (LoaiSP) obj;
        return Objects.equals(maLoai, that.maLoai);
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}

package Backend.DTO;

import java.util.Objects;

public class ChiTietBaoHanh {
    private String maCTBH;
    private String maBH;
    private String tenSP;
    private String noiDung;
    private String tinhTrang;

    public ChiTietBaoHanh() {
    }

    public ChiTietBaoHanh(String maCTBH, String maBH, String tenSP, String noiDung, String tinhTrang) {
        this.maCTBH = maCTBH;
        this.maBH = maBH;
        this.tenSP = tenSP;
        this.noiDung = noiDung;
        this.tinhTrang = tinhTrang;
    }

    public String getMaCTBH() { return maCTBH; }
    public void setMaCTBH(String maCTBH) { this.maCTBH = maCTBH; }

    public String getMaBH() { return maBH; }
    public void setMaBH(String maBH) { this.maBH = maBH; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChiTietBaoHanh that = (ChiTietBaoHanh) obj;
        return Objects.equals(maCTBH, that.maCTBH);
    }

    @Override
    public String toString() {
        return "ChiTietBaoHanh{" +
                "maCTBH='" + maCTBH + '\'' +
                ", maBH='" + maBH + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }
}

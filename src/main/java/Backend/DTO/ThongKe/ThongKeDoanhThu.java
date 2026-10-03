package Backend.DTO.ThongKe;

public class ThongKeDoanhThu {
    private double doanhThu;
    private double von;
    private double loiNhuan;
    private int soDonHang;

    public ThongKeDoanhThu() {}

    public ThongKeDoanhThu(double doanhThu, double von, double loiNhuan, int soDonHang) {
        this.doanhThu = doanhThu;
        this.von = von;
        this.loiNhuan = loiNhuan;
        this.soDonHang = soDonHang;
    }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

    public double getVon() { return von; }
    public void setVon(double von) { this.von = von; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }

    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }
}

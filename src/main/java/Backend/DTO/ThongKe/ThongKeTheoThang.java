package Backend.DTO.ThongKe;

public class ThongKeTheoThang {
    private int thang;
    private int nam;
    private double doanhThu;
    private double chiPhi;
    private double loiNhuan;

    public ThongKeTheoThang() {}

    public ThongKeTheoThang(int thang, int nam, double doanhThu, double chiPhi, double loiNhuan) {
        this.thang = thang;
        this.nam = nam;
        this.doanhThu = doanhThu;
        this.chiPhi = chiPhi;
        this.loiNhuan = loiNhuan;
    }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

    public double getChiPhi() { return chiPhi; }
    public void setChiPhi(double chiPhi) { this.chiPhi = chiPhi; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }
}

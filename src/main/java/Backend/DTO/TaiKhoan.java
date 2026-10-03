package Backend.DTO;

public class TaiKhoan {
    private String username;
    private String password;
    private String maNV;
    private String maNhomQuyen;
    private int status;
    private String conNguoiId; // Giữ lại nếu bạn vẫn cần dùng từ code cũ

    public TaiKhoan() {
    }

    // Getter và Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaNhomQuyen() { return maNhomQuyen; }
    public void setMaNhomQuyen(String maNhomQuyen) { this.maNhomQuyen = maNhomQuyen; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getConNguoiId() { return conNguoiId; }
    public void setConNguoiId(String conNguoiId) { this.conNguoiId = conNguoiId; }
}

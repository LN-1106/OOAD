CREATE DATABASE IF NOT EXISTS quanlyamthanh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE quanlyamthanh;

-- --------------------------------------------------------

--
-- Nhóm Con Người (Kế thừa)
--


CREATE TABLE ConNguoi (
    ID VARCHAR(20) PRIMARY KEY, -- Định dạng: NV001 hoặc KH001
    HoTen VARCHAR(40) NOT NULL,
    SDT VARCHAR(20),
    DiaChi VARCHAR(100)
);

-- --------------------------------------------------------

CREATE TABLE NhanVien (
    ID VARCHAR(20) PRIMARY KEY,
    ChucVu VARCHAR(50),
    Email VARCHAR(100),
    Luong DOUBLE,
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID) REFERENCES ConNguoi(ID)
);

-- --------------------------------------------------------

CREATE TABLE KhachHang (
    ID VARCHAR(20) PRIMARY KEY,
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (ID) REFERENCES ConNguoi(ID)
);

-- --------------------------------------------------------

--
-- Tài khoản
--

CREATE TABLE NhomQuyen (
    MaNhomQuyen VARCHAR(20) PRIMARY KEY,
    TenNhomQuyen VARCHAR(100),
    MoTa TEXT
);

CREATE TABLE TaiKhoan (
    TenDangNhap VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(255) NOT NULL,  -- Nên mã hóa BCrypt
    MaNV VARCHAR(20) UNIQUE,
    MaNhomQuyen VARCHAR(20),
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(ID),
    FOREIGN KEY (MaNhomQuyen) REFERENCES NhomQuyen(MaNhomQuyen)
);

CREATE TABLE ChucNang (
    MaChucNang VARCHAR(50) PRIMARY KEY,
    TenChucNang VARCHAR(100) NOT NULL,
    MoTa TEXT
);

CREATE TABLE ChiTietQuyen (
    MaNhomQuyen VARCHAR(20),
    MaChucNang VARCHAR(50),
    HanhDong VARCHAR(20),  -- create, read, update, delete
    PRIMARY KEY (MaNhomQuyen, MaChucNang, HanhDong),
    FOREIGN KEY (MaNhomQuyen) REFERENCES NhomQuyen(MaNhomQuyen),
    FOREIGN KEY (MaChucNang) REFERENCES ChucNang(MaChucNang)
);

-- --------------------------------------------------------


--
-- Sản phẩm & Thuộc tính
--

CREATE TABLE LoaiSP (
    MaLoai VARCHAR(20) PRIMARY KEY,
    TenLoai VARCHAR(50) NOT NULL -- Loa, Tai nghe, Amply, Micro
);

CREATE TABLE HangSX (
    MaHang VARCHAR(20) PRIMARY KEY,
    TenHang VARCHAR(50) NOT NULL,
    QuocGia VARCHAR(50)
);

CREATE TABLE SanPham (
    MaSP VARCHAR(20) PRIMARY KEY,
    TenSP VARCHAR(100) NOT NULL,
    MaLoai VARCHAR(20),
    MaHang VARCHAR(20),
    MoTa TEXT,
    ThoiGianBaoHanh INT DEFAULT 12, -- Tháng
    TrangThai BOOLEAN DEFAULT TRUE,
    HinhAnh VARCHAR(255),
    FOREIGN KEY (MaLoai) REFERENCES LoaiSP(MaLoai),
    FOREIGN KEY (MaHang) REFERENCES HangSX(MaHang)
);

--
-- Phiên bản sản phẩm
--

CREATE TABLE PhienBanSP (
    MaPhienBan VARCHAR(20) PRIMARY KEY,
    MaSP VARCHAR(20),
    MauSac VARCHAR(50),
    CongSuat VARCHAR(50),     -- VD: 80W, 40W
    Pin VARCHAR(50),          -- VD: 30h, 20h
    KetNoi VARCHAR(100),      -- VD: Bluetooth 5.2
    GiaNhap DOUBLE,
    GiaBan DOUBLE,
    SoLuongTon INT DEFAULT 0,
    TrangThai BOOLEAN DEFAULT TRUE,
    HinhAnh VARCHAR(255),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

--
-- Quản lý imei (Từng chiếc) 

CREATE TABLE ChiTietSP (
    MaImei VARCHAR(50) PRIMARY KEY, -- MS1-001, SN2-001
    MaPhienBan VARCHAR(20),
    MaPhieuNhap VARCHAR(20),
    MaPhieuXuat VARCHAR(20),
    TinhTrang VARCHAR(50) DEFAULT 'Trong kho', -- Đã bán, Bảo hành
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (MaPhienBan) REFERENCES PhienBanSP(MaPhienBan)
);

-- --------------------------------------------------------

--
-- Nhập hàng
-- 

CREATE TABLE NhaCungCap ( 
    MaNCC VARCHAR(20) PRIMARY KEY, 
    TenNCC VARCHAR(100), 
    DiaChi VARCHAR(255), 
    Sdt VARCHAR(20),
    TrangThai BOOLEAN DEFAULT TRUE
);

CREATE TABLE NCC_SanPham (
    MaNCC VARCHAR(20),
    MaSP VARCHAR(20),
    PRIMARY KEY (MaNCC, MaSP),
    FOREIGN KEY (MaNCC) REFERENCES NhaCungCap(MaNCC),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

CREATE TABLE PhieuNhap (
    MaPhieuNhap VARCHAR(20) PRIMARY KEY,
    NgayNhap DATETIME DEFAULT CURRENT_TIMESTAMP,
    MaNV VARCHAR(20),
    MaNCC VARCHAR(20),
    TongTien DOUBLE DEFAULT 0,
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(ID),
    FOREIGN KEY (MaNCC) REFERENCES NhaCungCap(MaNCC)
);

CREATE TABLE ChiTietPhieuNhap ( 
    MaPhieuNhap VARCHAR(20), 
    MaPhienBan VARCHAR(20) NOT NULL, 
    SoLuong INT,
    DonGia DOUBLE,
    ThanhTien DOUBLE,
    PRIMARY KEY (MaPhieuNhap, MaPhienBan),
    FOREIGN KEY (MaPhieuNhap) REFERENCES PhieuNhap(MaPhieuNhap),
    FOREIGN KEY (MaPhienBan) REFERENCES PhienBanSP(MaPhienBan)
);

-- --------------------------------------------------------

-- 
-- Bán hàng
-- 

CREATE TABLE KhuyenMai (
    MaKM VARCHAR(20)  PRIMARY KEY, 
    TenKM VARCHAR(100), 
    DieuKienGiam DOUBLE,  -- Giá trị đơn tối thiểu
    PhanTramGiam DOUBLE, 
    NgayBatDau DATE, 
    NgayKetThuc DATE,
    TrangThai BOOLEAN DEFAULT TRUE
);

CREATE TABLE PhieuXuat (
    MaPhieuXuat VARCHAR(20) PRIMARY KEY,
    NgayXuat DATETIME DEFAULT CURRENT_TIMESTAMP,
    MaNV VARCHAR(20),
    MaKH VARCHAR(20),
    MaKM VARCHAR(20),
    TongTien DOUBLE DEFAULT 0,
    TrangThai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(ID),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(ID),
    FOREIGN KEY (MaKM) REFERENCES KhuyenMai(MaKM)
);

CREATE TABLE ChiTietPhieuXuat ( 
    MaPhieuXuat VARCHAR(20),
    MaPhienBan VARCHAR(20),
    SoLuong INT,
    DonGia DOUBLE,
    ThanhTien DOUBLE,
    PRIMARY KEY (MaPhieuXuat, MaPhienBan),
    FOREIGN KEY (MaPhieuXuat) REFERENCES PhieuXuat(MaPhieuXuat),
    FOREIGN KEY (MaPhienBan) REFERENCES PhienBanSP(MaPhienBan)
);

-- --------------------------------------------------------

--
-- Bảo hành
--

CREATE TABLE BaoHanh ( 
    MaBH VARCHAR(20) PRIMARY KEY, 
    MaImei VARCHAR(50), 
    MaPhieuXuat VARCHAR(20), 
    NgayBatDau DATE DEFAULT (CURRENT_DATE),
    NgayKetThuc DATE,
    TinhTrang VARCHAR(50) DEFAULT 'Còn bảo hành',
    TrangThai BOOLEAN DEFAULT TRUE, 
    FOREIGN KEY (MaImei) REFERENCES ChiTietSP(MaImei),
    FOREIGN KEY (MaPhieuXuat) REFERENCES PhieuXuat(MaPhieuXuat)
);

CREATE TABLE ChiTietBaoHanh ( 
    MaCTBH VARCHAR(20) PRIMARY KEY, 
    MaBH VARCHAR(20), 
    NoiDung TEXT, 
    TinhTrang VARCHAR(50) DEFAULT 'Đang sửa chữa',
    FOREIGN KEY (MaBH) REFERENCES BaoHanh(MaBH) ON DELETE CASCADE
);

CREATE TABLE DoiTra (
MaDoiTra VARCHAR(20) PRIMARY KEY,
    MaKH VARCHAR(20),
    MaPhieuXuat VARCHAR(20),
    NgayDoiTra DATE,
    LyDo TEXT,
    TrangThai BOOLEAN DEFAULT TRUE, -- Xóa mềm
    -- Các trường này sẽ JOIN hoặc lấy từ logic để hiển thị
    CONSTRAINT FK_DoiTra_KhachHang FOREIGN KEY (MaKH) REFERENCES KhachHang(ID),
    CONSTRAINT FK_DoiTra_PhieuXuat FOREIGN KEY (MaPhieuXuat) REFERENCES PhieuXuat(MaPhieuXuat)
);

ALTER TABLE DoiTra ADD COLUMN MaImei VARCHAR(50) AFTER MaPhieuXuat;

ALTER TABLE DoiTra 
ADD CONSTRAINT FK_DoiTra_Imei 
FOREIGN KEY (MaImei) REFERENCES ChiTietSP(MaImei);

-- --------------------------------------------------------

-- 
-- THIẾT LẬP TRIGGER CẬP NHẬT TỒN KHO & TỔNG TIÊN
-- 

DELIMITER //

-- Tự động TĂNG tồn kho khi NHẬP hàng
CREATE TRIGGER trg_UpdateStockAfterImport
AFTER INSERT ON ChiTietPhieuNhap
FOR EACH ROW
BEGIN
    UPDATE PhienBanSP
    SET SoLuongTon = SoLuongTon + NEW.SoLuong 
    WHERE MaPhienBan = NEW.MaPhienBan;
END//

-- Tự động GIẢM tồn kho khi BÁN hàng
CREATE TRIGGER trg_UpdateStockAfterExport
AFTER INSERT ON ChiTietPhieuXuat
FOR EACH ROW
BEGIN
    UPDATE PhienBanSP 
    SET SoLuongTon = SoLuongTon - NEW.SoLuong 
    WHERE MaPhienBan = NEW.MaPhienBan;
END//


-- Tự động TỔNG TIỀN khi NHẬP hàng
CREATE TRIGGER trg_CalcThanhTienNhap
BEFORE INSERT ON ChiTietPhieuNhap
FOR EACH ROW
BEGIN
    SET NEW.ThanhTien = NEW.SoLuong * NEW.DonGia;
END//

CREATE TRIGGER trg_UpdateTongTienNhap
AFTER INSERT ON ChiTietPhieuNhap
FOR EACH ROW
BEGIN
    UPDATE PhieuNhap
    SET TongTien = (
        SELECT SUM(ThanhTien)
        FROM ChiTietPhieuNhap
        WHERE MaPhieuNhap = NEW.MaPhieuNhap
    )
    WHERE MaPhieuNhap = NEW.MaPhieuNhap;
END//

-- Tự động TỔNG TIỀN khi XUẤT hàng
CREATE TRIGGER trg_CalcThanhTienXuat
BEFORE INSERT ON ChiTietPhieuXuat
FOR EACH ROW
BEGIN
    SET NEW.ThanhTien = NEW.SoLuong * NEW.DonGia;
END//

CREATE TRIGGER trg_UpdateTongTienXuat
AFTER INSERT ON ChiTietPhieuXuat
FOR EACH ROW
BEGIN
    DECLARE v_phan_tram DOUBLE DEFAULT 0;

    -- Lấy % giảm giá từ bảng KhuyenMai dựa vào MaKM của phiếu xuất hiện tại
    SELECT COALESCE(km.PhanTramGiam, 0) INTO v_phan_tram
    FROM PhieuXuat px
    LEFT JOIN KhuyenMai km ON px.MaKM = km.MaKM
    WHERE px.MaPhieuXuat = NEW.MaPhieuXuat;

    -- Cập nhật lại tổng tiền = (Tổng giá gốc) * (1 - % giảm)
    UPDATE PhieuXuat
    SET TongTien = (
        SELECT SUM(ThanhTien) * (1 - v_phan_tram / 100)
        FROM ChiTietPhieuXuat
        WHERE MaPhieuXuat = NEW.MaPhieuXuat
    )
    WHERE MaPhieuXuat = NEW.MaPhieuXuat;
END//

DELIMITER ;

-- --------------------------------------------------------

-- 
-- CONSTRAINT & INDEX TỐI ƯU HÓA
-- 

-- -- CONSTRAINT ĐỂ ĐẢM BẢO DỮ LIỆU HỢP LỆ
ALTER TABLE PhienBanSP 
ADD CONSTRAINT chk_giaban CHECK (GiaBan > GiaNhap);

ALTER TABLE PhienBanSP 
ADD CONSTRAINT chk_soluong CHECK (SoLuongTon >= 0);

ALTER TABLE ChiTietPhieuNhap 
ADD CONSTRAINT chk_soluong_nhap CHECK (SoLuong > 0);

ALTER TABLE ChiTietPhieuXuat
ADD CONSTRAINT chk_soluong_xuat CHECK (SoLuong > 0);

ALTER TABLE KhuyenMai MODIFY COLUMN TrangThai INT DEFAULT 1;



-- INDEX ĐỂ TỐI ƯU HÓA 
CREATE INDEX idx_sp_ten ON SanPham(TenSP);
CREATE INDEX idx_pn_ngay ON PhieuNhap(NgayNhap);
CREATE INDEX idx_px_ngay ON PhieuXuat(NgayXuat);
CREATE INDEX idx_chitietsp_tinhtrang ON ChiTietSP(TinhTrang);
CREATE INDEX idx_connguoi_sdt ON ConNguoi(SDT);

-- --------------------------------------------------------


-- DỮ LIỆU
-- Bổ sung dữ liệu Chức năng để hiện lên bảng Phân quyền
INSERT IGNORE INTO ChucNang (MaChucNang, TenChucNang, MoTa) VALUES 
('BANHANG', 'Bán hàng', 'Quản lý giao dịch bán hàng'),
('NHAPHANG', 'Nhập hàng', 'Quản lý nhập kho sản phẩm'),
('SANPHAM', 'Sản phẩm', 'Quản lý thông tin và phiên bản sản phẩm'),
('KHACHHANG', 'Khách hàng', 'Quản lý thông tin khách hàng'),
('NHANVIEN', 'Nhân viên', 'Quản lý nhân sự và chức vụ'),
('NHACUNGCAP', 'Nhà cung cấp', 'Quản lý đối tác cung ứng'),
('PHIEUNHAP', 'Phiếu nhập', 'Quản lý lịch sử nhập hàng'),
('PHIEUXUAT', 'Phiếu xuất', 'Quản lý hóa đơn xuất hàng'),
('KHUYENMAI', 'Khuyến mãi', 'Quản lý chương trình giảm giá'),
('BAOHANH', 'Bảo hành', 'Quản lý thiết bị bảo hành'),
('THONGKE', 'Thống kê', 'Xem báo cáo doanh thu và tồn kho'),
('PHANQUYEN', 'Phân quyền', 'Thiết lập quyền hạn cho nhóm người dùng'),
('DOITRA', 'Đổi trả', 'Quản lý đổi trả sản phẩm');

INSERT INTO NhomQuyen VALUES 
('NQ01', 'Quản lý cửa hàng', 'Full quyền'),
('NQ02', 'Nhân viên bán hàng', 'Chỉ bán và xem kho'),
('NQ03', 'Nhân viên kho', 'Chỉ nhập hàng');

INSERT IGNORE INTO ChiTietQuyen (MaNhomQuyen, MaChucNang, HanhDong)
SELECT 'NQ01', MaChucNang, act
FROM ChucNang
CROSS JOIN (SELECT 'read' AS act UNION SELECT 'create' UNION SELECT 'update' UNION SELECT 'delete') AS actions;

INSERT INTO ChiTietQuyen (MaNhomQuyen, MaChucNang, HanhDong) VALUES 
('NQ02', 'BANHANG', 'read'), 
('NQ02', 'BANHANG', 'create'), 
('NQ02', 'BANHANG', 'update'),
('NQ02', 'PHIEUXUAT', 'read'), 
('NQ02', 'PHIEUXUAT', 'create'),
('NQ02', 'KHACHHANG', 'read'), 
('NQ02', 'KHACHHANG', 'create'), 
('NQ02', 'KHACHHANG', 'update'),
('NQ02', 'SANPHAM', 'read'), 
('NQ02', 'BAOHANH', 'read'), 
('NQ02', 'BAOHANH', 'create'),
('NQ03', 'NHAPHANG', 'read'), 
('NQ03', 'NHAPHANG', 'create'),
('NQ03', 'PHIEUNHAP', 'read'), 
('NQ03', 'PHIEUNHAP', 'create'),
('NQ03', 'SANPHAM', 'read'), 
('NQ03', 'SANPHAM', 'create'), 
('NQ03', 'SANPHAM', 'update'),
('NQ03', 'NHACUNGCAP', 'read'), 
('NQ03', 'NHACUNGCAP', 'create'),
('NQ03','DOITRA','read');

INSERT INTO ConNguoi (ID, HoTen, SDT, DiaChi) VALUES 
('NV001', 'Trương Phúc', '0909123456', 'Đà Nẵng'),
('NV002', 'Lê Văn Nam', '0909123457', 'Hà Nội'),
('NV003', 'Nguyễn Văn Kho', '0909999888', 'Hải Phòng'),
('KH001', 'Nguyễn Khách', '0912345678', 'TP.HCM'),
('KH002', 'Trần VIP', '0987654321', 'Cần Thơ');

INSERT INTO NhanVien (ID, ChucVu, Email, Luong) VALUES 
('NV001', 'Quản lý', 'phuc@sw.com', 20000000),
('NV002', 'Nhân viên bán hàng', 'nam@sw.com', 10000000),
('NV003', 'Nhân viên kho', 'kho@sw.com', 12000000);

INSERT INTO KhachHang (ID) VALUES 
('KH001'),
('KH002');

INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, MaNhomQuyen, TrangThai) VALUES 
('admin', '123456', 'NV001', 'NQ01', 1), 
('nhanvien', '123456', 'NV002', 'NQ02', 1),
('kho', '123456', 'NV003', 'NQ03', 1);
-- --------------------------------------------------------

INSERT INTO LoaiSP VALUES 
('L01', 'Loa Bluetooth'),
('L02', 'Tai nghe Over-ear'),
('L03', 'Phụ kiện âm thanh');

INSERT INTO HangSX VALUES 
('H01', 'Marshall', 'Anh'),
('H02', 'Sony', 'Nhật'),
('H03', 'JBL', 'Mỹ');

INSERT INTO SanPham (MaSP, TenSP, MaLoai, MaHang, MoTa, ThoiGianBaoHanh, TrangThai, HinhAnh) VALUES 
('SP001', 'Marshall Stanmore III', 'L01', 'H01', 'Loa decor cực đẹp', 12, TRUE, 'marshall.jpg'),
('SP002', 'Sony WH-1000XM5', 'L02', 'H02', 'Chống ồn đỉnh cao', 12, TRUE, 'sony_xm5.jpg'),
('SP003', 'JBL Boombox 3', 'L01', 'H03', 'Loa di động công suất lớn, kháng nước IP67', 12, TRUE, 'jbl_boombox3.jpg'),
('SP004', 'Marshall Emberton II', 'L01', 'H01', 'Loa cầm tay nhỏ gọn, pin 30h', 12, TRUE, 'marshall_emberton_2.jpg'),
('SP005', 'Marshall Middleton', 'L01', 'H01', 'Loa Bluetooth 4 loa cực mạnh', 12, TRUE, 'marshall_middleton.jpg'),
('SP006', 'Sony SRS-XE300', 'L01', 'H02', 'Loa chống nước, âm thanh rộng', 12, TRUE, 'sony_xe300.jpg'),
('SP007', 'Sony WH-CH720N', 'L02', 'H02', 'Tai nghe chống ồn giá rẻ', 12, TRUE, 'sony_ch720n.jpg'),
('SP008', 'JBL Charge 5', 'L01', 'H03', 'Loa Bluetooth kiêm sạc dự phòng', 12, TRUE, 'jbl_charge5.jpg'),
('SP009', 'JBL PartyBox Encore', 'L01', 'H03', 'Loa kèm 2 Micro hát karaoke', 12, TRUE, 'jbl_encore.jpg'),
('SP010', 'JBL Flip 6', 'L01', 'H03', 'Loa di động âm thanh 2 đường tiếng', 12, TRUE, 'jbl_flip6.jpg'),
('SP011', 'JBL Go 3', 'L01', 'H03', 'Loa mini chống nước', 12, TRUE, 'jbl_go3.jpg'),
('SP012', 'Apple AirPods Pro 2', 'L02', 'H03', 'Chống ồn chủ động', 12, TRUE, 'airpods_pro.jpg'),
('SP013', 'Sennheiser HD 450BT', 'L02', 'H02', 'Âm thanh chi tiết, pin 30h', 24, TRUE, 'sennheiser_hd450.jpg'),
('SP014', 'Bose QuietComfort 45', 'L02', 'H03', 'Khử tiếng ồn đỉnh cao', 12, TRUE, 'bose_qc45.jpg'),
('SP015', 'JBL Tune 510BT', 'L02', 'H03', 'Bass mạnh, giá sinh viên', 12, TRUE, 'jbl_510bt.jpg'),
('SP016', 'Sony WH-1000XM4', 'L02', 'H02', 'Bản nâng cấp huyền thoại', 12, TRUE, 'sony_xm4.jpg'),
('SP017', 'Marshall Major IV', 'L02', 'H01', 'Pin 80h, sạc không dây', 12, TRUE, 'marshall_major4.jpg'),
('SP018', 'AirPods Max', 'L02', 'H03', 'Tai nghe Over-ear cao cấp', 12, TRUE, 'airpods_max.jpg'),
('SP019', 'Micro Shure SM58', 'L03', 'H03', 'Micro vocal chuyên nghiệp', 12, TRUE, 'shure_sm58.jpg'),
('SP020', 'Cáp Audio 3.5mm Ugreen', 'L03', 'H02', 'Dây bọc dù chống nhiễu', 6, TRUE, 'ugreen_35.jpg'),
('SP021', 'Giá treo tai nghe Gỗ', 'L03', 'H01', 'Decor gỗ Walnut sang trọng', 0, TRUE, 'stand_wood.jpg'),
('SP022', 'Bao da Marshall Willen', 'L03', 'H01', 'Bao bảo vệ silicon', 3, TRUE, 'case_willen.jpg'),
('SP023', 'Micro JBL PBM100', 'L03', 'H03', 'Micro karaoke có dây', 12, TRUE, 'jbl_pbm100.jpg'),
('SP024', 'Cáp sạc Type-C bọc thép', 'L03', 'H02', 'Sạc siêu bền cho loa', 6, TRUE, 'cable_c.jpg'),
('SP025', 'Bộ vệ sinh tai nghe 3 in 1', 'L03', 'H02', 'Dạng bút vệ sinh tiện lợi', 0, TRUE, 'cleaning_kit.jpg'),
('SP026', 'Jack chuyển 6.35mm sang 3.5mm', 'L03', 'H02', 'Mạ vàng cao cấp', 6, TRUE, 'jack_convert.jpg'),
('SP027', 'Túi đựng loa JBL Charge', 'L03', 'H03', 'Chống sốc EVA cao cấp', 3, TRUE, 'bag_jbl.jpg');


INSERT INTO PhienBanSP VALUES 
('PB001', 'SP001', 'Kem (Cream)', '80W', 'N/A', 'Bluetooth 5.2', 7000000, 9500000, 0, TRUE, 'marshall.jpg'),
('PB002', 'SP002', 'Đen (Black)', 'N/A', '30h', 'Bluetooth 5.2', 7000000, 9500000, 0, TRUE, 'sony_xm5.jpg'),
('PB003', 'SP003', 'Đen (Black)', '180W', '24h', 'Bluetooth 5.3', 8500000, 11900000, 0, TRUE, 'jbl_boombox3.jpg'),
('PB004', 'SP004', 'Đen Brass', '20W', '30h', 'Bluetooth 5.1', 3200000, 4500000, 0, TRUE, 'marshall_emberton_2.jpg'),
('PB005', 'SP005', 'Đen (Black)', '60W', '20h', 'Bluetooth 5.1', 6500000, 8500000, 0, TRUE, 'marshall_middleton.jpg'),
('PB006', 'SP006', 'Xám (Grey)', '30W', '24h', 'Bluetooth 5.2', 2800000, 3900000, 0, TRUE, 'sony_xe300.jpg'),
('PB007', 'SP007', 'Xanh (Blue)', 'N/A', '35h', 'Bluetooth 5.2', 1800000, 2500000, 5, TRUE, 'sony_ch720n.jpg'),
('PB008', 'SP008', 'Đen (Black)', '40W', '20h', 'Bluetooth 5.1', 3000000, 3900000, 0, TRUE, 'jbl_charge5.jpg'),
('PB009', 'SP009', 'Đen (Black)', '100W', '10h', 'Bluetooth 5.1', 5800000, 7500000, 0, TRUE, 'jbl_encore.jpg'),
('PB010', 'SP010', 'Đỏ (Red)', '20W', '12h', 'Bluetooth 5.1', 2200000, 2900000, 0, TRUE, 'jbl_flip6.jpg'),
('PB011', 'SP011', 'Xanh quân đội', '4.2W', '5h', 'Bluetooth 5.1', 800000, 1050000, 0, TRUE, 'jbl_go3.jpg'),
('PB012', 'SP012', 'Trắng', 'N/A', '6h', 'Bluetooth 5.3', 4500000, 5900000, 0, TRUE, 'airpods_pro.jpg'),
('PB013', 'SP013', 'Đen', 'N/A', '30h', 'Bluetooth 5.0', 2800000, 3500000, 0, TRUE, 'sennheiser_hd450.jpg'),
('PB014', 'SP014', 'Trắng', 'N/A', '24h', 'Bluetooth 5.1', 5500000, 7200000, 0, TRUE, 'bose_qc45.jpg'),
('PB015', 'SP015', 'Hồng', 'N/A', '40h', 'Bluetooth 5.0', 700000, 1200000, 0, TRUE, 'jbl_510bt.jpg'),
('PB016', 'SP016', 'Bạc', 'N/A', '30h', 'Bluetooth 5.0', 5000000, 6500000, 0, TRUE, 'sony_xm4.jpg'),
('PB017', 'SP017', 'Nâu da', 'N/A', '80h', 'Bluetooth 5.0', 3000000, 4200000, 0, TRUE, 'marshall_major4.jpg'),
('PB018', 'SP018', 'Xanh Sky', 'N/A', '20h', 'Bluetooth 5.0', 10000000, 13500000, 0, TRUE, 'airpods_max.jpg'),
('PB019', 'SP019', 'Xám', 'N/A', 'N/A', 'XLR', 2100000, 2800000, 0, TRUE, 'shure_sm58.jpg'),
('PB020', 'SP020', 'Đen', 'N/A', 'N/A', '3.5mm', 150000, 250000, 0, TRUE, 'ugreen_35.jpg'),
('PB021', 'SP021', 'Gỗ Walnut', 'N/A', 'N/A', 'N/A', 300000, 550000, 0, TRUE, 'stand_wood.jpg'),
('PB022', 'SP022', 'Đen', 'N/A', 'N/A', 'Silicon', 100000, 190000, 0, TRUE, 'case_willen.jpg'),
('PB023', 'SP023', 'Đen', 'N/A', 'N/A', 'Cáp 3m', 700000, 950000, 0, TRUE, 'jbl_pbm100.jpg'),
('PB024', 'SP024', 'Đỏ', '60W', 'N/A', 'Type-C', 120000, 220000, 0, TRUE, 'cable_c.jpg'),
('PB025', 'SP025', 'Nhiều màu', 'N/A', 'N/A', 'N/A', 50000, 95000, 0, TRUE, 'cleaning_kit.jpg'),
('PB026', 'SP026', 'Vàng', 'N/A', 'N/A', 'Mạ vàng', 80000, 150000, 0, TRUE, 'jack_convert.jpg'),
('PB027', 'SP027', 'Đen', 'N/A', 'N/A', 'Vải Canvas', 200000, 350000, 0, TRUE, 'bag_jbl.jpg');
-- --------------------------------------------------------

INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, Sdt) VALUES 
('NCC001', 'Marshall VN Dist', 'Q1, TP.HCM', '0283333089'),
('NCC002', 'B&O', 'Q7, TP.HCM', '0961254087'),
('NCC003', 'Bose', 'Hà Nội', '0991299099');

INSERT INTO PhieuNhap (MaPhieuNhap, MaNV, MaNCC) VALUES 
('PN001', 'NV001', 'NCC001');

-- INSERT INTO PhieuNhap (MaPhieuNhap, NgayNhap, MaNV, MaNCC, TongTien) VALUES 
-- ('PN002', '2026-01-10 08:00:00', 'NV001', 'NCC001', 150000000),
-- ('PN003', '2026-01-20 14:00:00', 'NV001', 'NCC003', 85000000),
-- ('PN004', '2026-02-05 10:30:00', 'NV001', 'NCC002', 120000000),
-- ('PN005', '2026-02-25 16:00:00', 'NV001', 'NCC001', 95000000),
-- ('PN006', '2026-03-02 09:00:00', 'NV001', 'NCC003', 210000000),
-- ('PN007', '2026-03-05 13:00:00', 'NV001', 'NCC001', 45000000);

INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaPhienBan, SoLuong, DonGia) VALUES 
('PN001', 'PB001', 3, 7000000);

INSERT INTO ChiTietSP (MaImei, MaPhienBan, MaPhieuNhap, TinhTrang) VALUES 
('111222333', 'PB001', 'PN001', 'Trong kho'),
('444555666', 'PB001', 'PN001', 'Trong kho'),
('777888999', 'PB001', 'PN001', 'Trong kho');
-- ('840100001', 'PB001', 'PN01', 'Trong kho'), 
-- ('840100002', 'PB001', 'PN01', 'Trong kho'),
-- ('840100003', 'PB001', 'PN01', 'Trong kho'),
-- ('123123123', 'PB002', 'PN01', 'Trong kho'),
-- ('456456456', 'PB002', 'PN01', 'Trong kho'),
-- ('999888777', 'PB002', 'PN01', 'Đã bán'),
-- ('333111001', 'PB003', 'PN01', 'Trong kho');
-- ------------------------------------------------------

INSERT INTO KhuyenMai (MaKM, TenKM, DieuKienGiam, PhanTramGiam, NgayBatDau, NgayKetThuc, TrangThai) VALUES 
('KM001', 'Khai trương', 0, 10, '2025-01-01', '2030-12-31', 1), 
('KM002', 'Siêu sale Black Friday', 5000000, 20, '2026-11-20', '2026-11-30', 0),
('KM003', 'Hè rực rỡ', 0, 5, '2026-06-01', '2026-08-31', 0),
('KM004', 'Chào năm mới 2027', 2000000, 15, '2026-12-25', '2027-01-05', 0),
('KM005', 'Tri ân khách hàng VIP', 10000000, 25, '2026-01-01', '2026-12-31', 1),
('KM006', 'Giảm giá cuối tháng', 1000000, 8, '2026-03-25', '2026-03-31', 0),
('KM007', 'Ngày Quốc tế Phụ nữ 8/3', 0, 8, '2026-03-05', '2026-03-10', 1),
('KM008', 'Mua loa tặng phụ kiện', 3000000, 12, '2026-03-01', '2026-03-31', 1);

INSERT INTO PhieuXuat (MaPhieuXuat, MaNV, MaKH, MaKM) VALUES 
('PX001', 'NV001', 'KH001', 'KM001');

INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaPhienBan, SoLuong, DonGia) VALUES 
('PX001', 'PB001', 3, 8550000);

-- -- --------------------------------------------------------

-- INSERT INTO PhieuXuat (MaPhieuXuat, NgayXuat, MaNV, MaKH, TongTien) VALUES 
-- ('PX02', '2026-02-20 10:30:00', 'NV02', 'KH02', 30900000);

UPDATE ChiTietSP SET TinhTrang = 'Đã bán', MaPhieuXuat = 'PX001' WHERE MaImei IN ('111222333', '444555666', '777888999');
-- UPDATE ChiTietSP SET TinhTrang = 'Đã bán', MaPhieuXuat = 'PX02' WHERE MaImei IN ('123123123', '456456456', '333111001');

UPDATE ChiTietSP 
SET TinhTrang = 'Máy lỗi đổi trả', MaPhieuXuat = NULL 
WHERE MaImei = '111222333';

UPDATE ChiTietSP 
SET TinhTrang = 'Đã bán', MaPhieuXuat = 'PX001' 
WHERE MaImei = '444555666';

INSERT INTO BaoHanh (MaBH, MaImei, MaPhieuXuat, NgayBatDau, NgayKetThuc, TinhTrang) VALUES 
('BH001', '111222333', 'PX001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 'Đang sửa chữa'),
('BH002', '444555666', 'PX001', '2026-01-10', '2027-01-10', 'Đã trả máy'),
('BH003', '777888999', 'PX001', '2026-02-05', '2027-02-05', 'Đang sửa chữa');

INSERT INTO ChiTietBaoHanh (MaCTBH, MaBH, NoiDung, TinhTrang) VALUES 
('CTBH001', 'BH001', 'Loa bị rè bass', 'Đang sửa chữa'),
('CTBH002', 'BH002', 'Lỗi kết nối Bluetooth chập chờn', 'Đã trả máy'),
('CTBH003', 'BH003', 'Vệ sinh chân sạc miễn phí', 'Đang sửa chữa');

INSERT INTO NCC_SanPham VALUES 
('NCC001', 'SP001'), 
('NCC003', 'SP003'),
('NCC001', 'SP004'), 
('NCC001', 'SP005'), 
('NCC002', 'SP006'), 
('NCC002', 'SP007'), 
('NCC003', 'SP008'), 
('NCC003', 'SP009'), 
('NCC003', 'SP010'),
('NCC003', 'SP011'), 
('NCC003', 'SP012'), 
('NCC002', 'SP013'), 
('NCC003', 'SP014'), 
('NCC003', 'SP015'), 
('NCC002', 'SP016'), 
('NCC001', 'SP017'), 
('NCC003', 'SP018'), 
('NCC003', 'SP019'), 
('NCC002', 'SP020'), 
('NCC001', 'SP021'), 
('NCC001', 'SP022'), 
('NCC003', 'SP023'), 
('NCC002', 'SP024'), 
('NCC002', 'SP025'), 
('NCC002', 'SP026'), 
('NCC003', 'SP027');

INSERT INTO DoiTra (MaDoiTra, MaKH, MaPhieuXuat, MaImei, NgayDoiTra, LyDo, TrangThai)
VALUES ('DT001', 'KH001', 'PX001', '111222333', '2026-03-20', 'Loa bị rè bass', 1);

UPDATE ChiTietBaoHanh 
SET TinhTrang = 'Còn bảo hành' 
WHERE TinhTrang = 'Hoàn thành' 
AND NoiDung = 'Kích hoạt bảo hành điện tử';

INSERT INTO ChiTietBaoHanh (MaCTBH, MaBH, NoiDung, TinhTrang)
SELECT 
    CONCAT('CT', b.MaBH),
    b.MaBH,               
    'Kích hoạt bảo hành điện tử', 
    'Còn bảo hành'
FROM BaoHanh AS b 
WHERE b.MaBH NOT IN (SELECT MaBH FROM ChiTietBaoHanh);

SELECT bh.MaBH, ct.MaCTBH, ct.TinhTrang
FROM BaoHanh bh
LEFT JOIN ChiTietBaoHanh ct ON ct.MaBH = bh.MaBH
ORDER BY bh.MaBH, ct.MaCTBH DESC;


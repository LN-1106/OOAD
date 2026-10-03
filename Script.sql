CREATE DATABASE IF NOT EXISTS quanlydienthoai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE quanlydienthoai;

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
    Ram VARCHAR(20),          -- Thêm mới (VD: 8GB, 12GB)
    BoNhoTrong VARCHAR(20),   -- Thêm mới (VD: 128GB, 256GB)
    DungLuongPin VARCHAR(50),  -- Sửa lại (VD: 5000mAh)
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
('L01', 'Smartphone'),
('L02', 'Điện thoại phổ thông'),
('L03', 'Phụ kiện');

INSERT INTO HangSX VALUES 
('H01', 'Apple', 'Mỹ'),
('H02', 'Samsung', 'Hàn Quốc'),
('H03', 'Xiaomi', 'Trung Quốc');

INSERT INTO SanPham (MaSP, TenSP, MaLoai, MaHang, MoTa, ThoiGianBaoHanh, TrangThai, HinhAnh) VALUES 
('SP001', 'iPhone 15 Pro Max', 'L01', 'H01', 'Khung Titan, Chip A17 Pro', 12, TRUE, 'iphone-15-128-gbden.webp'),
('SP002', 'Samsung Galaxy S24 Ultra', 'L01', 'H02', 'Bút S-Pen, Galaxy AI', 12, TRUE, 'iphone-15-pro-256gb.webp'),
('SP003', 'Xiaomi 14', 'L01', 'H03', 'Camera Leica, Snapdragon 8 Gen 3', 18, TRUE, 'iphone_17_256gb-3_3.jpg'),
('SP004', 'iPad Air 5 M1', 'L02', 'H01', 'Màn hình Liquid Retina 10.9 inch', 12, TRUE, 'iphone-14-pro_2__5.png');

-- LƯU Ý: Thứ tự cột của PhienBanSP là (MaPhienBan, MaSP, MauSac, Ram, BaoNhoTrong, DungLuongPin, GiaNhap, GiaBan, SoLuongTon, TrangThai, HinhAnh)
INSERT INTO PhienBanSP VALUES 
('PB001', 'SP001', 'Titan Tự Nhiên', '8GB', '256GB', '4422 mAh', 27000000, 31990000, 0, TRUE, 'iphone-15-128-gbden.webp'),
('PB002', 'SP001', 'Đen Titan', '8GB', '512GB', '4422 mAh', 32000000, 37990000, 0, TRUE, 'iphone-15-128-gbden.webp'),
('PB003', 'SP002', 'Xám Titanium', '12GB', '256GB', '5000 mAh', 25000000, 29990000, 0, TRUE, 'iphone-15-pro-256gb.webp'),
('PB004', 'SP003', 'Xanh Lá', '12GB', '256GB', '4610 mAh', 18000000, 22990000, 0, TRUE, 'iphone_17_256gb-3_3.jpg'),
('PB005', 'SP004', 'Xám Space', '8GB', '64GB', '28.6 Wh', 12000000, 14990000, 0, TRUE, 'iphone-14-pro_2__5.png');
-- --------------------------------------------------------

INSERT INTO NhaCungCap (MaNCC, TenNCC, DiaChi, Sdt) VALUES 
('NCC001', 'FPT Synnex', 'Q1, TP.HCM', '0283333089'),
('NCC002', 'Digiworld (DGW)', 'Q3, TP.HCM', '0961254087'),
('NCC003', 'Viettel Distribution', 'Hà Nội', '0991299099');

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
('NCC001', 'SP004'),
('NCC001', 'SP003'), 
('NCC002', 'SP002'), 
('NCC003', 'SP003');

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


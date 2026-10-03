package Frontend.Compoent;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.colors.ColorConstants;
import Backend.DTO.PhieuXuat;
import Backend.BUS.PhieuXuatBUS;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.awt.Desktop;

public class XuatPDFHoaDonXuat {
        public static void xuatHoaDonNhap(PhieuXuat px, DefaultTableModel model) {
                try {
                        if (model.getColumnCount() < 6) {
                                System.out.println("LỖI: Model truyền vào PDF không đủ cột dữ liệu!");
                                return;
                        }

                        PhieuXuatBUS pxBUS = new PhieuXuatBUS();

                        // Lấy phần trăm giảm giá từ DB
                        double phanTramGiam = pxBUS.getPhanTramGiamCuaPhieu(px.getMaPhieuXuat());

                        // LẤY TÊN KHÁCH HÀNG: Đảm bảo bạn đã thêm hàm này ở file BUS và DAO!
                        String tenKhach = pxBUS.getTenKhachHang(px.getMaKH());
                        if (tenKhach == null || tenKhach.isEmpty())
                                tenKhach = "Khách vãng lai";

                        String fileName = "HoaDonXuat_" + px.getMaPhieuXuat() + ".pdf";
                        PdfWriter writer = new PdfWriter(fileName);
                        PdfDocument pdf = new PdfDocument(writer);
                        Document document = new Document(pdf);

                        PdfFont font = PdfFontFactory.createFont("C:/Windows/Fonts/Arial.ttf", PdfEncodings.IDENTITY_H);

                        document.add(new Paragraph("PHIẾU BÁN HÀNG")
                                        .setFont(font).setFontSize(18).setBold()
                                        .setTextAlignment(TextAlignment.CENTER));
                        document.add(new Paragraph("\n"));

                        document.add(new Paragraph("Mã phiếu: " + px.getMaPhieuXuat()).setFont(font));
                        document.add(new Paragraph("Nhân viên lập: " + px.getMaNV()).setFont(font));

                        // IN TÊN KHÁCH HÀNG ĐÃ SỬA LỖI FONT
                        document.add(new Paragraph("Khách hàng: " + tenKhach).setFont(font));

                        // Format lại ngày giờ cho thân thiện với người Việt
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        document.add(new Paragraph("Ngày lập: "
                                        + sdf.format(px.getNgayXuat() != null ? px.getNgayXuat() : new Date()))
                                        .setFont(font));
                        document.add(new Paragraph("\n"));

                        // Chỉnh lại kích thước các cột cho bảng đẹp hơn
                        float[] columnWidths = { 15f, 150f, 30f, 65f, 95f, 95f };
                        Table table = new Table(columnWidths);
                        table.useAllAvailableWidth();

                        String[] headers = { "STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "Tổng tiền", "Thành Tiền" };
                        for (String h : headers) {
                                table.addHeaderCell(new Cell().add(new Paragraph(h).setFont(font).setBold())
                                                .setTextAlignment(TextAlignment.CENTER)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                        }

                        double total = 0; // Biến tính tổng gốc (Trước KM)
                        double totalInvoice = 0; // Biến tính tổng thực thu (Sau KM)

                        for (int i = 0; i < model.getRowCount(); i++) {
                                String stt = model.getValueAt(i, 0).toString();
                                String tenSP = model.getValueAt(i, 2).toString();
                                int sl = Integer.parseInt(model.getValueAt(i, 3).toString());
                                String giaStr = model.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                                double donGia = Double.parseDouble(giaStr);

                                double thanhTienGocRow = donGia * sl;
                                double thanhTienSauGiamRow = thanhTienGocRow * (1 - phanTramGiam / 100.0);

                                total += thanhTienGocRow;
                                totalInvoice += thanhTienSauGiamRow;

                                table.addCell(new Cell().add(new Paragraph(stt).setFont(font))
                                                .setTextAlignment(TextAlignment.CENTER));
                                table.addCell(new Cell().add(new Paragraph(tenSP).setFont(font)));
                                table.addCell(new Cell().add(new Paragraph(String.valueOf(sl)).setFont(font))
                                                .setTextAlignment(TextAlignment.CENTER));
                                table.addCell(new Cell()
                                                .add(new Paragraph(String.format("%,.0f", donGia)).setFont(font))
                                                .setTextAlignment(TextAlignment.RIGHT));
                                table.addCell(new Cell().add(
                                                new Paragraph(String.format("%,.0f", thanhTienGocRow)).setFont(font))
                                                .setTextAlignment(TextAlignment.RIGHT));
                                table.addCell(new Cell().add(new Paragraph(String.format("%,.0f", thanhTienSauGiamRow))
                                                .setFont(font)).setTextAlignment(TextAlignment.RIGHT));
                        }

                        // ==========================================
                        // HIỂN THỊ 3 DÒNG TỔNG KẾT
                        // ==========================================

                        // 1. DÒNG TỔNG TIỀN GỐC (Trộn 5 ô đầu)
                        table.addCell(new Cell(1, 5).add(new Paragraph("TỔNG CỘNG (TRƯỚC KM):").setFont(font).setBold())
                                        .setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(new Cell().add(new Paragraph(String.format("%,.0f VNĐ", total)).setFont(font))
                                        .setTextAlignment(TextAlignment.RIGHT));

                        // 2. DÒNG MỨC KHUYẾN MÃI
                        table.addCell(new Cell(1, 5)
                                        .add(new Paragraph("MỨC KHUYẾN MÃI ÁP DỤNG:").setFont(font).setBold())
                                        .setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(new Cell().add(new Paragraph(phanTramGiam + " %").setFont(font))
                                        .setTextAlignment(TextAlignment.RIGHT));

                        // 3. DÒNG THÀNH TIỀN THỰC TẾ
                        table.addCell(new Cell(1, 5).add(new Paragraph("TỔNG TIỀN THANH TOÁN:").setFont(font).setBold())
                                        .setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(new Cell().add(new Paragraph(String.format("%,.0f VNĐ", totalInvoice))
                                        .setFont(font).setBold())
                                        .setFontColor(ColorConstants.RED)
                                        .setTextAlignment(TextAlignment.RIGHT));

                        document.add(table);
                        document.close();

                        File file = new File(fileName);
                        if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(file);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
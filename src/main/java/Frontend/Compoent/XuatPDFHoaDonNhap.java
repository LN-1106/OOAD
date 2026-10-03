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
import Backend.DTO.PhieuNhap;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.io.File;
import java.awt.Desktop;

public class XuatPDFHoaDonNhap {
    public static void xuatHoaDonNhap(PhieuNhap pn, DefaultTableModel model) {
        try {
            if (model.getColumnCount() < 6) {
                System.out.println("LỖI: Model truyền vào PDF không đủ cột dữ liệu!");
                return;
            }

            String fileName = "HoaDonNhap_" + pn.getmaPhieuNhap() + ".pdf";
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont("C:/Windows/Fonts/Arial.ttf", PdfEncodings.IDENTITY_H);

            document.add(new Paragraph("PHIẾU NHẬP HÀNG")
                    .setFont(font).setFontSize(18).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Mã phiếu: " + pn.getmaPhieuNhap()).setFont(font));
            document.add(new Paragraph("Nhân viên lập: " + pn.getmaNV()).setFont(font));
            document.add(new Paragraph("Ngày lập: " + new Date().toString()).setFont(font));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {30f, 180f, 40f, 80f, 100f};
            Table table = new Table(columnWidths);
            table.useAllAvailableWidth();

            String[] headers = {"STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền"};
            for (String h : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(h).setFont(font).setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            double totalInvoice = 0; 

            for (int i = 0; i < model.getRowCount(); i++) {
                // Cột 0: STT
                table.addCell(new Cell().add(new Paragraph(model.getValueAt(i, 0).toString()).setFont(font))
                    .setTextAlignment(TextAlignment.CENTER));
                
                // Cột 2: Tên Sản Phẩm
                table.addCell(new Cell().add(new Paragraph(model.getValueAt(i, 2).toString()).setFont(font)));
                
                // Cột 3: Số lượng
                table.addCell(new Cell().add(new Paragraph(model.getValueAt(i, 3).toString()).setFont(font))
                    .setTextAlignment(TextAlignment.CENTER));
                
                // Cột 4: Đơn giá
                table.addCell(new Cell().add(new Paragraph(model.getValueAt(i, 4).toString()).setFont(font))
                    .setTextAlignment(TextAlignment.RIGHT));
                
                // Cột 5: Thành tiền
                String thanhTienStr = model.getValueAt(i, 5).toString();
                table.addCell(new Cell().add(new Paragraph(thanhTienStr).setFont(font))
                    .setTextAlignment(TextAlignment.RIGHT));

                double val = Double.parseDouble(thanhTienStr.replaceAll("[^0-9]", ""));
                totalInvoice += val;
            }

            table.addCell(new Cell(1, 4).add(new Paragraph("TỔNG TIỀN THANH TOÁN").setFont(font).setBold())
                .setTextAlignment(TextAlignment.RIGHT));
            
            table.addCell(new Cell().add(new Paragraph(String.format("%,.0f VNĐ", totalInvoice)).setFont(font).setBold())
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
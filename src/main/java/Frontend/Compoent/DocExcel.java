package Frontend.Compoent;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DocExcel {
    // Trả về danh sách mảng Object chứa: [Mã SP, Tên SP, Số Lượng, Đơn Giá]
    public static ArrayList<Object[]> docDuLieuNhapHang() {
        ArrayList<Object[]> dsSanPham = new ArrayList<>();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file mẫu Nhập Hàng (Excel)");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = new XSSFWorkbook(fis)) {
                 
                Sheet sheet = workbook.getSheetAt(0); 
                
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    try {
                        // Đọc theo thứ tự cột: Mã PB | Tên PB | Số lượng | Giá nhập
                        String maPB = row.getCell(0).getStringCellValue().trim();
                        String tenSP = row.getCell(1).getStringCellValue().trim();
                        int sl = (int) row.getCell(2).getNumericCellValue();
                        double gia = row.getCell(3).getNumericCellValue();

                        dsSanPham.add(new Object[]{maPB, tenSP, sl, gia});
                    } catch (Exception ex) {
                        System.out.println("Bỏ qua dòng " + i + " do sai định dạng (có thể là ô trống)");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Không thể đọc file Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        return dsSanPham;
    }
}
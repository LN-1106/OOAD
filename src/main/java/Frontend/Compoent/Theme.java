package Frontend.Compoent;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class Theme {
    // Tông màu chủ đạo: Xám xanh nhẹ
    public static final Color PRIMARY_COLOR = new Color(53, 59, 71); // Màu chính cho Header & Sidebar
    public static final Color SECONDARY_COLOR = new Color(47, 54, 64);
    public static final Color BACKGROUND_COLOR = new Color(245, 246, 250); // Màu nền app (Trắng xám nhẹ)

    // Màu chức năng
    public static final Color ACCENT_COLOR = new Color(42, 187, 155);   // Xanh lá (Thêm/Lưu)
    public static final Color DANGER_COLOR = new Color(235, 87, 87);   // Đỏ (Xóa/Đăng xuất)
    public static final Color WARNING_COLOR = new Color(243, 156, 18); // Vàng (Cảnh báo/Sửa)
    public static final Color PURPLE_COLOR = new Color(155, 89, 182); // Tím (Làm mới)

    public static final int ROUNDING_ARC = 15;

    public static void setup(boolean isDark) {
        try {
            if(isDark) FlatDarkLaf.setup();
            else FlatLightLaf.setup();

            // Cấu hình UI Defaults
            UIManager.put("Button.arc", ROUNDING_ARC);
            UIManager.put("Component.arc", ROUNDING_ARC);
            UIManager.put("TextComponent.arc", ROUNDING_ARC);
            UIManager.put("CheckBox.arc", 5);
            UIManager.put("ScrollBar.thumbArc", 999);

            // Tinh chỉnh Table hiện đại
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.selectionBackground", new Color(232, 236, 241));
            UIManager.put("Table.selectionForeground", Color.BLACK);

            // Chỉnh màu sắc focus cho hiện đại
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Button.innerFocusWidth", 0);
        } catch (Exception ex) {
            System.err.println("Không thể thiết lập Theme!");
        }
    }
}

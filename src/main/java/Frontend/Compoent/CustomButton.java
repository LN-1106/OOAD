package Frontend.Compoent;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton{
    private Color originalBg;
    private Color hoverBg;
    private Color activeBg;
    private boolean isHovering = false;
    private boolean isActive = false;
    
    public CustomButton(String text, Color bg) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(bg);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Loại bỏ viền mặc định khi focus
        setFocusPainted(false);
        setBorderPainted(false);
        
        // Lưu màu gốc và tính toán màu hover (sáng hơn 30%) và active (sáng hơn 40%)
        this.originalBg = bg;
        this.hoverBg = brightenColor(bg, 0.3f);
        this.activeBg = brightenColor(bg, 0.4f);
        
        // Thêm mouse listener cho hiệu ứng hover
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled() && !isActive) {
                    setBackground(hoverBg);
                    isHovering = true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled() && !isActive) {
                    setBackground(originalBg);
                    isHovering = false;
                }
            }
        });
    }
    
    // Set button thành active (đang chọn)
    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setBackground(activeBg);
        } else {
            setBackground(originalBg);
        }
    }
    
    // Hàm để làm sáng màu
    private Color brightenColor(Color color, float amount) {
        int r = Math.min(255, (int) (color.getRed() + 255 * amount));
        int g = Math.min(255, (int) (color.getGreen() + 255 * amount));
        int b = Math.min(255, (int) (color.getBlue() + 255 * amount));
        return new Color(r, g, b);
    }
}

package Frontend.GUI;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import Frontend.Compoent.Theme; 
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Navigation extends JPanel{
    
    public Navigation(String userName) {
        setBackground(Theme.PRIMARY_COLOR);
        
        setLayout(new MigLayout("fill, insets 0 40 0 40", "[200!][grow, center][200!]", "[65!, center]"));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, 
                                                    new Color(230, 230, 230)));

        JPanel userBox = new JPanel(new MigLayout("insets 0, gapx 10", "[] []", "center"));
        userBox.setOpaque(false);
        userBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel userIconLabel = new JLabel(new FlatSVGIcon("images/icon/circle-user-round.svg", 35, 35));

        JLabel lblUser = new JLabel(userName);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 20));

        userBox.add(userIconLabel);
        userBox.add(lblUser);

        add(userBox, "left");

        JLabel lblLogo = new JLabel("SoundWave Store");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblLogo.setForeground(Color.WHITE);
        
        add(lblLogo, "center");
    }
}
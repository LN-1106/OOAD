package Frontend.GUI;

import javax.swing.*;
import java.awt.*;
import Frontend.Compoent.Theme;

public class MainFrame extends JFrame {
    private JPanel mainContent;

    public MainFrame(String userName, String maNQ) {
        setTitle("Cửa hàng thiết bị âm thanh " + userName);
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(new Navigation(userName), BorderLayout.NORTH);
        add(new Sidebar(this, maNQ), BorderLayout.WEST);

        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Theme.BACKGROUND_COLOR);
        add(mainContent, BorderLayout.CENTER);

        JPanel pnlWelcome = new JPanel(new GridBagLayout());
        pnlWelcome.setBackground(Theme.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblImage = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/isometric-logistics-delivery-composition-with-images-shelves-parcel-boxes-human-characters-workers-vector-illustration.png"));
            Image scaled = icon.getImage().getScaledInstance(600, -1, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImage.setText("🎧"); 
            lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 200));
            lblImage.setForeground(new Color(200, 200, 200));
        }
        
        pnlWelcome.add(lblImage, gbc);

        JLabel lblWelcome = new JLabel("Chào mừng " + userName + " trở lại hệ thống!");
        lblWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        lblWelcome.setForeground(new Color(80, 80, 80));

        gbc.gridy = 1; 
        pnlWelcome.add(lblWelcome, gbc);
        
        setPage(pnlWelcome);
    }

    public void setPage(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
}

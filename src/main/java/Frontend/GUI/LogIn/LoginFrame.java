package Frontend.GUI.LogIn;

import Frontend.Compoent.Theme;
import Frontend.Compoent.CustomButton;
import Backend.BUS.TaiKhoanBUS;
import Backend.DTO.TaiKhoan;
import net.miginfocom.swing.MigLayout;
import Backend.DTO.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.net.URL;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private CustomButton btnLogin;
    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Sound Store - Hệ thống quản lý");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Layout chính chia đều 50/50
        JPanel mainPanel = new JPanel(new MigLayout("insets 0, fill", "[50%!, fill][50%!, fill]", "[fill]"));

        // --- BÊN TRÁI: PANEL BRANDING CÓ HÌNH ẢNH MINH HỌA ---
        JPanel pnlImage = new JPanel(new MigLayout("fill, insets 40", "[center]", "push[]30[]10[]push[]")) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Hiệu ứng Gradient mượt mà
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMARY_COLOR,
                        getWidth(), getHeight(), Theme.PRIMARY_COLOR.darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Vẽ các họa tiết mờ trang trí
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fill(new Ellipse2D.Double(-80, -80, 250, 250));
                g2d.fill(new Ellipse2D.Double(getWidth() - 130, getHeight() - 130, 220, 220));
            }
        };

        // --- LOAD VÀ CĂN CHỈNH HÌNH ẢNH MINH HỌA (Encrypted Phone) ---
        URL imgUrl = getClass().getResource("/images/encrypted-phone.png");
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            // Căn chỉnh kích thước ảnh để vừa với panel bên trái (giữ tỷ lệ)
            Image scaledImage = icon.getImage().getScaledInstance(350, -1, Image.SCALE_SMOOTH);
            JLabel lblIllustration = new JLabel(new ImageIcon(scaledImage));
            pnlImage.add(lblIllustration, "wrap, gapbottom 10");
        } else {
            // Hiển thị thông báo lỗi nếu không tìm thấy ảnh
            System.err.println("Không tìm thấy hình ảnh minh họa tại: /images/encrypted-phone.png");
            // Backup bằng icon mặc định nếu mất ảnh
            JLabel lblBackup = new JLabel("🔒");
            lblBackup.setFont(new Font("Segoe UI", Font.PLAIN, 150));
            lblBackup.setForeground(new Color(255, 255, 255, 220));
            pnlImage.add(lblBackup, "wrap, gapbottom 20");
        }

        // --- CÁC THÀNH PHẦN VĂN BẢN TRÊN PANEL BÊN TRÁI ---
        JLabel lblLogo = new JLabel("SOUND STORE");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblDescription = new JLabel(
                "<html><center>Giải pháp quản lý kho hàng và doanh thu<br>dành riêng cho thiết bị âm thanh cao cấp</center></html>");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblDescription.setForeground(new Color(240, 240, 240));

        JLabel lblVersion = new JLabel("Phiên bản 1.3.0 • Bảo mật bởi SoundStore Team");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblVersion.setForeground(new Color(255, 255, 255, 150));

        pnlImage.add(lblLogo, "wrap");
        pnlImage.add(lblDescription, "wrap");
        pnlImage.add(lblVersion, "south, center");

        // --- BÊN PHẢI: PANEL FORM ĐĂNG NHẬP (Giữ nguyên giao diện đẹp cũ) ---
        JPanel pnlFormContainer = new JPanel(new MigLayout("fill, insets 40", "[center]", "[center]"));
        pnlFormContainer.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 0", "[grow]"));
        formPanel.setOpaque(false);

        JLabel lblWelcome = new JLabel("XIN CHÀO!");
        lblWelcome.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        lblWelcome.setForeground(Theme.PRIMARY_COLOR);

        JLabel lblLoginTitle = new JLabel("Đăng nhập để bắt đầu");
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLoginTitle.setForeground(new Color(50, 50, 50));

        txtUsername = new JTextField();
        txtUsername.putClientProperty("FlatLaf.style", "margin: 8,12,8,12; arc: 15; placeholderText: 'Tên đăng nhập'");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("FlatLaf.style",
                "margin: 8,12,8,12; arc: 15; showRevealButton: true; placeholderText: 'Mật khẩu'");

        btnLogin = new CustomButton("ĐĂNG NHẬP NGAY", Theme.PRIMARY_COLOR);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));

        formPanel.add(lblWelcome, "center");
        formPanel.add(lblLoginTitle, "gapbottom 40, center");

        formPanel.add(new JLabel("Tên đăng nhập"), "gapbottom 5");
        formPanel.add(txtUsername, "growx, h 50!, gapbottom 20");

        formPanel.add(new JLabel("Mật khẩu"), "gapbottom 5");
        formPanel.add(txtPassword, "growx, h 50!, gapbottom 40");

        formPanel.add(btnLogin, "growx, h 55!");

        pnlFormContainer.add(formPanel, "w 380!");

        mainPanel.add(pnlImage);
        mainPanel.add(pnlFormContainer);
        setContentPane(mainPanel);

        // Sự kiện phím Enter
        KeyAdapter enterSubmit = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    doLogin();
            }
        };
        txtUsername.addKeyListener(enterSubmit);
        txtPassword.addKeyListener(enterSubmit);

        // Sự kiện nút bấm
        btnLogin.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        Session.loadQuyen();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnLogin.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new Thread(() -> {
            TaiKhoan tk = taiKhoanBUS.login(username, password);

            SwingUtilities.invokeLater(() -> {
                if (tk != null) {
                    Backend.DTO.Session.currentAccount = tk;
                    Backend.BUS.NhanVienBUS nvBUS = new Backend.BUS.NhanVienBUS();
                    Backend.DTO.NhanVien nv = nvBUS.getById(tk.getMaNV());
                    Backend.DTO.Session.currentNhanVien = nv;

                    String displayName = (nv != null) ? nv.getHoTen() : tk.getUsername();
                    new Frontend.GUI.MainFrame(displayName, tk.getMaNhomQuyen()).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE);
                    btnLogin.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                    txtPassword.setText("");
                }
            });
        }).start();
    }
}

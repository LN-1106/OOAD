package Frontend.Compoent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.miginfocom.swing.MigLayout;

public abstract class ThaoTacDialog extends JDialog {

    protected JPanel pnlContent;
    protected CustomButton btnHuy, btnXacNhan;

    public ThaoTacDialog(Window parent, String title, int width, int height) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setSize(width, height);
        setLocationRelativeTo(parent); 
        setLayout(new BorderLayout());
        setResizable(false);
        
        getRootPane().putClientProperty("FlatLaf.style", "arc: 20");

        // --- PHẦN HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(Theme.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        pnlContent = new JPanel(new MigLayout("wrap 2, fillx, insets 25", "[110!]10[grow]", "[]10[]"));
        pnlContent.setBackground(Color.WHITE);
        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnHuy = new CustomButton("HỦY", Theme.DANGER_COLOR);
        btnXacNhan = new CustomButton("LƯU", Theme.ACCENT_COLOR);
        pnlFooter.add(btnHuy);
        pnlFooter.add(btnXacNhan);
        add(pnlFooter, BorderLayout.SOUTH);

        btnXacNhan.addActionListener(e -> logicXacNhan());
        btnHuy.addActionListener(e -> dispose());

        // Gọi initForm để lớp con vẽ giao diện
        initForm();
    }
    
    protected abstract void initForm();
    protected abstract void logicXacNhan();
}
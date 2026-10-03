package Frontend.GUI.BanHang;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PaginationPanel extends JPanel {
    private int currentPage = 1;
    private int totalPages;
    private JLabel lblPageInfo;
    private Consumer<Integer> onPageChange;
    private JButton btnPrev, btnNext; 

    public PaginationPanel(int totalPages, Consumer<Integer> onPageChange) {
        this.totalPages = totalPages;
        this.onPageChange = onPageChange;

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        setBackground(Color.WHITE);

        btnPrev = new JButton("<");
        btnNext = new JButton(">");

        btnPrev.putClientProperty("FlatLaf.style", "arc: 10");
        btnNext.putClientProperty("FlatLaf.style", "arc: 10");

        lblPageInfo = new JLabel();
        lblPageInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                refreshPage();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                refreshPage();
            }
        });

        add(btnPrev);
        add(lblPageInfo);
        add(btnNext);
        refreshPage();
        updateUIState(); 
    }

    public void setOnPageChange(Consumer<Integer> onPageChange) {
        this.onPageChange = onPageChange;
    }

    public void setTotalPages(int total) {
        this.totalPages = Math.max(1, total);
        this.currentPage = 1; 
        lblPageInfo.setText("Trang " + currentPage + " / " + totalPages);
        updateUIState();
    }

    private void refreshPage() {
        lblPageInfo.setText("Trang " + currentPage + " / " + totalPages);

        if (btnPrev != null)
            btnPrev.setEnabled(currentPage > 1);
        if (btnNext != null)
            btnNext.setEnabled(currentPage < totalPages);

        if (onPageChange != null) {
            onPageChange.accept(currentPage);
        }
    }

    private void updateUIState() {
        if (btnPrev != null && btnNext != null) {
            btnPrev.setEnabled(currentPage > 1);
            btnNext.setEnabled(currentPage < totalPages);
        }
    }
}
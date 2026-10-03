package Frontend.GUI.ThongKe;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Backend.BUS.ThongKeBUS;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

public class ProductChartPanel extends JPanel {
    private JComboBox<String> cbFilter;
    private final ThongKeBUS tkBUS = new ThongKeBUS();
    private DefaultCategoryDataset barDataset;
    private DefaultPieDataset<String> pieDataset;

    public ProductChartPanel() {
        setLayout(new MigLayout("fill, insets 0", "[60%]15[40%]", "[grow]"));
        setOpaque(false);
        initComponents();
    }

    private void initComponents() {
        JPanel pnlBar = createChartWrapper("Sản Phẩm Bán Chạy");
        pnlBar.add(createBarChart(), BorderLayout.CENTER);
        
        JPanel pnlPie = createChartWrapper("Tỷ Lệ Phân Loại");
        pnlPie.add(createPieChart(), BorderLayout.CENTER);

        add(pnlBar, "grow");
        add(pnlPie, "grow");

        cbFilter.addActionListener(e -> {
            String selectedType = cbFilter.getSelectedItem().toString();
            refreshCharts(selectedType);
        });
    }

    private void refreshCharts(String type) {
        tkBUS.napDuLieuTop5(barDataset, type);
        tkBUS.napDuLieuTiTrong(pieDataset, type);
    }

    private JPanel createChartWrapper(String title) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(Color.WHITE);
        pnl.putClientProperty("FlatLaf.style", "arc: 20");
        
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        cbFilter = new JComboBox<>(new String[]{"Ngày", "Tháng", "Năm"});
        
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(cbFilter, BorderLayout.EAST);
        
        pnl.add(pnlHeader, BorderLayout.NORTH);
        return pnl;
    }

    private ChartPanel createBarChart() {
        barDataset = new DefaultCategoryDataset();
        tkBUS.napDuLieuTop5(barDataset, "Tháng"); 

        JFreeChart chart = ChartFactory.createBarChart(
            "TOP 5 SẢN PHẨM BÁN CHẠY", 
            "",              // Trục tung (Tên SP)
            "Số lượng bán",  // Trục hoành (Giá trị)
            barDataset, 
            PlotOrientation.HORIZONTAL, 
            false, true, false
        );
        
        formatBasicChart(chart);
        
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        
        return new ChartPanel(chart);
    }

    private ChartPanel createPieChart() {
        pieDataset = new DefaultPieDataset<>();
        tkBUS.napDuLieuTiTrong(pieDataset, "Tháng");

        JFreeChart chart = ChartFactory.createPieChart("TỶ LỆ DOANH THU", pieDataset, true, true, false);
        formatBasicChart(chart);
        return new ChartPanel(chart);
    }

    private void formatBasicChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlineVisible(false);
    }
}
package Frontend.GUI.ThongKe;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import Backend.BUS.ThongKeBUS;
import net.miginfocom.swing.MigLayout;

public class TrendChartPanel extends JPanel {
    private final ThongKeBUS tkBUS = new ThongKeBUS();
    private DefaultCategoryDataset dataset;

    public TrendChartPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        putClientProperty("FlatLaf.style", "arc: 20");
        initComponents();
    }

   private void initComponents() {
        dataset = new DefaultCategoryDataset();
        tkBUS.napDuLieuXuHuong(dataset);

        JFreeChart chart = ChartFactory.createLineChart("BIẾN ĐỘNG GIAO DỊCH (15 NGÀY GẦN NHẤT)", "", "Số lượng", dataset);
        chart.setBackgroundPaint(Color.WHITE);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        // Màu cho đường "Khách hàng"
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        
        // Màu cho đường "Đơn hàng"
        renderer.setSeriesPaint(1, new Color(46, 204, 113));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(0, 300));
        add(chartPanel, BorderLayout.CENTER);
    }
}
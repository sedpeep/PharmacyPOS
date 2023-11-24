import java.awt.*;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class SalesReportPanel extends JPanel {
    private OrderDetailService orderDetailService;
    private JTabbedPane tabbedPane;
    private JPanel weeklySalesPanel;
    private JPanel monthlySalesPanel;
    private JPanel yearlySalesPanel;

    public SalesReportPanel(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        weeklySalesPanel = new JPanel(new BorderLayout());
        monthlySalesPanel = new JPanel(new BorderLayout());
        yearlySalesPanel = new JPanel(new BorderLayout());

        tabbedPane.addTab("Weekly Sales", null, weeklySalesPanel, "View weekly sales summary");
        tabbedPane.addTab("Monthly Sales", null, monthlySalesPanel, "View monthly sales summary");
        tabbedPane.addTab("Yearly Sales", null, yearlySalesPanel, "View yearly sales summary");

        add(tabbedPane, BorderLayout.CENTER);

        loadSalesData();
    }

    private void loadSalesData() {
        new Thread(() -> {
            try {
                final Map<String, Double> weeklySalesSummary = orderDetailService.getSalesSummaryByWeek();
                final Map<String, Double> monthlySalesSummary = orderDetailService.getSalesSummaryByMonth();
                final Map<String, Double> yearlySalesSummary = orderDetailService.getSalesSummaryByYear();

                SwingUtilities.invokeLater(() -> {
                    if (!weeklySalesSummary.isEmpty() && !monthlySalesSummary.isEmpty() && !yearlySalesSummary.isEmpty()) {
                        loadWeeklySalesChart(weeklySalesSummary);
                        loadMonthlySalesChart(monthlySalesSummary);
                        loadYearlySalesChart(yearlySalesSummary);
                    } else {
                        weeklySalesPanel.add(new JLabel("No data available for weekly sales."), BorderLayout.CENTER);
                    }
                    revalidate();
                    repaint();
                });
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error fetching sales data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void loadYearlySalesChart(Map<String, Double> yearlySalesSummary) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        yearlySalesSummary.forEach((year, sales) -> {
            dataset.addValue(sales, "Sales", year);
        });

        JFreeChart barChart = ChartFactory.createBarChart(
                "Year Sales Summary",
                "Year",
                "Sales ($)",
                dataset
        );

        customizeChart(barChart);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(350, 250));
        yearlySalesPanel.removeAll();
        yearlySalesPanel.add(chartPanel, BorderLayout.CENTER);
        yearlySalesPanel.revalidate();
        yearlySalesPanel.repaint();

    }

    private void loadMonthlySalesChart(Map<String, Double> monthlySalesSummary) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        monthlySalesSummary.forEach((month, sales) -> {
            dataset.addValue(sales, "Sales", month);
        });

        JFreeChart barChart = ChartFactory.createBarChart(
                "Monthly Sales Summary",
                "Month",
                "Sales ($)",
                dataset
        );

        customizeChart(barChart);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(350, 250));
        monthlySalesPanel.removeAll();
        monthlySalesPanel.add(chartPanel, BorderLayout.CENTER);
        monthlySalesPanel.revalidate();
        monthlySalesPanel.repaint();
    }
    private void loadWeeklySalesChart(Map<String, Double> weeklySalesSummary) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        weeklySalesSummary.forEach((week, sales) -> {
            dataset.addValue(sales, "Sales", week);
        });

        JFreeChart barChart = ChartFactory.createBarChart(
                "Weekly Sales Summary",
                "Week",
                "Sales ($)",
                dataset
        );

        customizeChart(barChart);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(350, 250)); // Adjusted for better visibility
        weeklySalesPanel.removeAll();
        weeklySalesPanel.add(chartPanel, BorderLayout.CENTER);
        weeklySalesPanel.revalidate();
        weeklySalesPanel.repaint();
    }

    private void customizeChart(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(63, 104, 129));

        CategoryAxis categoryAxis = plot.getDomainAxis();
        categoryAxis.setMaximumCategoryLabelWidthRatio(0.8f);
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


    }
}

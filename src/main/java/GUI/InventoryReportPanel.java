package GUI;
import DAOLayer.*;
import ServiceLayer.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;

public class InventoryReportPanel extends JPanel {
    private JTable categorySummaryTable;
    private JTable lowStockTable;
    private ProductService productService;
    private final int LOW_STOCK_THRESHOLD = 10;
    private JLabel inventoryValueLabel;
    private JPanel barChartPanel;

    public InventoryReportPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel categoryTitle = new JLabel("Inventory Value by Category");
        categoryTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
        titlePanel.add(categoryTitle);

        JLabel lowStockTitle = new JLabel("Low Stock Items");
        lowStockTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
        titlePanel.add(lowStockTitle);

        add(titlePanel, BorderLayout.NORTH);

        initializeTables();
        loadInventoryData();

    }

    private void initializeTables() {
//
//        JLabel categoryTitle = new JLabel("Inventory Value by DAOLayer.Category");
//        categoryTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
//        add(categoryTitle, BorderLayout.NORTH);
//
//
//        JLabel lowStockTitle = new JLabel("Low Stock Items");
//        lowStockTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
//        add(lowStockTitle, BorderLayout.EAST);


        String[] categorySummaryColumns = {"DAOLayer.Category", "Number of Items", "Inventory Value"};
        DefaultTableModel categorySummaryModel = new DefaultTableModel(categorySummaryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This will make the table non-editable
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Double.class : String.class;
            }
        };
        categorySummaryTable = new JTable(categorySummaryModel);
        customizeTableHeader(categorySummaryTable);


        String[] lowStockColumns = {"Category ID", "Product ID", "Quantity"};
        DefaultTableModel lowStockModel = new DefaultTableModel(lowStockColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lowStockTable = new JTable(lowStockModel);
        customizeTableHeader(lowStockTable);


        add(new JScrollPane(categorySummaryTable), BorderLayout.CENTER);
        add(new JScrollPane(lowStockTable), BorderLayout.EAST);
    }

    private void customizeTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 18));
    }

    private void loadInventoryData() {
        Map<String, Integer> categoryItemCount = new HashMap<>();
        Map<String, Double> categoryValue = new HashMap<>();
        double totalValue = 0;

        List<Product> products = productService.getAllProducts();
        if (products == null) {
            JOptionPane.showMessageDialog(this, "Error fetching products.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel categorySummaryModel = (DefaultTableModel) categorySummaryTable.getModel();
        DefaultTableModel lowStockModel = (DefaultTableModel) lowStockTable.getModel();
        categorySummaryModel.setRowCount(0);
        lowStockModel.setRowCount(0);

        for (Product product : products) {
            double value = product.getPrice() * product.getQuantity();
            totalValue += value;
            String category = productService.getCategoryName(product.getCategoryId());

            categoryItemCount.put(category, categoryItemCount.getOrDefault(category, 0) + 1);
            categoryValue.put(category, categoryValue.getOrDefault(category, 0.0) + value);

            if (product.getQuantity() < LOW_STOCK_THRESHOLD) {
                lowStockModel.addRow(new Object[]{product.getCategoryId(), product.getProductId(), product.getQuantity()});
            }
        }

        for (Map.Entry<String, Integer> entry : categoryItemCount.entrySet()) {
            String category = entry.getKey();
            int itemCount = entry.getValue();
            Double value = categoryValue.getOrDefault(category, 0.0);
            categorySummaryModel.addRow(new Object[]{category, itemCount, value});
        }

        Double totalInventoryValue = categoryValue.values().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
        categorySummaryModel.addRow(new Object[]{"Total Inventory Value", "", totalInventoryValue});
        categorySummaryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row == table.getModel().getRowCount() - 1) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setBackground(new Color(0, 128, 0));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
        add(createBarChartPanel(categoryItemCount), BorderLayout.SOUTH);
        revalidate();
    }


    private JPanel createBarChartPanel(Map<String, Integer> categoryItemCount) {
        return new CustomBarChart(categoryItemCount);
    }
    private class CustomBarChart extends JPanel {
        private final Map<String, Integer> data;

        public CustomBarChart(Map<String, Integer> data) {
            this.data = data;
            setPreferredSize(new Dimension(400, 200));
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                return;
            }

            int maxCount = data.values().stream().max(Integer::compareTo).orElse(0);
            int barWidth = getWidth() / data.size();
            int barIndex = 0;


            int nameYPosition = getHeight() - 5;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue();
                int barHeight = (int) ((getHeight() - 20) * ((double) value / maxCount));
                int x = barIndex * barWidth + 10;
                int y = getHeight() - barHeight - 10;


                g.setColor(new Color(51, 255, 255));
                g.fillRect(x, y, barWidth - 20, barHeight);


                g.setColor(Color.BLACK);
                g.drawString(entry.getKey() + ": " + value, x, nameYPosition);

                barIndex++;
            }
            }
        }



}

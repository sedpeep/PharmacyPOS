import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class InventoryReportPanel extends JPanel {
    private JTable inventoryReportTable;
    private ProductService productService;
    private final int LOW_STOCK_THRESHOLD = 10;
    private JLabel inventoryValueLabel;

    public InventoryReportPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout());
        initializeInventoryReportTable();
    }
    private void customizeTableHeader() {
        JTableHeader header = inventoryReportTable.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 25));
    }
    private void initializeInventoryReportTable() {
        String[] columnNames = {"Product ID", "Name", "Quantity", "Expiration Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        inventoryReportTable = new JTable(tableModel);
        customizeTableHeader();
        loadInventoryData(tableModel);
        add(new JScrollPane(inventoryReportTable), BorderLayout.CENTER);
        inventoryValueLabel = new JLabel("Total Inventory Value: $0.00");
        add(inventoryValueLabel, BorderLayout.NORTH);
    }

    private void loadInventoryData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        double totalValue = 0;
        List<Product> products = productService.getAllProducts();
        if (products == null) {
            JOptionPane.showMessageDialog(this, "Error fetching products.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Product product : products) {
            double value = product.getPrice() * product.getQuantity();
            totalValue += value;
            Object[] row = new Object[]{
                    product.getProductId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getExpirationDate().toString(),
            };
            tableModel.addRow(row);
            if (product.getQuantity() < LOW_STOCK_THRESHOLD) {
                inventoryReportTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        int quantity = (Integer) table.getModel().getValueAt(row, 2); // Adjust index as per your table model
                        if (quantity < LOW_STOCK_THRESHOLD) {
                            c.setBackground(Color.YELLOW);
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                        return c;
                    }
                });
            }
        }
       // inventoryValueLabel.setText("Total Inventory Value: $" + String.format("%.2f", totalValue));
    }
}

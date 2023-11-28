package GUI;

import ServiceLayer.*;
import DAOLayer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class InventoryManagementPanel extends JPanel {
    private JTable inventoryTable;
    private JButton replenishStockButton;
    private JButton removeExpiredProductsButton;
    private ProductService productService;

    public InventoryManagementPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout());

        initializeInventoryTable();

        replenishStockButton = new JButton("Replenish Stock");
        replenishStockButton.addActionListener(e -> replenishStock());


        removeExpiredProductsButton = new JButton("Remove Expired Products");
        removeExpiredProductsButton.addActionListener(e -> removeExpiredProducts());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(replenishStockButton);
        buttonPanel.add(removeExpiredProductsButton);
        add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void customizeTableHeader() {
        JTableHeader header = inventoryTable.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 25));
    }
    private void initializeInventoryTable() {
        String[] columnNames = {"Product ID", "Category ID", "Name", "Quantity", "Expiration Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = new JTable(tableModel);
        customizeTableHeader();
        loadInventoryData(tableModel);
    }
    private void loadInventoryData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Products> products = productService.getAllProducts();
        for (Products product : products) {
            Object[] row = new Object[]{
                    product.getProductId(),
                    product.getCategoryId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getExpirationDate().toString()
            };
            tableModel.addRow(row);
        }
    }

    private void replenishStock() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to replenish.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (Integer) inventoryTable.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:", "Replenish Stock", JOptionPane.PLAIN_MESSAGE);
        try {
            int newQuantity = Integer.parseInt(quantityStr);
            if (newQuantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                return;
            }

            productService.updateProductQuantity(productId, newQuantity);
            loadInventoryData((DefaultTableModel) inventoryTable.getModel());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void removeExpiredProducts() {
        int removedCount = productService.deleteExpiredProducts();
        if (removedCount > 0) {
            JOptionPane.showMessageDialog(this, removedCount + " expired products removed.", "Expired Products Removed", JOptionPane.INFORMATION_MESSAGE);
            loadInventoryData((DefaultTableModel) inventoryTable.getModel());
        } else {
            JOptionPane.showMessageDialog(this, "No expired products found.", "No Action Required", JOptionPane.INFORMATION_MESSAGE);
        }
    }



}

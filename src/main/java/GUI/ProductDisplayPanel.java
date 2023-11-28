package GUI;

import ServiceLayer.*;
import DAOLayer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ProductDisplayPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private ProductService productService;
    private JButton addProductButton;
    private JButton updateProductButton;
    private JButton deleteProductButton;
    private int currentCategoryId;

    public ProductDisplayPanel(ProductService productService, int categoryId) {
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.productService = productService;
        this.currentCategoryId=categoryId;
        setLayout(new BorderLayout());
        addProductButton = new JButton("Add Product");
        updateProductButton = new JButton("Update Product");
        deleteProductButton = new JButton("Delete Product");

        addProductButton.addActionListener(e -> addProduct());
        updateProductButton.addActionListener(e -> updateProduct());
        deleteProductButton.addActionListener(e -> deleteProduct());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(updateProductButton);
        buttonPanel.add(deleteProductButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        initializeProductTable();
        loadProducts(categoryId);
    }

    private void initializeProductTable() {
        productTableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Description", "Price", "Quantity", "Expiration Date"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        productTable = new JTable(productTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    private void customizeTableHeader() {
        JTableHeader header =productTable.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 20));
    }
    private void loadProducts(int categoryId) {
        productTableModel.setRowCount(0);
        customizeTableHeader();
        List<Products> products = productService.getAllProductsByID(categoryId);
        for (Products product : products) {
            productTableModel.addRow(new Object[]{product.getProductId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getExpirationDate()});
        }
    }

    public boolean isValidExpirationDate(Date expirationDate) {

        LocalDate expDate = expirationDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return expDate.isAfter(currentDate);
    }

    private void addProduct() {

        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField expirationDateField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        panel.add(expirationDateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {

                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                Date expirationDate = Date.valueOf(expirationDateField.getText());
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isValidExpirationDate(expirationDate)) {
                    JOptionPane.showMessageDialog(this, "Expiration date must be later than the current date.", "Invalid Expiration Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Products newProduct = new Products(0,name, description, price, quantity, currentCategoryId, expirationDate);
                boolean success = productService.addProduct(newProduct);
                if (success) {
                    loadProducts(currentCategoryId);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid product information.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int productId = (Integer) productTableModel.getValueAt(selectedRow, 0);
            String currentName = (String) productTableModel.getValueAt(selectedRow, 1);
            String currentDescription = (String) productTableModel.getValueAt(selectedRow, 2);
            String currentPrice = productTableModel.getValueAt(selectedRow, 3).toString();
            String currentQuantity = productTableModel.getValueAt(selectedRow, 4).toString();
            String currentExpirationDate = productTableModel.getValueAt(selectedRow, 5).toString();

            JTextField nameField = new JTextField(currentName);
            JTextField descriptionField = new JTextField(currentDescription);
            JTextField priceField = new JTextField(currentPrice);
            JTextField quantityField = new JTextField(currentQuantity);
            JTextField expirationDateField = new JTextField(currentExpirationDate);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Quantity:"));
            panel.add(quantityField);
            panel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
            panel.add(expirationDateField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newName = nameField.getText();
                    String newDescription = descriptionField.getText();
                    double newPrice = Double.parseDouble(priceField.getText());
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    Date newExpirationDate = Date.valueOf(expirationDateField.getText());
                    if (newPrice <= 0) {
                        JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (newQuantity <= 0) {
                        JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!isValidExpirationDate(newExpirationDate)) {
                        JOptionPane.showMessageDialog(this, "Expiration date must be later than the current date.", "Invalid Expiration Date", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Products updatedProduct = new Products(0,newName, newDescription, newPrice, newQuantity, currentCategoryId, newExpirationDate);
                    updatedProduct.setProductId(productId);

                    boolean success = productService.updateProduct(updatedProduct);
                    if (success) {
                        loadProducts(currentCategoryId);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Please enter valid product information.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int productId = (Integer) productTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = productService.deleteProduct(productId);
                if (success) {
                    loadProducts(currentCategoryId);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public DefaultTableModel getProductTableModel() {
        return productTableModel;
    }

    public JTable getProductTable() {
        return productTable;
    }

    public JButton getUpdateProductButton() {
        return updateProductButton;
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }
    public void addProduct(Products p,int id){
        boolean sucess = productService.addProduct(p);
        if(sucess){
            loadProducts(id);
        }
    }
    public void updateProduct(Products p,int id){
        boolean sucess = productService.updateProduct(p);
        if(sucess){
            loadProducts(id);
        }
    }
    public void deleteProduct(int categoryID,int id){
        boolean sucess = productService.deleteProduct(id);
        if(sucess){
            loadProducts(categoryID);
        }
    }

}

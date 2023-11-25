package GUI;

import ServiceLayer.*;
import DAOLayer.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class CategoryManagementPanel extends JPanel {
    private JTable categoryTable;
    private JButton addCategoryButton;
    private JButton updateCategoryButton;
    private JButton deleteCategoryButton;
    private JButton showProductsButton;
    private DefaultTableModel categoryTableModel;
    private CategoryService categoryService;

    public CategoryManagementPanel(CategoryService categoryService) {
        this.categoryService = categoryService;
        setLayout(new BorderLayout());
        initializeCategoryTable();
        loadCategories();

        addCategoryButton = new JButton("Add Category");
        updateCategoryButton = new JButton("Update Category");
        deleteCategoryButton = new JButton("Delete Category");
        showProductsButton= new JButton("Show Products");


        // Action listeners for buttons
        addCategoryButton.addActionListener(e -> addCategory());
        updateCategoryButton.addActionListener(e -> updateCategory());
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        showProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = categoryTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int categoryId = (Integer) categoryTableModel.getValueAt(selectedRow, 0);
                    displayProductsForCategory(categoryId);
                }
            }
        });

        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = categoryTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int categoryId = (Integer) categoryTableModel.getValueAt(selectedRow, 0);
                        displayProductsForCategory(categoryId);
                    }
                }
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(updateCategoryButton);
        buttonPanel.add(deleteCategoryButton);
        buttonPanel.add(showProductsButton);

        add(new JScrollPane(categoryTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeCategoryTable() {
        categoryTableModel = new DefaultTableModel(new Object[]{"Category ID", "Category Name"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        categoryTable = new JTable(categoryTableModel);

        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    private void customizeTableHeader() {
        JTableHeader header = categoryTable.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 25));
    }
    private void displayProductsForCategory(int categoryId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS", "root", "");
            ProductService productService = new ProductService(connection);
            ProductDisplayPanel productDisplayPanel = new ProductDisplayPanel(productService, categoryId);

            JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
            if (frame != null) {
                Container frameContainer = frame.getContentPane();
                frameContainer.removeAll();
                frameContainer.add(productDisplayPanel);
                frameContainer.revalidate();
                frameContainer.repaint();
            } else {
                System.out.println("No parent frame found for GUI.CategoryManagementPanel.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void loadCategories() {
        categoryTableModel.setRowCount(0);
        customizeTableHeader();
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            categoryTableModel.addRow(new Object[]{category.getCategoryId(), category.getCategoryName()});
        }
    }

    private void addCategory() {
        JTextField categoryNameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Category Name:"));
        panel.add(categoryNameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameField.getText();

            if (!categoryName.isEmpty()) {
                Category newCategory = new Category(categoryName);
                boolean success = categoryService.addCategory(newCategory);
                if (success) {
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            int categoryId = (Integer) categoryTableModel.getValueAt(selectedRow, 0);
            String categoryName = (String) categoryTableModel.getValueAt(selectedRow, 1);

            JTextField categoryNameField = new JTextField(categoryName, 20);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Category Name:"));
            panel.add(categoryNameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newCategoryName = categoryNameField.getText();

                if (!newCategoryName.isEmpty()) {
                    Category updatedCategory = new Category(categoryId, newCategoryName);
                    boolean success = categoryService.updateCategory(updatedCategory);
                    if (success) {
                        loadCategories();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            int categoryId = (Integer) categoryTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this category?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = categoryService.deleteCategory(categoryId);
                if (success) {
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

}

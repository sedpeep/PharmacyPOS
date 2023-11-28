package Test;

import DAOLayer.Category;
import DAOLayer.Products;
import GUI.CategoryManagementPanel;
import GUI.ProductDisplayPanel;
import ServiceLayer.CategoryService;
import ServiceLayer.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CategoryManagementPanelTest {
    @Mock
    private CategoryService mockCategoryService;
    private CategoryManagementPanel categoryManagementPanel;
    private ProductDisplayPanel productDisplayPanel;
    @Mock
    private ProductService mockProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Category> categories = Arrays.asList(new Category("Category1"), new Category("Category2"));
        when(mockCategoryService.getAllCategories()).thenReturn(categories);
        categoryManagementPanel = new CategoryManagementPanel(mockCategoryService);
        productDisplayPanel = new ProductDisplayPanel(mockProductService,1);

    }

    @Test
    void testLoadCategories() {
        DefaultTableModel tableModel = (DefaultTableModel) categoryManagementPanel.getCategoryTableModel();
        assertEquals(2, tableModel.getRowCount());
        assertEquals("Category1", tableModel.getValueAt(0, 1));
        assertEquals("Category2", tableModel.getValueAt(1, 1));

        verify(mockCategoryService, times(1)).getAllCategories();
    }

    @Test
    void testAddCategory() {
        String newCategoryName = "New Category";
        Category newCategory = new Category(newCategoryName);

        List<Category> categories = Arrays.asList(new Category("Category1"), new Category("Category2"), new Category(newCategoryName));
        when(mockCategoryService.getAllCategories()).thenReturn(categories);
        when(mockCategoryService.addCategory(any(Category.class))).thenReturn(true);

        categoryManagementPanel.triggerAddCategory(newCategory);

        verify(mockCategoryService, times(1)).addCategory(any(Category.class));
        verify(mockCategoryService, times(2)).getAllCategories();


        DefaultTableModel tableModel = categoryManagementPanel.getCategoryTableModel();
        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(newCategoryName)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Category should be added to the table");

    }
    @Test
    void testUpdateCategory() {
        Category originalCategory = new Category(1, "Original Category");
        Category updatedCategory = new Category(1, "Updated Category");

        when(mockCategoryService.updateCategory(any(Category.class))).thenReturn(true);
        // The list returned by getAllCategories should include the updated category
        List<Category> categories = Arrays.asList(updatedCategory, new Category(2, "Category2"));
        when(mockCategoryService.getAllCategories()).thenReturn(categories);

        categoryManagementPanel.triggerUpdateCategory(updatedCategory);

        verify(mockCategoryService, times(1)).updateCategory(any(Category.class));

        DefaultTableModel tableModel = categoryManagementPanel.getCategoryTableModel();
        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(updatedCategory.getCategoryName())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Category should be updated in the table");
    }
    @Test
    void testDeleteCategory() {
        int categoryIdToDelete = 1;
        Category categoryToDelete = new Category(categoryIdToDelete, "CategoryToDelete");

        when(mockCategoryService.deleteCategory(categoryIdToDelete)).thenReturn(true);
        // The list returned by getAllCategories should exclude the deleted category
        List<Category> categories = Arrays.asList(new Category(2, "Category2"));
        when(mockCategoryService.getAllCategories()).thenReturn(categories);

        categoryManagementPanel.triggerDeleteCategory(categoryToDelete);

        verify(mockCategoryService, times(1)).deleteCategory(categoryIdToDelete);

        DefaultTableModel tableModel = categoryManagementPanel.getCategoryTableModel();
        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Integer) tableModel.getValueAt(i, 0) == categoryIdToDelete) {
                found = true;
                break;
            }
        }
        assertFalse(found, "Category should be deleted from the table");
    }
    @Test
    void testDisplayProductsForCategory(){
       categoryManagementPanel.displayProductsForCategory(1);
       verify(mockProductService).getAllProductsByID(1);
    }
    @Test
    void testAddProduct(){
        Products newProduct = new Products( 0,"New Product", "Description", 10.0, 5, 1, java.sql.Date.valueOf("2023-12-21"));
        when(mockProductService.addProduct(newProduct)).thenReturn(true);
        productDisplayPanel.addProduct(newProduct,1);
        verify(mockProductService).addProduct(newProduct);

        DefaultTableModel tableModel = productDisplayPanel.getProductTableModel();
        boolean productFound = true;
        for(int i = 0 ; i<tableModel.getRowCount(); i++){
            String productName = (String) tableModel.getValueAt(i,1);
            if(productName.equals("New Product")){
                productFound = true;
                break;
            }
        }
        assertTrue(productFound,"The new product should exist in table");
    }
    @Test
    void testUpdateProduct(){
        Products upadtedProduct = new Products(0,"Updated Product", "Description", 10.0, 5, 1, java.sql.Date.valueOf("2023-12-21"));
        when(mockProductService.updateProduct(upadtedProduct)).thenReturn(true);

        productDisplayPanel.updateProduct(upadtedProduct,1);
        verify(mockProductService).updateProduct(upadtedProduct);
        DefaultTableModel tableModel = productDisplayPanel.getProductTableModel();
        boolean productFound = true;
        for(int i = 0 ; i<tableModel.getRowCount(); i++){
            String productName = (String) tableModel.getValueAt(i,1);
            if(productName.equals("Updated Product")){
                productFound = true;
                break;
            }
        }
        assertTrue(productFound,"The updated product should exist in table");
    }
    @Test
    void testDeleteProduct(){
        int productIdToDelete = 1;
        Products productToDelete = new Products(productIdToDelete, "Product to Delete", "Description", 10.0, 5, 1, java.sql.Date.valueOf("2023-12-21"));


        when(mockProductService.deleteProduct(productIdToDelete)).thenReturn(true);


        DefaultTableModel tableModel = productDisplayPanel.getProductTableModel();
        tableModel.addRow(new Object[]{productToDelete.getProductId(), productToDelete.getName(), productToDelete.getDescription(), productToDelete.getPrice(), productToDelete.getQuantity(), productToDelete.getExpirationDate()});


        boolean productFoundPreDelete = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Integer) tableModel.getValueAt(i, 0) == productIdToDelete) {
                productFoundPreDelete = true;
                break;
            }
        }
        assertTrue(productFoundPreDelete, "The product to be deleted should initially exist in the table.");


        productDisplayPanel.deleteProduct(1,productIdToDelete);


        verify(mockProductService).deleteProduct(productIdToDelete);


        boolean productFoundPostDelete = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Integer) tableModel.getValueAt(i, 0) == productIdToDelete) {
                productFoundPostDelete = true;
                break;
            }
        }
        assertFalse(productFoundPostDelete, "The product should be deleted from the table.");
    }
}


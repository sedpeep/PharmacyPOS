package ServiceLayer;
import DAOLayer.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    ProductDAO productDAO;
    private List<Products> productList;
    public ProductService(Connection connection){
        productDAO = new ProductDAO(connection);
        this.productList = new ArrayList<>();
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        productList = productDAO.loadProductsFromDatabase();
    }
    public Products getProductAtRow(int row) {
        if (row >= 0 && row < productList.size()) {
            return productList.get(row);
        } else {
            return null;
        }
    }
    public int getProductQuantity(int productID){
        return productDAO.getProductsQuantity(productID);
    }

    public List<Products> getAllProductsByID(int id){
        return productDAO.getProductsByCategory(id);
    }
    public boolean addProduct(Products p){
        return  productDAO.addProduct(p);
    }
    public boolean updateProduct(Products p){
        return  productDAO.updateProduct(p);
    }
    public boolean deleteProduct(int id){
        return productDAO.deleteProduct(id);
    }
    public List<Products> getAllProducts(){
        return productDAO.getAllProducts();
    }
    public  boolean updateProductQuantity(int productID, int newQuantity){
        return productDAO.updateProductQuantity(productID,newQuantity);
    }
    public int deleteExpiredProducts(){
        return productDAO.deleteExpiredProducts();
    }
    public String getCategoryName(int id){
        return productDAO.getCategoryName(id);
    }
    public List<Products> searchProducts(String name, String category) throws SQLException{ return productDAO.searchProducts(name,category);}
}

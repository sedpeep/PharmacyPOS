import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    ProductDAO productDAO;
    private List<Product> productList;
    public ProductService(Connection connection){
        productDAO = new ProductDAO(connection);
        this.productList = new ArrayList<>();
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        productList = productDAO.loadProductsFromDatabase();
    }
    public Product getProductAtRow(int row) {
        if (row >= 0 && row < productList.size()) {
            return productList.get(row);
        } else {
            return null;
        }
    }
    public int getProductQuantity(int productID){
        return productDAO.getProductsQuantity(productID);
    }

    public List<Product> getAllProductsByID(int id){
        return productDAO.getProductsByCategory(id);
    }
    public boolean addProduct(Product p){
        return  productDAO.addProduct(p);
    }
    public boolean updateProduct(Product p){
        return  productDAO.updateProduct(p);
    }
    public boolean deleteProduct(int id){
        return productDAO.deleteProduct(id);
    }
    public List<Product> getAllProducts(){
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
    public List<Product> searchProducts(String name, String category) throws SQLException{ return productDAO.searchProducts(name,category);}
}

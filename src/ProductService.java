import java.sql.Connection;
import java.util.List;

public class ProductService {
    ProductDAO productDAO;
    public ProductService(Connection connection){
        productDAO = new ProductDAO(connection);
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
}

package ServiceLayer;

import DAOLayer.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(Connection connection){
        categoryDAO= new CategoryDAO(connection);
    }
    public List<Category> getAllCategories(){
        return categoryDAO.getAllCategories();
    }
    public boolean addCategory(Category category){
        return categoryDAO.addCategory(category);
    }
    public boolean updateCategory(Category category){
        return categoryDAO.updateCategory(category);
    }
    public boolean deleteCategory(int id){
        return  categoryDAO.deleteCategory(id);
    }
    public List<String> getAllCategoryName() throws SQLException {
        return categoryDAO.getAllCategoriesNames();
    }
    public List<Products> getAllProductsByCategoryID(int categoryId){
        return categoryDAO.getAllProductsByCategoryID(categoryId);
    }
}

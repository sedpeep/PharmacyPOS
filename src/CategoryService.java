import java.sql.Connection;
import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;

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
}

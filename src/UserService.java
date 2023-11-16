import java.sql.Connection;
import java.util.*;

public class UserService {
    private UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }

    public User authenticate(String username, String password) {
        return userDAO.verifyLogin(username, password);
    }
    public boolean addUser(User u){
        return userDAO.addUser(u);
    }
    public boolean updateUser(User user){
        return userDAO.updateUser(user);
    }
    public  boolean deleteUser(User user){
        return  userDAO.deleteUser(user);
    }
    public List<User> getAllUsers(){
        return userDAO.getAllUsers();
    }
}

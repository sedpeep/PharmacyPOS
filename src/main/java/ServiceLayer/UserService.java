package ServiceLayer;

import DAOLayer.*;
import java.sql.Connection;
import java.util.*;

public class UserService {
    private UserDAO userDAO;
    private User currentUser;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }
    public void setCurrentUser(User user){
        this.currentUser=user;
    }
    public int getCurrentUserID(){
        if (currentUser!=null){
            return currentUser.getUserID();
        }else {
            throw new IllegalStateException("No user currently logged in");
        }
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
    public  boolean deleteUser(String username){
        return  userDAO.deleteUser(username);
    }
    public User getUserByUsername(String username){
        return userDAO.getUserByUsername(username);
    }
    public List<User> getAllUsers(){
        return userDAO.getAllUsers();
    }
}

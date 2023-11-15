import java.sql.Connection;

public class UserService {
    private UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }

    public User authenticate(String username, String password) {
        return userDAO.verifyLogin(username, password);
    }
}

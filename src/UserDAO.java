import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    public User verifyLogin(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE username = ? AND password = (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                // Assuming we don't want to retrieve the password for security reasons
                user.setRole(rs.getString("role")); // Directly get the role as a string
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
class User {
    private int userID;
    private String username;
    private String password;
    private String role;

    public User(){}
    public User(String username, String password, String role){
        this.password=password;
        this.username=username;
        this.role=role;
    }
    public String getRole() {
        return role;
    }

    public int getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
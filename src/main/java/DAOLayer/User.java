package DAOLayer;

public class User {
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
    @Override
    public String toString() {
        return this.username + " (" + this.role + ")";
    }
}
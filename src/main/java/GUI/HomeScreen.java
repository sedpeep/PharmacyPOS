package GUI;

import DAOLayer.User;
import ServiceLayer.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class HomeScreen extends JFrame {
    private JLabel welcomeLabel;
    private JLabel selectRoleLabel;
    private JComboBox<String> roleComboBox;
    private JLabel usernameLabel;
    private JTextField usernameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;
    private User currentUser;
    private boolean loginSuccess;
    private String errorMessage;
    private JPanel MainFrame;
    private JTextField passwordTextField;


    public HomeScreen(UserService userService) {

        super("Welcome to POS");
        this.setSize(500,500);
        this.userService = userService;
        //this.productService= new ServiceLayer.ProductService();
        initializeComponents();
        initializeListeners();


        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        welcomeLabel = new JLabel("Welcome to POS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 120, 215));
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        selectRoleLabel = new JLabel("Select your role:");
        roleComboBox = new JComboBox<>(new String[]{"Sales Assistant", "Manager"});
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernameLabel = new JLabel("Username:");
        usernameTextField = new JTextField(20);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        loginButton = new JButton("Login");
        loginPanel.add(selectRoleLabel);
        loginPanel.add(roleComboBox);
        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(loginButton);
        // Add the login panel to the frame
        add(loginPanel, BorderLayout.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

    }
    private void initializeListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loginSuccess = false;
                errorMessage = "";

                String selectedRole = (String) roleComboBox.getSelectedItem();
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    errorMessage = "Username and password fields cannot be empty.";
                    JOptionPane.showMessageDialog(HomeScreen.this,
                            errorMessage,
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!"Manager".equals(selectedRole) && !"Sales Assistant".equals(selectedRole)) {
                    loginSuccess = false;
                    errorMessage = "Incorrect role selected.";
                    return;
                }

                User user = userService.authenticate(username, password);
                if (user != null && user.getRole().equalsIgnoreCase(selectedRole)) {
                    // Successful login
                    loginSuccess = true;
                    currentUser = user;
                    if ("Manager".equalsIgnoreCase(selectedRole)) {
                        userService.setCurrentUser(user);
                        ManagerDashboard managerDashboard = new ManagerDashboard(userService);
                        managerDashboard.setVisible(true);
                    } else if ("Sales Assistant".equalsIgnoreCase(selectedRole)) {
                        userService.setCurrentUser(user);
                        try {
                            SalesAssistantDashboard salesDashboard = new SalesAssistantDashboard(userService);
                            salesDashboard.setVisible(true);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    HomeScreen.this.dispose();
                } else {

                    loginSuccess = false;
                    errorMessage = "Invalid username or password, or incorrect role selected.";
                    JOptionPane.showMessageDialog(HomeScreen.this,
                            errorMessage,
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JComboBox<String> getRoleComboBox() {
        return roleComboBox;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isSuccessfulLogin() {
        return loginSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            UserService userService = new UserService(connection);
            HomeScreen homeScreen = new HomeScreen(userService);
            homeScreen.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

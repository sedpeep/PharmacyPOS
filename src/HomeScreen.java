import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public HomeScreen(UserService userService) {
        super("Welcome to POS");
        this.userService = userService;
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
                String selectedRole = (String) roleComboBox.getSelectedItem();
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());

                User user = userService.authenticate(username, password);
                if (user != null && user.getRole().equalsIgnoreCase(selectedRole)) {
                    // Successful login
                    JOptionPane.showMessageDialog(HomeScreen.this,
                            "Login successful! Welcome, " + selectedRole,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    if("Manager".equalsIgnoreCase(selectedRole)){
                        ManagerDashboard managerDashboard = new ManagerDashboard();
                        managerDashboard.setVisible(true);
                        HomeScreen.this.dispose();
                    }

                } else {
                    // Failed login
                    JOptionPane.showMessageDialog(HomeScreen.this,
                            "Invalid username or password, or incorrect role selected.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }



    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","12345678");
            UserService userService = new UserService(connection);
            HomeScreen homeScreen = new HomeScreen(userService);
            homeScreen.setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JLabel usernameLabel;
    private JTextField usernameTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String role;
    private UserService userService;

    public LoginScreen(UserService userService, String role) {
        super(role + " Login");
        this.userService = userService;
        this.role = role;
        initializeComponents();
        layoutComponents();
        initializeListeners();
    }

    private void initializeComponents() {
        usernameLabel = new JLabel("Username:");
        usernameTextField = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
    }

    private void layoutComponents() {
        setLayout(new GridLayout(3, 2, 5, 5));
        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());

                User user = userService.authenticate(username, password);
                if (user != null && user.getRole().equalsIgnoreCase(role)) {
                    // Successful login
                    JOptionPane.showMessageDialog(LoginScreen.this,
                            "Login successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Proceed to the next screen based on the role
                } else {
                    // Failed login
                    JOptionPane.showMessageDialog(LoginScreen.this,
                            "Invalid username or password.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

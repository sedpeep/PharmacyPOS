package Test;

import DAOLayer.User;
import GUI.HomeScreen;
import ServiceLayer.UserService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

class HomeScreenTest {

    private HomeScreen homeScreen;
    private UserService mockUserService;
    private User mockManagerUser;
    private User mockAssistantUser;

    @BeforeEach
    void setUp() {
        mockUserService = mock(UserService.class);
        mockManagerUser = new User("manager", "password123", "Manager");
        mockAssistantUser = new User("assistant1", "password123", "Sales Assistant");

        homeScreen = new HomeScreen(mockUserService);
    }

    @Test
    @DisplayName("Manager logs in successfully")
    void testManagerSuccessfulLogin() {
        when(mockUserService.authenticate("manager", "password123")).thenReturn(mockManagerUser);

        homeScreen.getUsernameTextField().setText("manager");
        homeScreen.getPasswordField().setText("password123");
        homeScreen.getRoleComboBox().setSelectedItem("Manager");

        homeScreen.getLoginButton().doClick();

        verify(mockUserService).authenticate("manager", "password123");
        // You can add assertions here to check if the ManagerDashboard was opened
    }

    @Test
    @DisplayName("Sales Assistant logs in successfully")
    void testAssistantSuccessfulLogin() {
        when(mockUserService.authenticate("assistant1", "password123")).thenReturn(mockAssistantUser);

        homeScreen.getUsernameTextField().setText("assistant1");
        homeScreen.getPasswordField().setText("password123");
        homeScreen.getRoleComboBox().setSelectedItem("Sales Assistant");

        homeScreen.getLoginButton().doClick();

        verify(mockUserService).authenticate("assistant1", "password123");
        // You can add assertions here to check if the SalesAssistantDashboard was opened
    }

    @Test
    @DisplayName("Failed login attempt with incorrect manager credentials")
    void testManagerFailedLogin() {
        when(mockUserService.authenticate("manager", "wrongpassword")).thenReturn(null);

        homeScreen.getUsernameTextField().setText("manager");
        homeScreen.getPasswordField().setText("wrongpassword");
        homeScreen.getRoleComboBox().setSelectedItem("Manager");

        homeScreen.getLoginButton().doClick();

        verify(mockUserService).authenticate("manager", "wrongpassword");

    }
    @DisplayName("Failed login attempt with incorrect assistant credentials")
    void testAssistantFailedLogin() {
        when(mockUserService.authenticate("assistant1", "wrongpassword")).thenReturn(null);

        homeScreen.getUsernameTextField().setText("assistant1");
        homeScreen.getPasswordField().setText("wrongpassword");
        homeScreen.getRoleComboBox().setSelectedItem("Sales Assistant");

        homeScreen.getLoginButton().doClick();

        verify(mockUserService).authenticate("assistant1", "wrongpassword");
        // You can add assertions here to check if an error message is shown
    }

    @Test
    @DisplayName("Failed login attempt with incorrect role")
    void testFailedLoginWithIncorrectRole() {
        // Assuming the roles are case-sensitive, and "manager" is not equal to "Manager"
        when(mockUserService.authenticate("manager", "password123")).thenReturn(mockManagerUser);

        homeScreen.getUsernameTextField().setText("manager");
        homeScreen.getPasswordField().setText("password123");
        homeScreen.getRoleComboBox().setSelectedItem("manager"); // Incorrect role case

        homeScreen.getLoginButton().doClick();

        verify(mockUserService, never()).authenticate("manager", "password123");
        // You can add assertions here to check if an error message is shown for incorrect role selection
    }

    @Test
    @DisplayName("Failed login attempt with empty credentials")
    void testFailedLoginWithEmptyCredentials() {
        homeScreen.getUsernameTextField().setText("");
        homeScreen.getPasswordField().setText("");
        homeScreen.getRoleComboBox().setSelectedItem("Manager");

        homeScreen.getLoginButton().doClick();

        verify(mockUserService, never()).authenticate("", "");
        // You can add assertions here to check if an error message is shown for empty credentials
    }

}

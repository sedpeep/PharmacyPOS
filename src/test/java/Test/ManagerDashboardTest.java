package Test;

import GUI.*;
import ServiceLayer.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ManagerDashboardTest {
    private ManagerDashboard dashboard;
    private UserManagementPanel mockUserManagementPanel;
    private ProductDisplayPanel mockProductManagementPanel;
    private InventoryManagementPanel mockInventoryManagementPanel;
    private SalesReportPanel mockSalesReportPanel;
    private InventoryReportPanel mockInventoryReportPanel;
    private JFrame mockFrame;

    @Mock
    private UserService mockUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dashboard = new ManagerDashboard(mockUserService);
        mockUserManagementPanel = mock(UserManagementPanel.class);
        mockProductManagementPanel = mock(ProductDisplayPanel.class);
        mockInventoryManagementPanel = mock(InventoryManagementPanel.class);
        mockSalesReportPanel = mock(SalesReportPanel.class);
        mockInventoryReportPanel = mock(InventoryReportPanel.class);
        mockFrame = mock(JFrame.class);
        dashboard.setVisible(true);
    }
    @Test
    @DisplayName("Main Page Displays Correctly")
    void testMainPageDisplay() {
        SwingUtilities.invokeLater(() -> dashboard.showMainPage());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertAll(
                () -> assertNotNull(dashboard.getCurrentPanel(), "Main panel should be present"),
                () -> assertTrue(dashboard.getCurrentPanel().getComponentCount() > 0, "Main panel should have components"),
                () -> assertEquals("Welcome to Manager Dashboard", dashboard.getWelcomeLabel().getText(), "Welcome label text should match")
        );
    }

    @Test
    @DisplayName("Manage Users Opens Correct Panel")
    public void testManageUsersDisplay() throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> dashboard.showUserManagementPanel(mockUserService));
            Thread.sleep(500);
        } catch (InterruptedException | InvocationTargetException e) {
            Thread.currentThread().interrupt();
        }assertAll(
                () -> assertNotNull(dashboard.getCurrentPanel(),"Current panel should not be null"),
                () -> assertTrue(dashboard.getCurrentPanel() instanceof UserManagementPanel, "UserManagementPanel should be displayed")
            );
    }


    @Test
    void testManageProductsDisplay() {
        dashboard.showProductManagementPanel();
        assertTrue(dashboard.getCurrentPanel() instanceof CategoryManagementPanel);
    }

    @Test
    void testManageInventoryDisplay() {
        dashboard.showInventoryManagementPanel();
        assertTrue(dashboard.getCurrentPanel() instanceof InventoryManagementPanel);
    }

    @Test
    void testSalesReportDisplay() {
        dashboard.showSalesReportPanel();
        assertTrue(dashboard.getCurrentPanel() instanceof SalesReportPanel);
    }

    @Test
    void testInventoryReportDisplay() {
        dashboard.showInventoryReportPanel();
        assertTrue(dashboard.getCurrentPanel() instanceof InventoryReportPanel);
    }

    @Test
    void testLogoutFunctionality() {
        SwingUtilities.invokeLater(() -> dashboard.returnToLogin());

        try {
            Thread.sleep(500); // Adjust as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertAll(
                () -> assertFalse(dashboard.isVisible(), "Dashboard should not be visible after logout"),
                () -> {
                    boolean foundHomeScreen = false;
                    for (Window window : Window.getWindows()) {
                        if (window instanceof HomeScreen && window.isVisible()) {
                            foundHomeScreen = true;
                            break;
                        }
                    }
                    assertTrue(foundHomeScreen, "HomeScreen should be visible after logout");
                }
        );
    }
}

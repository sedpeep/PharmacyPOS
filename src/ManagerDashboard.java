import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerDashboard extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu, manageMenu, reportsMenu, systemMenu;
    private JMenuItem exitItem, manageUsersItem, manageProductsItem, manageInventoryItem, salesReportItem, inventoryReportItem, systemSettingsItem;

    public ManagerDashboard(UserService userService) {
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents(userService);
    }

    private void initComponents(UserService userService) {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        // Manage Menu
        manageMenu = new JMenu("Manage");
        manageUsersItem = new JMenuItem("Users");
        manageProductsItem = new JMenuItem("Products");
        manageInventoryItem = new JMenuItem("Inventory");
        manageMenu.add(manageUsersItem);
        manageMenu.add(manageProductsItem);
        manageMenu.add(manageInventoryItem);

        reportsMenu = new JMenu("Reports");
        salesReportItem = new JMenuItem("Sales Reports");
        inventoryReportItem = new JMenuItem("Inventory Reports");
        reportsMenu.add(salesReportItem);
        reportsMenu.add(inventoryReportItem);


        systemMenu = new JMenu("System");
        systemSettingsItem = new JMenuItem("Settings");
        systemMenu.add(systemSettingsItem);


        menuBar.add(fileMenu);
        menuBar.add(manageMenu);
        menuBar.add(reportsMenu);
        menuBar.add(systemMenu);


        setJMenuBar(menuBar);
        initializeMenuListners(userService);


    }

    private void initializeMenuListners(UserService userService) {
        manageUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserManagementPanel(userService);
            }
        });
    }
    private void showUserManagementPanel(UserService userService) {
        UserManagementPanel userManagementPanel = new UserManagementPanel(userService);
        setContentPane(userManagementPanel);
        validate();
        repaint();
    }



}

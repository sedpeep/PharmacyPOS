import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class ManagerDashboard extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu, manageMenu, reportsMenu, systemMenu;
    private JMenuItem exitItem, manageUsersItem, manageProductsItem, manageInventoryItem, salesReportItem, inventoryReportItem, systemSettingsItem;
    private UserService user;
    public ManagerDashboard(UserService userService) {
        this.user=userService;
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
        manageProductsItem.addActionListener(e -> showProductManagementPanel());
        manageInventoryItem.addActionListener(e -> showInventoryManagementPanel());
        salesReportItem.addActionListener(e -> showSalesReportPanel());
        inventoryReportItem.addActionListener(e -> showInventoryReportPanel());
        salesReportItem.addActionListener(e-> showSalesReportPanel());
        exitItem.addActionListener(e -> returnToLogin());
    }

    private void showSalesReportPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            OrderDetailService orderDetailService = new OrderDetailService(connection);

            SalesReportPanel salesReportPanel = new SalesReportPanel(orderDetailService);
            setContentPane(salesReportPanel);
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void showInventoryReportPanel(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            ProductService productService = new ProductService(connection);
            if(productService == null){
                JOptionPane.showMessageDialog(this,"Eror fetching the products data");
            }
            InventoryReportPanel inventoryReportPanel = new InventoryReportPanel(productService);
            setContentPane(inventoryReportPanel);
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void returnToLogin() {

        HomeScreen loginScreen = new HomeScreen(user);
        loginScreen.setVisible(true);

        this.setVisible(false);
        this.dispose();
    }
    private void showProductManagementPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            CategoryService categoryService = new CategoryService(connection);
            CategoryManagementPanel productManagementPanel = new CategoryManagementPanel(categoryService);
            setContentPane(productManagementPanel);
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void showUserManagementPanel(UserService userService) {
        UserManagementPanel userManagementPanel = new UserManagementPanel(userService);
        setContentPane(userManagementPanel);
        validate();
        repaint();
    }
    private void showInventoryManagementPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS", "root", "");
            ProductService productService = new ProductService(connection);
            InventoryManagementPanel inventoryManagementPanel = new InventoryManagementPanel(productService);
            setContentPane(inventoryManagementPanel);
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}

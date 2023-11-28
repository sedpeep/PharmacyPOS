package GUI;

import ServiceLayer.CategoryService;
import ServiceLayer.OrderDetailService;
import ServiceLayer.ProductService;
import ServiceLayer.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerDashboard extends JFrame {
    private JMenuBar menuBar;
    private JPanel currentPanel;
    private JButton productsPage,userPage,inventoryPage,salesReport,inventoryReport;
    private JMenu fileMenu, manageMenu, reportsMenu, systemMenu;
    private JMenuItem mainPage,exitItem, manageUsersItem, manageProductsItem, manageInventoryItem, salesReportItem, inventoryReportItem, systemSettingsItem;
    private UserService user;
    private  JLabel welcome;
    public ManagerDashboard(UserService userService) {
        this.user=userService;
        this.currentPanel =  null;
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents(userService);
    }

    private void initComponents(UserService userService) {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        mainPage = new JMenuItem("Main Page");
        exitItem = new JMenuItem("Exit");
        fileMenu.add(mainPage);
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
        showMainPage();
        initializeMenuListners(userService);
    }

    private void initializeMenuListners(UserService userService) {
        manageUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserManagementPanel(userService);
            }
        });
        mainPage.addActionListener(e->showMainPage());
        manageProductsItem.addActionListener(e -> showProductManagementPanel());
        manageInventoryItem.addActionListener(e -> showInventoryManagementPanel());
        salesReportItem.addActionListener(e -> showSalesReportPanel());
        inventoryReportItem.addActionListener(e -> showInventoryReportPanel());
        salesReportItem.addActionListener(e-> showSalesReportPanel());
        exitItem.addActionListener(e -> returnToLogin());

        productsPage.addActionListener(e -> showProductManagementPanel());
        userPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserManagementPanel(userService);
            }
        });
        inventoryPage.addActionListener(e ->showInventoryManagementPanel());

        salesReport.addActionListener(e -> showSalesReportPanel());
        inventoryReport.addActionListener(e -> showInventoryReportPanel());
    }
    public void showMainPage(){
        this.getContentPane().removeAll();

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());



        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Top, left, bottom, right padding

         welcome = new JLabel("Welcome to Manager Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        gbc.anchor = GridBagConstraints.CENTER;
        mainMenuPanel.add(welcome, gbc);

        JLabel manageItems = new JLabel("Manage:", SwingConstants.LEFT);
        manageItems.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.anchor = GridBagConstraints.LINE_START;
        mainMenuPanel.add(manageItems, gbc);

        productsPage = new JButton("Products");
        userPage = new JButton("Users");
        inventoryPage = new JButton("Inventory");

        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainMenuPanel.add(productsPage, gbc);
        mainMenuPanel.add(userPage, gbc);
        mainMenuPanel.add(inventoryPage, gbc);

        JLabel reportGenerate = new JLabel("Reports:", SwingConstants.LEFT);
        reportGenerate.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.anchor = GridBagConstraints.LINE_START;
        mainMenuPanel.add(reportGenerate, gbc);

        salesReport = new JButton("Sales Report");
        inventoryReport = new JButton("Inventory Report");

        mainMenuPanel.add(salesReport, gbc);
        mainMenuPanel.add(inventoryReport, gbc);

        // Add some space before the last component if needed
        // gbc.weighty = 1; // Uncomment this line if you want the components to be at the top
        this.add(mainMenuPanel, BorderLayout.CENTER);
        this.currentPanel=mainMenuPanel;
        this.validate();
        this.repaint();

        initializeMenuListners(user);


    }
    public void showSalesReportPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            OrderDetailService orderDetailService = new OrderDetailService(connection);

            SalesReportPanel salesReportPanel = new SalesReportPanel(orderDetailService);
            setContentPane(salesReportPanel);
            this.currentPanel=salesReportPanel;
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void showInventoryReportPanel(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            ProductService productService = new ProductService(connection);
            if(productService == null){
                JOptionPane.showMessageDialog(this,"Eror fetching the products data");
            }
            InventoryReportPanel inventoryReportPanel = new InventoryReportPanel(productService);
            setContentPane(inventoryReportPanel);
            this.currentPanel=inventoryReportPanel;
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnToLogin() {

        HomeScreen loginScreen = new HomeScreen(user);
        loginScreen.setVisible(true);

        this.setVisible(false);
        this.dispose();
    }
    public void showProductManagementPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS","root","");
            CategoryService categoryService = new CategoryService(connection);
            CategoryManagementPanel productManagementPanel = new CategoryManagementPanel(categoryService);
            setContentPane(productManagementPanel);
            this.currentPanel=productManagementPanel;
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void showUserManagementPanel(UserService userService) {
        UserManagementPanel userManagementPanel = new UserManagementPanel(userService);
        setContentPane(userManagementPanel);
        this.currentPanel=userManagementPanel;
        validate();
        repaint();
    }
    public void showInventoryManagementPanel() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS", "root", "");
            ProductService productService = new ProductService(connection);
            InventoryManagementPanel inventoryManagementPanel = new InventoryManagementPanel(productService);
            setContentPane(inventoryManagementPanel);
            this.currentPanel= inventoryManagementPanel;
            validate();
            repaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public JLabel getWelcomeLabel(){
        return welcome;
    }
    public JPanel getCurrentPanel(){
        return currentPanel;
    }

}

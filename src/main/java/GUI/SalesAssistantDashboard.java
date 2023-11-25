package GUI;

import DAOLayer.Category;
import DAOLayer.Product;
import ServiceLayer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesAssistantDashboard extends JFrame {
    private static final int QUANTITY_COLUMN_INDEX = 2;
    private JTable cartTable;
    private JTable table;
    private JMenu fileMenu;
    private JMenuItem exitItem, processTransactionItem;
    private JPanel mainPanel;
    private JComboBox<String> categoryComboBox;
    private JTextField searchField;
    private JButton searchButton, cartButton;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private Cart cart;
    private JLabel totalLabel ;
    private JButton processOrderButton;
    private JMenuItem mainItem;
    private OrderService orderService;
    private OrderDetailService orderDetailService;
    private JPanel bottomPanel;

    public SalesAssistantDashboard(UserService user) throws SQLException {
        this.userService = user;
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PharmacyPOS", "root", "");

        productService = new ProductService(connection);
        categoryService = new CategoryService(connection);
        orderService = new OrderService(connection);
        orderDetailService = new OrderDetailService(connection);

        cart = new Cart();

        setTitle("Sales Assistant Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        totalLabel = new JLabel("Total:$"+cart.calculateTotal());
        initComponents();
        processOrderButton = new JButton("Process Order");
        initializeMenuListeners();
        setVisible(true);
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        exitItem = new JMenuItem("Exit");
        //mainMenu = new JMenu("Main Page");
        mainItem = new JMenu("Home Page");
        fileMenu.add(exitItem);

        JMenu transactionMenu = new JMenu("Transaction");
        processTransactionItem = new JMenuItem("Process Transaction");

        transactionMenu.add(processTransactionItem);

        menuBar.add(fileMenu);
        menuBar.add(transactionMenu);
        menuBar.add(mainItem);

        setJMenuBar(menuBar);

        mainPanel = new JPanel(new BorderLayout());
        //transactionPanel = createTransactionPanel();


        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>(getCategories());
        searchButton = new JButton("Search");
        cartButton = new JButton("Cart");

        searchPanel.add(searchField);
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(cartButton);


        JPanel productsPanel = createProductsPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));


        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        add(mainPanel, BorderLayout.CENTER);
       // initializeMenuListeners();
    }
    private void initializeMenuListeners() {
        processTransactionItem.addActionListener(e -> showTransactionPanel());
        exitItem.addActionListener(e -> returnToLogin());
        processOrderButton.addActionListener(e-> {
            try {
                processOrder();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        cartButton.addActionListener(e -> showTransactionPanel());
        mainItem.addActionListener(e->showMainPanel());

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                try {
                    List<Product> foundProducts = productService.searchProducts(searchText, selectedCategory);
                    updateProductTable(foundProducts);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error searching products: " + ex.getMessage(), "Search Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }
    private String[] getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        String[] categoryNames = new String[categories.size() + 1];
        categoryNames[0] = "All";

        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i + 1] = categories.get(i).getCategoryName();
        }

        return categoryNames;
    }


    private JPanel createProductsPanel() {
        JPanel productsPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Product ID", "Product Name", "Description", "DAOLayer.Category", "Price", "Add to Cart"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        table.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
        table.getColumn("Add to Cart").setCellEditor(new ButtonEditor(new JCheckBox(), cart, productService));


        loadProducts(model);

        JScrollPane scrollPane = new JScrollPane(table);
        productsPanel.add(scrollPane, BorderLayout.CENTER);

        return productsPanel;
    }
    public void showMainPanel() {
        mainPanel.removeAll();

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>(getCategories());
        searchButton = new JButton("Search");
        cartButton = new JButton("Cart");


        searchPanel.add(searchField);
        searchPanel.add(categoryComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(cartButton);

        mainPanel.add(searchPanel,BorderLayout.NORTH);

        JPanel productsPanel = createProductsPanel();
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        initializeMenuListeners();
        revalidate();
        repaint();
    }

    private void loadProducts(DefaultTableModel model) {
        try {
            List<Product> products = productService.getAllProducts();
            for (Product product : products) {
                model.addRow(new Object[]{
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategoryId(),
                        String.format("$%.2f", product.getPrice()),
                        "Add to Cart"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private Cart cart;
        private ProductService productService;

        public ButtonEditor(JCheckBox checkBox, Cart cart, ProductService productService) {
            super(checkBox);
            this.cart = cart;
            this.productService = productService;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "Add to Cart" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();
                Product product = productService.getProductAtRow(row);
                cart.addProduct(product);
                JOptionPane.showMessageDialog(button, label + ": Added to cart!");
                updateTotalLabel();
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private void updateProductTable(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Product product : products) {
            model.addRow(new Object[]{
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    "Add to Cart"
            });
        }

    }
    private void updateTotalLabel() {
        double total = cart.calculateTotal();
        totalLabel.setText("Total: " + String.format("$%.2f", total));
        System.out.println(total);
        totalLabel.revalidate();
        totalLabel.repaint();
    }
    private void processOrder() throws SQLException {
        if(cart ==null || cart.getItems().isEmpty()){
            JOptionPane.showMessageDialog(this,"Cart Empty! Add an item to process order");
            return;
        }
        else {
            try {
                int userID = userService.getCurrentUserID();
                int orderID = orderService.addOrder(userID, cart.calculateTotal());
                if (orderID > 0) {
                    for (Cart.CartItem item : cart.getItems()) {
                        boolean flag = orderDetailService.addOrderDetail(orderID, item.getProduct().getProductId(), item.getQuantity(), item.getProduct().getPrice());
                        if (!flag) {
                            throw new SQLException("Failed to add ordere detail for product!");
                        }
                    }

                }
                JOptionPane.showMessageDialog(this, "Order processed!");
                cart.clearCart();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to process to order: " + e.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void returnToLogin() {

        HomeScreen loginScreen = new HomeScreen(userService);
        loginScreen.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }
    private void showTransactionPanel() {

        mainPanel.removeAll();

        JLabel checkoutHeading = new JLabel("Checkout Page", SwingConstants.CENTER);
        checkoutHeading.setFont(new Font("Monospaced", Font.BOLD, 24));
        mainPanel.add(checkoutHeading, BorderLayout.NORTH);

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.addActionListener(e-> cart.clearCart());

        JPanel clearCartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearCartPanel.add(clearCartButton);
        mainPanel.add(clearCartPanel,BorderLayout.NORTH);


        String[] columnNames = {"ID", "Name", "Quantity", "Price", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columnNames,0);
        cartTable = new JTable(model){
            public Class getColumnClass(int column){
                return  getValueAt(0,column).getClass();
            }
        };
        cartTable.setRowHeight(600);

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex){
                    case 2:
                        return QuantityPanel.class;
                    default:
                        return super.getColumnClass(columnIndex);
                }
            }
        };
        cartTable = new JTable(model) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2) {
                    return new TableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                                                       boolean isSelected, boolean hasFocus, int row, int column) {
                            if ( value instanceof QuantityPanel) {
                                return (QuantityPanel) value;
                            }
                            return new DefaultTableCellRenderer().getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                        }
                    };
                }else {
                    return new DefaultTableCellRenderer();
                }
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2) {
                    return new DefaultCellEditor(new JCheckBox()) {
                        @Override
                        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                                     int row, int column) {
                            if (column == QUANTITY_COLUMN_INDEX && value instanceof QuantityPanel) {
                                return (Component) value;
                            }
                            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
                        }
                    };
                }
                return super.getCellEditor(row, column);
            }
        };

        for(Cart.CartItem item : cart.getItems()){
            QuantityPanel quantityPanel = new QuantityPanel(item);
            model.addRow(new Object[]{
                    item.getProduct().getProductId(),
                    item.getProduct().getName(),
                    quantityPanel,
                    String.format("$%.2f",item.getProduct().getPrice()),"Remove"
            });
        }
        cartTable.getColumn("Actions").setCellRenderer(new ButtonColumnRenderer());
        cartTable.getColumn("Actions").setCellEditor(new ButtonColumnEditor(cartTable, cart));

        cartTable.setRowHeight(40);
        cartTable.revalidate();
        cart.repaint();
        JScrollPane tableScrollpane = new JScrollPane(cartTable);
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.add(tableScrollpane,BorderLayout.CENTER);

        totalLabel = new JLabel("Total"+String.format("$%.2f",cart.calculateTotal()));
        totalLabel.setFont(new Font("Monospaced",Font.BOLD,20));

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        totalPanel.add(totalLabel,BorderLayout.EAST);

        receiptPanel.add(totalPanel,BorderLayout.SOUTH);

        mainPanel.add(receiptPanel,BorderLayout.CENTER);
        mainPanel.add(processOrderButton,BorderLayout.SOUTH);
        updateTotalLabel();
        revalidate();
        repaint();

    }
    class ButtonColumnRenderer extends JButton implements TableCellRenderer {
        JButton removeButton;
        public  ButtonColumnRenderer(){
            setOpaque(true);
            setLayout(new FlowLayout(FlowLayout.LEFT));
            removeButton = new JButton("Remove");
            add(removeButton);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Actions");
            return this;
        }
    }
    class ButtonColumnEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton removeButton;
        private Cart cart;
        private int currentRow;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }

        public ButtonColumnEditor(JTable table, Cart cart) {
            super(new JCheckBox());
            this.cart = cart;
            this.panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            removeButton = new JButton("Remove");

            panel.add(removeButton);


            removeButton.addActionListener(e -> {
                int editingRow = table.getEditingRow();
                int modelRow = table.convertRowIndexToModel(editingRow);
                if(table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                    updateTotalLabel();
                    table.revalidate();
                    table.repaint();
                }
                if(modelRow>=0){
                    cart.removeProduct(modelRow);
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    updateTotalLabel();
                    table.revalidate();
                    table.repaint();
                }
                updateTotalLabel();
                if(table.getRowCount()==0){
                    JOptionPane.showMessageDialog(this.getComponent(), "The cart is now empty.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
                }

            });
        }

    }
    class QuantityPanel extends JPanel {
        private final JLabel quantityLabel;
        private final JButton minusButton;
        private final JButton plusButton;
        private int quantity;
        private Cart.CartItem cartItem;

        public QuantityPanel(Cart.CartItem item) {
            this.cartItem=item;
            quantity = item.getQuantity();

            quantityLabel = new JLabel(String.valueOf(quantity));
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

            minusButton = new JButton("-");
            minusButton.addActionListener(e -> {
                if (quantity > 1) {
                    quantity--;
                    cartItem.setQuantity(quantity);
                    quantityLabel.setText(String.valueOf(quantity));
                    updateComponent();
                    this.revalidate();
                    this.repaint();
                }
            });

            plusButton = new JButton("+");
            plusButton.addActionListener(e -> {

                int productID = cartItem.getProduct().getProductId();
                int availableQuantity = productService.getProductQuantity(productID);
                if (quantity<availableQuantity){
                quantity++;
                cartItem.setQuantity(quantity);
                updateComponent();
                table.revalidate();
                table.repaint();
                }else{
                    JOptionPane.showMessageDialog(this,"Cannot add more items than available in stock");
                }
            });
            add(minusButton);
            add(quantityLabel);
            add(plusButton);

        }
        private void updateComponent() {
            quantityLabel.setText(String.valueOf(quantity));
            if (cart != null) {

                updateTotalLabel();
            }
        }
    }
    class Cart extends Component {
        private List<CartItem> items = new ArrayList<>();

        public void addProduct(Product product) {
            for (CartItem item : items) {
                if (item.getProduct().getProductId() == product.getProductId()) {
                    item.incrementQuantity();
                    return;
                }
            }
            items.add(new CartItem(product, 1));
        }

        public void removeProduct(int index) {
            if (index >= 0 && index < items.size()) {
                items.remove(index);
            }
        }

        public void updateQuantity(int productID, int newQuantity) {

            for(CartItem item: items){
                if (item.getProduct().getProductId() == productID){
                    if (newQuantity <=0){
                        removeProduct(productID);
                    }
                    else if(newQuantity <= item.getQuantity()) {
                        item.setQuantity(newQuantity);
                    }
                    else{
                        JOptionPane.showMessageDialog(this,"Quantity cant be greater than available stock of medicine");
                    }
                    break;
                }
            }
        }

        public double calculateTotal() {
            double total = 0.0;
            for (CartItem item : items) {
                double price = item.getProduct().getPrice();
                int quantity = item.getQuantity();
                total += (price * quantity);
            }
            return total;
        }

        public void clearCart(){
            if (cart!=null && cart.getItems()!=null){
                cart.getItems().clear();
                DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
                model.setRowCount(0);
                updateTotalLabel();
                revalidate();
                repaint();
            }
        }

        public List<CartItem> getItems(){
            return items;
        }
        public class CartItem {
            private Product product;
            private int quantity;

            public CartItem(Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
            }

            public Product getProduct() {
                return product;
            }

            public int getQuantity() {
                return quantity;
            }
            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public void incrementQuantity() {
                this.quantity++;
            }

            public void decrementQuantity() {
                if (this.quantity > 0) {
                    this.quantity--;
                }
            }
        }
    }


}

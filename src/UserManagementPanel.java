import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JList<User> userList;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private DefaultListModel<User> listModel;
    private UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(userList);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        tableModel = new DefaultTableModel(new Object[]{"Username", "Role"}, 0);
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load users and populate list
        //initializeTable();
        loadUsers();
        add(userTable,BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e->addUser());
        updateButton.addActionListener(e-> updateUser());
    }
    public class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof User) {
                User user = (User) value;
                setText(user.getUsername() + " - " + user.getRole());
            }
            return this;
        }
    }
    private void initializeTable() {

    }

    private void loadUsers() {
        // initializeTable();
        tableModel.setRowCount(0); // Clear the existing table data
        tableModel.addRow(new Object[]{"Username","Role"});
        //Component c = TableCellEditor(tableModel,)

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getUsername(), user.getRole()});
        }
    }


    private void deleteUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the user: " + selectedUser.getUsername() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userService.deleteUser(selectedUser.getUserID());
                if (success) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers(); // Reload the user list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void addUser() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Manager", "Sales Assistant"});

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // Convert to string for use in User object
            String role = (String) roleComboBox.getSelectedItem();

            boolean success =userService.addUser(new User(username, password, role));
            if (success) {
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void updateUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField usernameField = new JTextField(selectedUser.getUsername(), 20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Manager", "Sales Assistant"});
        roleComboBox.setSelectedItem(selectedUser.getRole());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password: (leave blank to not change)"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            User updatedUser = new User();
            updatedUser.setUserID(selectedUser.getUserID());
            updatedUser.setUsername(username);
            updatedUser.setPassword(password.isEmpty() ? selectedUser.getPassword() : password);
            updatedUser.setRole(role);


            boolean success = userService.updateUser(updatedUser);
            if (success) {
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}

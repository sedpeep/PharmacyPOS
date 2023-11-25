package GUI;

import ServiceLayer.UserService;
import DAOLayer.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private final JButton deleteButton;
    private DefaultListModel<User> listModel;
    private UserService userService;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        JList<User> userList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(userList);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        tableModel = new DefaultTableModel(new Object[]{"Username", "Role"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customizeTableHeader();

        JScrollPane userScroll = new JScrollPane(userTable);
        this.add(userScroll,BorderLayout.CENTER);

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
        deleteButton.addActionListener(e->deleteUser());
    }
    private void customizeTableHeader() {
        JTableHeader header = userTable.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 20));
    }

    private void loadUsers() {

        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Username","Role"});
        customizeTableHeader();
        //Component c = TableCellEditor(tableModel,)

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getUsername(), user.getRole()});
        }

    }


    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the user: " + username + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userService.deleteUser(username);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
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
            String password = new String(passwordField.getPassword());
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
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            User selectedUser = userService.getUserByUsername(username);

            if (selectedUser != null) {
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
                    String newUsername = usernameField.getText().trim();
                    String newPassword = new String(passwordField.getPassword()).trim();
                    String newRole = (String) roleComboBox.getSelectedItem();

                    if (newUsername.isEmpty()) {
                        newUsername = selectedUser.getUsername();
                    }
                    if (newPassword.isEmpty()) {
                        newPassword = selectedUser.getPassword();
                    }

                    User updatedUser = new User(newUsername, newPassword, newRole);
                    updatedUser.setUserID(selectedUser.getUserID()); // Set the user ID

                    boolean success = userService.updateUser(updatedUser);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "User could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

}

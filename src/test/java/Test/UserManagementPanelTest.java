package Test;

import DAOLayer.User;
import GUI.UserManagementPanel;
import ServiceLayer.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserManagementPanelTest {
    @Mock
    private UserService mockUserService;
    private UserManagementPanel userManagementPanel;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        userManagementPanel = new UserManagementPanel(mockUserService);
        List<User> initialUsers = Arrays.asList( new User("user1", "role1","Sales Assistant"),
                new User("user2", "role2","Manager"));
        when(mockUserService.getAllUsers()).thenReturn(initialUsers);
        userManagementPanel.loadUsers();
    }
    @Test
    void testInitializationAndUserLoading(){
            List<User> users = Arrays.asList(new User("user1", "role1", "Sales Assistant"), new User("user2", "role2", "Manager"));
            when(mockUserService.getAllUsers()).thenReturn(users);

            //userManagementPanel.loadUsers();

            verify(mockUserService, times(2)).getAllUsers();
            assertEquals(users.size()+1, userManagementPanel.getTableModel().getRowCount());
    }

    @Test
    void testAddUser(){
        String username = "newUser";
        String password = "pass123";
        String role = "Manager";

        List<User> updatedUsers = new ArrayList<>(Arrays.asList(
                new User("user1", "role1","Sales Assistant"),
                new User("user2", "role2","Manager"),
                new User(username, password,role)
        ));
        when(mockUserService.getAllUsers()).thenReturn(updatedUsers);

        when(mockUserService.addUser(any(User.class))).thenReturn(true);
        userManagementPanel.triggerAddUser(username, password, role);

        verify(mockUserService, times(1)).addUser(any(User.class));


        boolean userAdded = false;
        for (int i = 0; i < userManagementPanel.getTableModel().getRowCount(); i++) {
            if (userManagementPanel.getTableModel().getValueAt(i, 0).equals(username)) {
                userAdded = true;
                break;
            }
        }
        assertTrue(userAdded, "User should be added in the table model");

    }
    @Test
    void testUpdateUser() {
        String username = "newUser";
        String newPassword = "pass123";
        String newRole = "Manager";


        User oldUser = new User(username, "", "OldRole");


        User updatedUser = new User(username, newPassword, newRole);

        when(mockUserService.updateUser(any(User.class))).thenReturn(true);


        List<User> updatedUsers = Arrays.asList(
                new User("user1", "role1","Manager"),
                new User("user2", "role2","Sales Assistant"),
                updatedUser
        );


        when(mockUserService.getAllUsers()).thenReturn(updatedUsers);


        userManagementPanel.triggerUpdateUser(username, newPassword, newRole);

        verify(mockUserService, times(1)).updateUser(any(User.class));


        boolean userUpdated = false;
        for (int i = 0; i < userManagementPanel.getTableModel().getRowCount(); i++) {
            String tableUsername = (String) userManagementPanel.getTableModel().getValueAt(i, 0);
            String tableRole = (String) userManagementPanel.getTableModel().getValueAt(i, 1);
            if (tableUsername.equals(username) && tableRole.equals(newRole)) {
                userUpdated = true;
                break;
            }
        }

        assertTrue(userUpdated, "User should be updated in the table model");
    }

    @Test
    void testDeleteUser(){
        String username = "newUser";

        when(mockUserService.deleteUser(username)).thenReturn(true);

        userManagementPanel.triggerDeleteUser(username);

        verify(mockUserService, times(1)).deleteUser(username);

        boolean userFound = false;
        for(int i =0; i< userManagementPanel.getTableModel().getRowCount();i++){
            if(userManagementPanel.getTableModel().getValueAt(i,0).equals(username)){
                userFound = true;
                break;
            }
        }
        assertFalse(userFound,"User should be removed from the table");

    }



}

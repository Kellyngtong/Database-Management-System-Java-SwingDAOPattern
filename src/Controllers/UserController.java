package Controllers;

import DAO.UserDAO;
import Models.User;
import java.util.List;

public class UserController {
    private final UserDAO userDAO = new UserDAO();

    public void createUser(String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email);
        userDAO.createUser(user);
        System.out.println("Usuario creado: " + user);
    }

    public void deleteUser(int id) {
        userDAO.deleteUser(id);
        System.out.println("Usuario con ID " + id + " eliminado.");
    }

    public void updateUser(int id, String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email);
        user.setId(id);
        userDAO.updateUser(user);
        System.out.println("Usuario actualizado: " + user);
    }

    public List<User> listUsers() {
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios.");
        }
        return users;
    }


}
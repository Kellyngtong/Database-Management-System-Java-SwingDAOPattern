package DAO;

import Models.User;
import DB.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void createUser(User user) {
        String getLastIdQuery = "SELECT MAX(id) FROM usuarios";
        String insertUserQuery = "INSERT INTO usuarios (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection()) {
            int newId = 1;

            try (PreparedStatement stmt = conn.prepareStatement(getLastIdQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt(1) + 1;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertUserQuery)) {
                stmt.setInt(1, newId);
                stmt.setString(2, user.getFirstName());
                stmt.setString(3, user.getLastName());
                stmt.setString(4, user.getEmail());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email")
                );
                user.setId(rs.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user) {
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String deletePedidosQuery = "DELETE FROM pedidos WHERE usuario_id = ?";
        String deleteDireccionesQuery = "DELETE FROM direcciones WHERE usuario_id = ?";
        String deleteUsuarioQuery = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(deletePedidosQuery)) {
                stmt1.setInt(1, id);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(deleteDireccionesQuery)) {
                stmt2.setInt(1, id);
                stmt2.executeUpdate();
            }

            try (PreparedStatement stmt3 = conn.prepareStatement(deleteUsuarioQuery)) {
                stmt3.setInt(1, id);
                stmt3.executeUpdate();
            }
            System.out.println("Usuario y datos asociados eliminados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("email")
                    );
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
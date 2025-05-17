package DAO;

import Models.Order;
import DB.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public void createOrder(Order order) {
        String getLastIdQuery = "SELECT MAX(id) FROM pedidos";
        String insertOrderQuery = "INSERT INTO pedidos (id, usuario_id, producto_id, cantidad, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection()) {
            int newId = 1;

            try (PreparedStatement stmt = conn.prepareStatement(getLastIdQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt(1) + 1;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertOrderQuery)) {
                stmt.setInt(1, newId);
                stmt.setInt(2, order.getUserId());
                stmt.setInt(3, order.getProductId());
                stmt.setInt(4, order.getCuantity());
                stmt.setString(5, order.getDate());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM pedidos";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("usuario_id"),
                        rs.getInt("producto_id"),
                        rs.getInt("cantidad"),
                        rs.getString("fecha")
                );
                order.setId(rs.getInt("id"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrder(Order order) {
        String query = "UPDATE pedidos SET usuario_id = ?, producto_id = ?, cantidad = ?, fecha = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, order.getUserId());
            stmt.setInt(2, order.getProductId());
            stmt.setInt(3, order.getCuantity());
            stmt.setString(4, order.getDate());
            stmt.setInt(5, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int id) {
        String query = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getOrderById(int id) {
        String query = "SELECT * FROM pedidos WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order(
                            rs.getInt("usuario_id"),
                            rs.getInt("producto_id"),
                            rs.getInt("cantidad"),
                            rs.getString("fecha")
                    );
                    order.setId(rs.getInt("id"));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
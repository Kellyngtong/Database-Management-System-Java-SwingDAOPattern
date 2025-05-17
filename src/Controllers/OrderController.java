package Controllers;

import DAO.OrderDAO;
import Models.Order;
import java.util.List;

public class OrderController {
    private final OrderDAO orderDAO = new OrderDAO();

    public void createOrder(Order order) {
        orderDAO.createOrder(order);
        System.out.println("Pedido creado: " + order);
    }

    public void updateOrder(Order order) {
        if (order != null && order.getId() > 0) {
            orderDAO.updateOrder(order);
            System.out.println("Pedido actualizado: " + order);
        } else {
            System.out.println("Error: El pedido no es válido o no tiene un ID.");
        }
    }

    public void deleteOrder(int id) {
        orderDAO.deleteOrder(id);
        System.out.println("Pedido con ID " + id + " eliminado.");
    }

    public List<Order> listOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No hay pedidos.");
        }
        return orders;
    }
}
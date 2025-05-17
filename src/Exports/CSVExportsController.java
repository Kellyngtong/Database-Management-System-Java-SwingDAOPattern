package Exports;

import Controllers.OrderController;
import Controllers.UserController;
import Models.Order;
import Models.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExportsController {

    public void ExportUserOnCsv(String filePath) {
        UserController userController = new UserController();
        List<User> users = userController.listUsers();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Nombre,Apellido,Email\n");

            for (User user : users) {
                writer.append(user.getId() + ",")
                        .append(user.getFirstName() + ",")
                        .append(user.getLastName() + ",")
                        .append(user.getEmail() + "\n");
            }

            System.out.println("Usuarios exportados correctamente a: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al exportar usuarios: " + e.getMessage());
        }
    }

    public void ExportOrderOnCsv(String filePath) {
        OrderController orderController = new OrderController();
        List<Order> orders = orderController.listOrders();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,UsuarioID,ProductoID,Cantidad,Fecha\n");

            for (Order order : orders) {
                writer.append(order.getId() + ",")
                        .append(order.getUserId() + ",")
                        .append(order.getProductId() + ",")
                        .append(order.getCuantity() + ",")
                        .append(order.getDate() + "\n");
            }

            System.out.println("Pedidos exportados correctamente a: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al exportar pedidos: " + e.getMessage());
        }
    }
}

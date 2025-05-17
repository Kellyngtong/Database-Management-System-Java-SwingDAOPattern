package Exports;

import Controllers.OrderController;
import Controllers.UserController;
import Models.Order;
import Models.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLExportController {

    public void ExportUserOnXml(String filePath) {
        UserController userController = new UserController();
        List<User> users = userController.listUsers();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.append("<Users>\n");

            for (User user : users) {
                writer.append("  <User>\n")
                        .append("    <ID>").append(String.valueOf(user.getId())).append("</ID>\n")
                        .append("    <Nombre>").append(user.getFirstName()).append("</Nombre>\n")
                        .append("    <Apellido>").append(user.getLastName()).append("</Apellido>\n")
                        .append("    <Email>").append(user.getEmail()).append("</Email>\n")
                        .append("  </User>\n");
            }

            writer.append("</Users>\n");
            System.out.println("Usuarios exportados correctamente a: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al exportar usuarios: " + e.getMessage());
        }
    }

    public void ExportOrderOnXml(String filePath) {
        OrderController orderController = new OrderController();
        List<Order> orders = orderController.listOrders();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.append("<Orders>\n");

            for (Order order : orders) {
                writer.append("  <Order>\n")
                        .append("    <ID>").append(String.valueOf(order.getId())).append("</ID>\n")
                        .append("    <UsuarioID>").append(String.valueOf(order.getUserId())).append("</UsuarioID>\n")
                        .append("    <ProductoID>").append(String.valueOf(order.getProductId())).append("</ProductoID>\n")
                        .append("    <Cantidad>").append(String.valueOf(order.getCuantity())).append("</Cantidad>\n")
                        .append("    <Fecha>").append(order.getDate()).append("</Fecha>\n")
                        .append("  </Order>\n");
            }

            writer.append("</Orders>\n");
            System.out.println("Pedidos exportados correctamente a: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al exportar pedidos: " + e.getMessage());
        }
    }
}
package Controllers;

import DAO.OrderDAO;
import DAO.UserDAO;
import Models.Order;
import Models.User;

import javax.swing.*;
import javax.swing.table.TableModel;

public class SearchController {
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    public void searchById(int id, String type, JTable ordersTable, JTable usersTable, JTabbedPane tabbedPane) {
        System.out.println("Buscando " + type + " con ID: " + id);
        switch (type.toLowerCase()) {
            case "pedido" -> {
                Order order = orderDAO.getOrderById(id);
                if (order != null) {
                    System.out.println("Pedido encontrado: " + order);
                    tabbedPane.setSelectedIndex(0); // Cambiar a la pestaña "Pedidos"
                    selectRowInTable(ordersTable, id);
                } else {
                    System.out.println("Pedido con ID " + id + " no encontrado.");
                    JOptionPane.showMessageDialog(null, "Pedido con ID " + id + " no encontrado.");
                }
            }
            case "usuario" -> {
                User user = userDAO.getUserById(id);
                if (user != null) {
                    System.out.println("Usuario encontrado: " + user);
                    tabbedPane.setSelectedIndex(1); // Cambiar a la pestaña "Usuarios"
                    selectRowInTable(usersTable, id);
                } else {
                    System.out.println("Usuario con ID " + id + " no encontrado.");
                    JOptionPane.showMessageDialog(null, "Usuario con ID " + id + " no encontrado.");
                }
            }
            default -> System.out.println("Tipo no válido. Usa 'pedido' o 'usuario'.");
        }
    }

    private void selectRowInTable(JTable table, int id) {
        TableModel model = table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            if ((int) model.getValueAt(row, 0) == id) { // Asume que la columna 0 contiene el ID
                table.setRowSelectionInterval(row, row);
                table.scrollRectToVisible(table.getCellRect(row, 0, true));
                break;
            }
        }
    }
}
package Views;

import Controllers.OrderController;
import Controllers.SearchController;
import Controllers.UserController;
import Exports.CSVExportsController;
import Exports.XMLExportController;
import Models.Order;
import Models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainUI extends JFrame {
    private final OrderController orderController = new OrderController();
    private final UserController userController = new UserController();
    private final JTable ordersTable;
    private final JTable usersTable;

    private final JPanel mainPanel;

    public MainUI() {
        setTitle("Gestión de Base de Datos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersTable = new JTable();
        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        ordersPanel.add(ordersScrollPane, BorderLayout.CENTER);

        JPanel ordersButtonPanel = new JPanel();
        JButton createOrderButton = new JButton("Crear Pedido");
        JButton deleteOrderButton = new JButton("Borrar Pedido");
        JButton updateOrderButton = new JButton("Modificar Pedido");
        JButton exportOrderButton = new JButton("Exportar Pedidos a CSV");
        JButton exportOrderXmlButton = new JButton("Exportar Pedidos a XML");
        ordersButtonPanel.add(createOrderButton);
        ordersButtonPanel.add(deleteOrderButton);
        ordersButtonPanel.add(updateOrderButton);
        ordersButtonPanel.add(exportOrderButton);
        ordersButtonPanel.add(exportOrderXmlButton);

        ImageIcon icon = new ImageIcon("src/assets/csv.png");
        Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        exportOrderButton.setIcon(new ImageIcon(scaledImage));

        exportOrderButton.setHorizontalTextPosition(SwingConstants.LEFT);
        exportOrderButton.setIconTextGap(10);

        ordersPanel.add(ordersButtonPanel, BorderLayout.SOUTH);

        JPanel usersPanel = new JPanel(new BorderLayout());
        usersTable = new JTable();
        JScrollPane usersScrollPane = new JScrollPane(usersTable);
        usersPanel.add(usersScrollPane, BorderLayout.CENTER);

        JPanel usersButtonPanel = new JPanel();
        JButton createUserButton = new JButton("Crear Usuario");
        JButton deleteUserButton = new JButton("Borrar Usuario");
        JButton updateUserButton = new JButton("Modificar Usuario");
        JButton exportUserButton = new JButton("Exportar Usuarios a CSV");
        JButton exportUserXmlButton = new JButton("Exportar Usuarios a XML");
        usersButtonPanel.add(createUserButton);
        usersButtonPanel.add(deleteUserButton);
        usersButtonPanel.add(updateUserButton);
        usersButtonPanel.add(exportUserButton);
        usersButtonPanel.add(exportUserXmlButton);
        usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Pedidos", ordersPanel);
        tabbedPane.addTab("Usuarios", usersPanel);



        JTextField searchField = new JTextField(10);
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"Usuario", "Pedido"});
        JButton searchButton = new JButton("Buscar");

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("ID:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Tipo:"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String idText = searchField.getText();
            String type = (String) searchTypeCombo.getSelectedItem();

            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID.");
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                SearchController searchController = new SearchController();
                assert type != null;
                searchController.searchById(id, type.toLowerCase(), ordersTable, usersTable, tabbedPane);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.");
            }
        });

        // Inicialize UI Data
        loadOrders();
        loadUsers();

        createOrderButton.addActionListener(e -> {
            JTextField userIdField = new JTextField();
            JTextField productIdField = new JTextField();
            JTextField quantityField = new JTextField();
            JTextField dateField = new JTextField();

            Object[] message = {
                    "Usuario ID:", userIdField,
                    "Producto ID:", productIdField,
                    "Cantidad:", quantityField,
                    "Fecha (YYYY-MM-DD):", dateField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Crear Pedido", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int userId = Integer.parseInt(userIdField.getText());
                    int productId = Integer.parseInt(productIdField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    String date = dateField.getText();

                    if (!date.isEmpty()) {
                        Order order = new Order(userId, productId, quantity, date);
                        orderController.createOrder(order);
                        loadOrders();
                    } else {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Usuario ID, Producto ID y Cantidad deben ser números válidos.");
                }
            }
        });

        deleteOrderButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                int orderId = (int) ordersTable.getValueAt(selectedRow, 0);
                orderController.deleteOrder(orderId);
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido para borrar.");
            }
        });

        updateOrderButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                int orderId = (int) ordersTable.getValueAt(selectedRow, 0);
                int oldUserId = Integer.parseInt(ordersTable.getValueAt(selectedRow, 1).toString());
                int oldProductId = (int) ordersTable.getValueAt(selectedRow, 2);
                int oldQuantity = (int) ordersTable.getValueAt(selectedRow, 3);
                String oldDate = (String) ordersTable.getValueAt(selectedRow, 4);

                JTextField userIdField = new JTextField(String.valueOf(oldUserId));
                JTextField productIdField = new JTextField(String.valueOf(oldProductId));
                JTextField quantityField = new JTextField(String.valueOf(oldQuantity));
                JTextField dateField = new JTextField(oldDate);

                Object[] message = {
                        "Usuario ID:", userIdField,
                        "Producto ID:", productIdField,
                        "Cantidad:", quantityField,
                        "Fecha (YYYY-MM-DD):", dateField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modificar Pedido", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int newUserId = Integer.parseInt(userIdField.getText());
                        int newProductId = Integer.parseInt(productIdField.getText());
                        int newQuantity = Integer.parseInt(quantityField.getText());
                        String newDate = dateField.getText();

                        String confirmationMessage = String.format(
                                """
                                        Se va a modificar el pedido:
                                        
                                        De:
                                        Usuario ID: %d
                                        Producto ID: %d
                                        Cantidad: %d
                                        Fecha: %s
                                        
                                        A:
                                        Usuario ID: %d
                                        Producto ID: %d
                                        Cantidad: %d
                                        Fecha: %s""",
                                oldUserId, oldProductId, oldQuantity, oldDate,
                                newUserId, newProductId, newQuantity, newDate
                        );

                        int confirmOption = JOptionPane.showConfirmDialog(
                                this, confirmationMessage, "Confirmar Modificación", JOptionPane.OK_CANCEL_OPTION
                        );

                        if (confirmOption == JOptionPane.OK_OPTION) {
                            Order updatedOrder = new Order(newUserId, newProductId, newQuantity, newDate);
                            updatedOrder.setId(orderId);
                            orderController.updateOrder(updatedOrder);
                            loadOrders();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Usuario ID, Producto ID y Cantidad deben ser números válidos.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido para modificar.");
            }
        });

        exportOrderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar ubicación para guardar el archivo");
            fileChooser.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".csv")) {
                    filePath += ".csv";
                }

                CSVExportsController CSVExportsController = new CSVExportsController();
                CSVExportsController.ExportOrderOnCsv(filePath);
                JOptionPane.showMessageDialog(this, "Pedidos exportados correctamente a: " + filePath);
            }
        });

        exportOrderXmlButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar ubicación para guardar el archivo");
            fileChooser.setFileFilter(new FileNameExtensionFilter(".xml", "xml"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xml")) {
                    filePath += ".xml";
                }

                XMLExportController xmlExportController = new XMLExportController();
                xmlExportController.ExportOrderOnXml(filePath);
                JOptionPane.showMessageDialog(this, "Pedidos exportados correctamente a: " + filePath);
            }
        });



        createUserButton.addActionListener(e -> {
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField emailField = new JTextField();

            Object[] message = {
                    "Nombre:", firstNameField,
                    "Apellido:", lastNameField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Crear Usuario", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
                    userController.createUser(firstName, lastName, email);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                }
            }
        });

        deleteUserButton.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) usersTable.getValueAt(selectedRow, 0);
                userController.deleteUser(userId);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para borrar.");
            }
        });

        updateUserButton.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) usersTable.getValueAt(selectedRow, 0);
                String firstName = (String) usersTable.getValueAt(selectedRow, 1);
                String lastName = (String) usersTable.getValueAt(selectedRow, 2);
                String email = (String) usersTable.getValueAt(selectedRow, 3);

                JTextField firstNameField = new JTextField(firstName);
                JTextField lastNameField = new JTextField(lastName);
                JTextField emailField = new JTextField(email);

                Object[] message = {
                        "Nombre:", firstNameField,
                        "Apellido:", lastNameField,
                        "Email:", emailField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modificar Usuario", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String newFirstName = firstNameField.getText();
                    String newLastName = lastNameField.getText();
                    String newEmail = emailField.getText();

                    userController.updateUser(userId, newFirstName, newLastName, newEmail);
                    loadUsers();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.");
            }
        });

        exportUserButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar ubicación para guardar el archivo");
            fileChooser.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".csv")) {
                    filePath += ".csv";
                }

                CSVExportsController CSVExportsController = new CSVExportsController();
                CSVExportsController.ExportUserOnCsv(filePath);
                JOptionPane.showMessageDialog(this, "Usuarios exportados correctamente a: " + filePath);
            }
        });

        exportUserXmlButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar ubicación para guardar el archivo");
            fileChooser.setFileFilter(new FileNameExtensionFilter(".xml", "xml"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xml")) {
                    filePath += ".xml";
                }

                XMLExportController xmlExportController = new XMLExportController();
                xmlExportController.ExportUserOnXml(filePath);
                JOptionPane.showMessageDialog(this, "Usuarios exportados correctamente a: " + filePath);
            }
        });
    }

    private void loadOrders() {
        List<Order> orders = orderController.listOrders();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Usuario ID", "Producto ID", "Cantidad", "Fecha"}, 0);
        for (Order order : orders) {
            model.addRow(new Object[]{order.getId(), order.getUserId(), order.getProductId(), order.getCuantity(), order.getDate()});
        }
        ordersTable.setModel(model);
    }

    private void loadUsers() {
        List<User> users = userController.listUsers();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Email"}, 0);
        for (User user : users) {
            model.addRow(new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()});
        }
        usersTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }
}
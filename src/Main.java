import DB.Database;
import Views.MainUI;
import Views.UIStyles;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.getConnection();

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
            }
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIStyles.applyButtonStyles();
        } catch (Exception e) {
            System.err.println("No se pudo aplicar Windows: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }
}
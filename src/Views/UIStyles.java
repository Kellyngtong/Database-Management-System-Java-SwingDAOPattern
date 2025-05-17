package Views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UIStyles {

    public static void applyButtonStyles() {
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put(("Button.font"), new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.border", new CompoundBorder(
                new LineBorder(Color.RED),
                new EmptyBorder(5, 15, 5, 15)
        ));
    }
}
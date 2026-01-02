
import com.formdev.flatlaf.FlatLightLaf;
import GUI.LoginGUI;
import javax.swing.*;
import java.awt.*;

public class app {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                UIManager.put("Button.arc", 15);
                UIManager.put("TextComponent.arc", 15);
                UIManager.put("Component.focusWidth", 1);

                new LoginGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

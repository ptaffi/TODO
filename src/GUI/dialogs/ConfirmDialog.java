package GUI.dialogs;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog {
    public static boolean ask(Component parent, String message, String title) {
        int ok = JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return ok == JOptionPane.YES_OPTION;
    }
}

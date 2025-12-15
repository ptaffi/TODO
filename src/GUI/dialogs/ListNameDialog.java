package GUI.dialogs;

import javax.swing.*;
import java.awt.*;

public class ListNameDialog {

    public static String show(Component parent, String title, String label, String initValue) {
        JTextField tf = new JTextField(initValue == null ? "" : initValue);
        tf.setPreferredSize(new Dimension(320, 40));

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);

        int ok = JOptionPane.showConfirmDialog(
                parent,
                p,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (ok != JOptionPane.OK_OPTION) return null;
        return tf.getText();
    }
}

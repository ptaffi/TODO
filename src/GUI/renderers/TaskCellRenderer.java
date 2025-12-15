// package GUI.renderers;

// import model.Task;

// import javax.swing.*;
// import java.awt.*;
// import java.text.SimpleDateFormat;

// public class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {

//     private JLabel lblCheck;
//     private JLabel lblTitle;
//     private JLabel lblMeta;

//     private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//     public TaskCellRenderer() {
//         initUI();
//     }

//     private void initUI() {
//         setLayout(new BorderLayout(12, 0));
//         setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
//         setOpaque(true);

//         lblCheck = new JLabel("○");
//         lblCheck.setFont(new Font("Segoe UI", Font.BOLD, 20));

//         lblTitle = new JLabel();
//         lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));

//         lblMeta = new JLabel();
//         lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//         lblMeta.setForeground(new Color(180, 180, 180));

//         JPanel center = new JPanel(new BorderLayout(0, 2));
//         center.setOpaque(false);
//         center.add(lblTitle, BorderLayout.NORTH);
//         center.add(lblMeta, BorderLayout.SOUTH);

//         add(lblCheck, BorderLayout.WEST);
//         add(center, BorderLayout.CENTER);
//     }

//     @Override
//     public Component getListCellRendererComponent(JList<? extends Task> list, Task t, int index,
//                                                   boolean isSelected, boolean cellHasFocus) {

//         boolean done = t.isCompleted();
//         lblCheck.setText(done ? "✔" : "○");

//         String title = t.getTitle();
//         if (done) {
//             lblTitle.setText("<html><span style='text-decoration:line-through;opacity:0.7'>" + escape(title) + "</span></html>");
//         } else {
//             lblTitle.setText(escape(title));
//         }

//         String note = (t.getNote() == null) ? "" : t.getNote().trim();
//         String due = (t.getDueDate() == null) ? "" : sdf.format(t.getDueDate());
//         String meta = note.isEmpty() ? due : (due.isEmpty() ? note : (note + " • " + due));
//         lblMeta.setText(meta);

//         setBackground(isSelected ? new Color(0, 120, 215, 35) : new Color(0, 0, 0, 0));
//         return this;
//     }

//     private String escape(String s) {
//         if (s == null) return "";
//         return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
//     }
// }

package GUI.renderers;

import model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {

    private final JLabel icon = new JLabel("○");
    private final JLabel title = new JLabel();
    private final JLabel sub = new JLabel();

    public TaskCellRenderer() {
        setOpaque(true);
        setLayout(new BorderLayout(12, 0));
        setBorder(new EmptyBorder(12, 16, 12, 16));

        icon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        icon.setForeground(new Color(210, 210, 210));

        title.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        title.setForeground(new Color(240, 240, 240));

        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(170, 170, 170));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(Box.createVerticalStrut(4));
        text.add(sub);

        add(icon, BorderLayout.WEST);
        add(text, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task t, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        boolean done = t.isCompleted();
        title.setForeground(done ? new Color(120,120,120) : new Color(25,25,25));
        sub.setForeground(done ? new Color(150,150,150) : new Color(90,90,90));
        icon.setForeground(new Color(120,120,120));

        icon.setText(done ? "✔" : "○");

        String ttl = t.getTitle();
        if (done) {
            title.setText("<html><span style='text-decoration: line-through; color:#bdbdbd;'>" + escape(ttl) + "</span></html>");
        } else {
            title.setText(escape(ttl));
        }

        String note = t.getNote() == null ? "" : t.getNote().trim();
        String due = (t.getDueDate() == null) ? "" : (" • " + t.getDueDate().toString());
        sub.setText((note.isEmpty() ? " " : note) + due);

        Color bg = isSelected ? new Color(255, 255, 255, 18) : list.getBackground();
        setBackground(bg);

        return this;
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

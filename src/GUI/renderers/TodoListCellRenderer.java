// package GUI.renderers;

// import model.TodoList;

// import javax.swing.*;
// import java.awt.*;
// import java.util.Map;
// import java.util.function.Supplier;

// public class TodoListCellRenderer extends JPanel implements ListCellRenderer<TodoList> {

//     private final Supplier<Map<Integer, Integer>> badgeSupplier;

//     private JLabel lblIcon;
//     private JLabel lblName;
//     private JLabel lblBadge;

//     public TodoListCellRenderer(Supplier<Map<Integer, Integer>> badgeSupplier) {
//         this.badgeSupplier = badgeSupplier;
//         initUI();
//     }

//     private void initUI() {
//         setLayout(new BorderLayout(10, 0));
//         setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
//         setOpaque(true);

//         lblIcon = new JLabel("✔");
//         lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 16));

//         lblName = new JLabel();
//         lblName.setFont(new Font("Segoe UI", Font.PLAIN, 15));

//         lblBadge = new JLabel("", SwingConstants.CENTER);
//         lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
//         lblBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
//         lblBadge.setOpaque(true);

//         JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
//         left.setOpaque(false);
//         left.add(lblIcon);
//         left.add(lblName);

//         add(left, BorderLayout.WEST);
//         add(lblBadge, BorderLayout.EAST);
//     }

//     @Override
//     public Component getListCellRendererComponent(JList<? extends TodoList> list, TodoList value, int index,
//                                                   boolean isSelected, boolean cellHasFocus) {
//         lblName.setText(value.getName());

//         int count = 0;
//         Map<Integer, Integer> m = badgeSupplier.get();
//         if (m != null) count = m.getOrDefault(value.getId(), 0);
//         lblBadge.setText(String.valueOf(count));

//         Color bg = isSelected ? new Color(0, 120, 215, 50) : new Color(0, 0, 0, 0);
//         setBackground(bg);

//         lblBadge.setBackground(isSelected ? new Color(0, 120, 215, 140) : new Color(0, 0, 0, 40));
//         lblBadge.setForeground(Color.WHITE);

//         return this;
//     }
// }

package GUI.renderers;

import GUI.MainFrame;
import model.TodoList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class TodoListCellRenderer extends DefaultListCellRenderer {

    private final Map<Integer, Integer> counts;

    public TodoListCellRenderer(Map<Integer, Integer> counts) {
        this.counts = counts;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel lb = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        TodoList tl = (TodoList) value;

        int c = counts == null ? 0 : counts.getOrDefault(tl.getId(), 0);

        lb.setText(tl.getName() + "   (" + c + ")");
        lb.setBorder(new EmptyBorder(15, 12, 0, 12));
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        lb.setForeground(MainFrame.FG_TEXT);
        lb.setBackground(isSelected ? new Color(255, 255, 255, 18) : MainFrame.BG_PANEL);

       setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(188, 143, 187)),
        BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        return lb;
    }
}

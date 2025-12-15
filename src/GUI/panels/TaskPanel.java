package GUI.panels;

import GUI.MainFrame;
import GUI.renderers.TaskCellRenderer;
import model.Task;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TaskPanel extends JPanel {

    public interface Listener {
        void onAddTask();
        void onEditTask(Task t);
        void onDeleteTask(Task t);
        void onToggleComplete(Task t);
    }

    private final Listener listener;

    private final DefaultListModel<Task> taskModel = new DefaultListModel<>();
    private final JList<Task> taskJList = new JList<>(taskModel);

    private JButton btnAddTask;

    public TaskPanel(Listener listener) {
        this.listener = listener;
        initUI();
        initEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(MainFrame.BG_APP);

        taskJList.setCellRenderer(new TaskCellRenderer());
        taskJList.setBackground(MainFrame.BG_APP);
        taskJList.setSelectionBackground(new Color(255, 255, 255, 30));
        taskJList.setSelectionForeground(MainFrame.FG_MUTED);
        taskJList.setFixedCellHeight(72);

        JScrollPane sp = new JScrollPane(taskJList);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(MainFrame.BG_APP);

        add(sp, BorderLayout.CENTER);

        btnAddTask = new JButton("+ Add a to-do");
        pill(btnAddTask);
        btnAddTask.setPreferredSize(new Dimension(0, 56));
        btnAddTask.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(MainFrame.BG_APP);
        bottom.setBorder(new EmptyBorder(12, 22, 18, 22));
        bottom.add(btnAddTask, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnAddTask.addActionListener(e -> listener.onAddTask());

        taskJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = taskJList.locationToIndex(e.getPoint());
                if (idx < 0) return;

                Task t = taskModel.get(idx);
                Rectangle cell = taskJList.getCellBounds(idx, idx);

                int clickX = e.getX() - cell.x;
                if (clickX <= 42 && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    listener.onToggleComplete(t);
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    listener.onEditTask(t);
                }
            }

            @Override public void mousePressed(MouseEvent e) { rightclMenu(e); }
            @Override public void mouseReleased(MouseEvent e) { rightclMenu(e); }

            private void rightclMenu(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                int idx = taskJList.locationToIndex(e.getPoint());
                if (idx < 0) return;

                taskJList.setSelectedIndex(idx);
                Task t = taskModel.get(idx);

                JPopupMenu pm = new JPopupMenu();
                JMenuItem miEdit = new JMenuItem("Edit");
                JMenuItem miDelete = new JMenuItem("Delete");
                pm.add(miEdit);
                pm.addSeparator();
                pm.add(miDelete);

                miEdit.addActionListener(a -> listener.onEditTask(t));
                miDelete.addActionListener(a -> listener.onDeleteTask(t));

                pm.show(taskJList, e.getX(), e.getY());
            }
        });
    }

    public void setTasks(List<Task> tasks) {
        taskModel.clear();
        for (Task t : tasks) taskModel.addElement(t);
    }

    public void clearTasks() {
        taskModel.clear();
    }

    private void pill(JButton b) {
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}

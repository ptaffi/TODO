package GUI;
import DAO.TaskDAO;
import DAO.TodoListDAO;
import model.Task;
import model.TodoList;
// import util.ThemeManager;
import GUI.dialogs.ConfirmDialog;
import GUI.dialogs.ListNameDialog;
import GUI.dialogs.TaskDialog;
import GUI.dialogs.ShareDialog;
import GUI.panels.HeaderPanel;
import GUI.panels.SidebarPanel;
import GUI.panels.TaskPanel;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame
        implements SidebarPanel.Listener, HeaderPanel.Listener, TaskPanel.Listener {

    private final int userId;
    private final String userName;
    private final String email;

    private SidebarPanel sidebar;
    private HeaderPanel header;
    private TaskPanel taskPanel;

    private TodoList selectedList;

    public static final Color BG_APP   = new Color(250, 245, 250);
    public static final Color BG_PANEL = new Color(245, 235, 245);
    public static final Color BG_HEADER = new Color(188, 143, 187);
    public static final Color FG_TEXT  = new Color(188, 143, 187);
    public static final Color FG_MUTED = new Color(0, 0, 0);

    public MainFrame(int userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;

        UIManager.put("Button.arc", 18);
        UIManager.put("Component.arc", 18);
        UIManager.put("TextComponent.arc", 18);
        UIManager.put("Component.focusWidth", 1);

        setTitle("FINI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_APP);

        sidebar = new SidebarPanel(this);
        sidebar.setUserInfo(userName, email);

        header = new HeaderPanel(this);
        taskPanel = new TaskPanel(this);

        add(sidebar, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG_APP);
        right.add(header, BorderLayout.NORTH);
        right.add(taskPanel, BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);

        header.setDateText(new SimpleDateFormat("EEEE, MMMM d").format(new java.util.Date()));

        loadListsnPickFirst();
    }

    // load
    private void loadListsnPickFirst() {
        loadLists();
        if (sidebar.getListCount() > 0) {
            sidebar.selectIndex(0);
        } else {
            selectedList = null;
            header.setTitleText("...");
            taskPanel.clearTasks();
        }
    }

    private void loadLists() {
        try {
            List<TodoList> lists = TodoListDAO.getByUserId(userId);

            Map<Integer, Integer> counts = new HashMap<>();
            for (TodoList l : lists) {
                int c = TaskDAO.countByListId(l.getId());
                counts.put(l.getId(), c);
            }

            sidebar.setLists(lists, counts);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load lists failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadTasks(int listId) {
        try {
            List<Task> tasks = TaskDAO.getByListId(listId);
            taskPanel.setTasks(tasks);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load tasks failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void refreshCountsnKeepCurrent() {
        int keepId = (selectedList == null) ? -1 : selectedList.getId();
        loadLists();
        if (keepId != -1) sidebar.selectListById(keepId);
    }
    // SidebarPanel
    @Override
    public void onListSelected(TodoList list) {
        if (list == null) return;

        selectedList = list;
        header.setTitleText(list.getName());
        loadTasks(list.getId());
    }
        @Override
    public void onOpenProfile() {
        String info = "Username: " + (userName == null ? "" : userName) + "\nEmail: " + (email == null ? "" : email);

        JOptionPane.showMessageDialog(this, info, "Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onNewList() {
        String name = ListNameDialog.show(this, "New list", "List name:", "");
        if (name == null) return;

        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên list không được để trống");
            return;
        }

        try {
            int newId = TodoListDAO.create(name, userId);
            loadLists();
            sidebar.selectListById(newId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Create list failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    @Override
    public void onChangePassword() {
    new GUI.ChangePassword(this, userId).setVisible(true);
    }

    @Override
    public void onLogout() {
        boolean ok = ConfirmDialog.ask(this, "Đăng xuất?", "Logout");
        if (!ok) return;

        dispose();
        EventQueue.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    @Override
    public void onShareList() {
        if (selectedList == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn list.");
            return;
        }

        ShareDialog dlg = new ShareDialog(this, selectedList.getId(), userId);
        dlg.setVisible(true);

        // accept share->list mới -> reload
        loadLists();
        if (selectedList != null) sidebar.selectListById(selectedList.getId());
    }

    @Override
    public void onRenameList() {
        if (selectedList == null) return;

        String newName = ListNameDialog.show(this, "Rename list", "New name:", selectedList.getName());
        if (newName == null) return;

        newName = newName.trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên list");
            return;
        }

        try {
            TodoListDAO.rename(selectedList.getId(), newName);
            loadLists();
            sidebar.selectListById(selectedList.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Rename failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDeleteList() {
        if (selectedList == null) return;

        boolean ok = ConfirmDialog.ask(this, "Xoá list \"" + selectedList.getName() + "\"?\n(Tất cả task trong list cũng sẽ bị xoá)", "Delete list");
        if (!ok) return;

        try {
            TaskDAO.deleteByListId(selectedList.getId());
            TodoListDAO.delete(selectedList.getId());

            selectedList = null;
            loadListsnPickFirst();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete list failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // HeaderPanel.Listener 
    @Override public void onHeaderShare() { onShareList(); }
    @Override public void onHeaderRename() { onRenameList(); }
    @Override public void onHeaderDelete() { onDeleteList(); }

    // TaskPanel.Listener
    @Override
    public void onAddTask() {
        if (selectedList == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lisy.");
            return;
        }

        TaskDialog dlg = new TaskDialog(this, "Add task", null);
        dlg.setVisible(true);

        if (!dlg.ok) return;

        try {
            TaskDAO.create(dlg.title, dlg.note, false, dlg.dueDate, selectedList.getId());
            loadTasks(selectedList.getId());
            refreshCountsnKeepCurrent();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Add task failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onEditTask(Task t) {
        if (t == null) return;

        TaskDialog dlg = new TaskDialog(this, "Edit task", t);
        dlg.setVisible(true);

        if (!dlg.ok) return;

        try {
            TaskDAO.update(t.getId(), dlg.title, dlg.note, dlg.completed, dlg.dueDate);
            if (selectedList != null) loadTasks(selectedList.getId());
            refreshCountsnKeepCurrent();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Edit task failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDeleteTask(Task t) {
        if (t == null) return;

        boolean ok = ConfirmDialog.ask(this, "Xoá task \"" + t.getTitle() + "\"?", "Delete task");
        if (!ok) return;

        try {
            TaskDAO.delete(t.getId());
            if (selectedList != null) loadTasks(selectedList.getId());
            refreshCountsnKeepCurrent();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete task failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onToggleComplete(Task t) {
        if (t == null) return;

        try {
            boolean newVal = !t.isCompleted();
            TaskDAO.setCompleted(t.getId(), newVal);
            if (selectedList != null) loadTasks(selectedList.getId());
            refreshCountsnKeepCurrent();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    // test
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> EventQueue.invokeLater(() -> new MainFrame(1, "User", "").setVisible(true)));
    }
}

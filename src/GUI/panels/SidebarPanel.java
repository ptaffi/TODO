package GUI.panels;

import GUI.MainFrame;
import GUI.renderers.TodoListCellRenderer;
import model.TodoList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
// import util.ThemeManager;

public class SidebarPanel extends JPanel {

    public interface Listener {
        void onListSelected(TodoList list);
        void onNewList();
        void onShareList();
        void onRenameList();
        void onDeleteList();
        void onLogout();

        default void onOpenProfile() {}
    }

    private final Listener listener;

    private final DefaultListModel<TodoList> listModel = new DefaultListModel<>();
    private final JList<TodoList> listJList = new JList<>(listModel);

    private JButton btnNewList;

    // profile UI
    private String displayName = "User";
    private String email = "";
    private JLabel avatar;
    private JLabel nameLb;
    private JLabel mailLb;

    public SidebarPanel(Listener listener) {
        this(listener, null, null);
    }

    public SidebarPanel(Listener listener, String displayName, String email) {
        this.listener = listener;
        setUserInfo(displayName, email);
        initUI();
        initEvents();
    }

    public void setUserInfo(String displayName, String email) {
        this.displayName = (displayName != null && !displayName.trim().isEmpty()) ? displayName.trim() : "User";
        this.email = (email != null) ? email.trim() : "";

        if (nameLb != null) nameLb.setText(this.displayName);
        if (mailLb != null) mailLb.setText(this.email);
        if (avatar != null) avatar.setText(getInitials(this.displayName));
        revalidate();
        repaint();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));
        setBackground(MainFrame.BG_PANEL);
        setBorder(new EmptyBorder(18, 18, 16, 16));

        JLabel title = new JLabel("TODO LIST");
        title.setFont(new Font("Segoe UI", Font.BOLD, 45));
        title.setForeground(MainFrame.FG_TEXT);
        title.setBorder(new EmptyBorder(50, 23, 50, 6));
        add(title, BorderLayout.NORTH);

        listJList.setFixedCellHeight(60);
        listJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listJList.setBackground(MainFrame.BG_PANEL);
        listJList.setBorder(new EmptyBorder(8, 6, 8, 6));

        JScrollPane sp = new JScrollPane(listJList);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(MainFrame.BG_PANEL);
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnNewList = new JButton("+ New list");
        pill(btnNewList);

        btnNewList.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnNewList.setPreferredSize(new Dimension(0, 46));

        bottom.add(btnNewList);
        bottom.add(Box.createVerticalStrut(10));

        JComponent profileBlock = createProfileBlock();
        profileBlock.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileBlock.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        bottom.add(profileBlock);

        add(bottom, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnNewList.addActionListener(e -> listener.onNewList());

        listJList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            listener.onListSelected(listJList.getSelectedValue());
        });

        // right-click menu
        JPopupMenu menu = new JPopupMenu();
        JMenuItem miShare = new JMenuItem("Share list");
        JMenuItem miRename = new JMenuItem("Rename list");
        JMenuItem miDelete = new JMenuItem("Delete list");

        menu.add(miShare);
        menu.add(miRename);
        menu.addSeparator();
        menu.add(miDelete);

        miShare.addActionListener(e -> listener.onShareList());
        miRename.addActionListener(e -> listener.onRenameList());
        miDelete.addActionListener(e -> listener.onDeleteList());

        listJList.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { showIfPopup(e); }
            @Override public void mouseReleased(MouseEvent e) { showIfPopup(e); }
            private void showIfPopup(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                int idx = listJList.locationToIndex(e.getPoint());
                if (idx >= 0) listJList.setSelectedIndex(idx);
                menu.show(listJList, e.getX(), e.getY());
            }
        });
    }

    private JComponent createProfileBlock() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 8, 6, 8));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        avatar = new JLabel(getInitials(displayName), SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(42, 42));
        avatar.setForeground(MainFrame.FG_TEXT);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(255, 255, 255, 18));
        avatar.putClientProperty("JComponent.arc", 999);

        nameLb = new JLabel(displayName);
        nameLb.setForeground(MainFrame.FG_TEXT);
        nameLb.setFont(new Font("Segoe UI", Font.BOLD, 14));

        mailLb = new JLabel(email == null ? "" : email);
        mailLb.setForeground(MainFrame.FG_MUTED);
        mailLb.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(nameLb);
        text.add(Box.createVerticalStrut(2));
        text.add(mailLb);

        JLabel chevron = new JLabel("▾");
        chevron.setForeground(MainFrame.FG_MUTED);

        p.add(avatar, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        p.add(chevron, BorderLayout.EAST);

        // menu profile
        JPopupMenu menu = new JPopupMenu();
        JMenuItem miProfile = new JMenuItem("Profile");
        JMenuItem miLogout = new JMenuItem("Logout");
        menu.add(miProfile);
        menu.addSeparator();
        menu.add(miLogout);

        miProfile.addActionListener(e -> listener.onOpenProfile());
        miLogout.addActionListener(e -> listener.onLogout());

        MouseAdapter openMenu = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { showMenu(e); }
            @Override public void mouseReleased(MouseEvent e) { showMenu(e); }
            private void showMenu(MouseEvent e) {
                menu.show(p, 0, p.getHeight());
            }
        };

        p.addMouseListener(openMenu);
        avatar.addMouseListener(openMenu);
        text.addMouseListener(openMenu);

        return p;
    }

    public void setLists(List<TodoList> lists, Map<Integer, Integer> counts) {
        listModel.clear();
        for (TodoList l : lists) listModel.addElement(l);
        listJList.setCellRenderer(new TodoListCellRenderer(counts));
        listJList.repaint();
    }

    public int getListCount() { return listModel.size(); }

    public void selectIndex(int idx) {
        if (idx < 0 || idx >= listModel.size()) return;
        listJList.setSelectedIndex(idx);
        listJList.ensureIndexIsVisible(idx);
    }

    public void selectListById(int id) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getId() == id) {
                selectIndex(i);
                return;
            }
        }
    }

    private void pill(JButton b) {
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI", Font.BOLD, 20));
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "U";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}

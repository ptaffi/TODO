package GUI.panels;

import GUI.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel {

    public interface Listener {
        void onHeaderShare();
        void onHeaderRename();
        void onHeaderDelete();
    }

    private final Listener listener;
    private JLabel Title;
    private JLabel Date;
    private JButton btnShare;
    private JButton btnRename;
    private JButton btnDelete;

    public HeaderPanel(Listener listener) {
        this.listener = listener;
        initUI();
        initEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 190));
        setBorder(new EmptyBorder(26, 28, 18, 28));
        setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        Title = new JLabel("...");
        Title.setForeground(Color.WHITE);
        Title.setFont(new Font("Segoe UI", Font.BOLD, 52));

        Date = new JLabel("");
        Date.setForeground(new Color(230, 230, 230));
        Date.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        left.add(Title);
        left.add(Box.createVerticalStrut(6));
        left.add(Date);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        btnShare = new JButton("Share");
        btnRename = new JButton("Rename");
        btnDelete = new JButton("Delete");

        pill(btnShare);
        pill(btnRename);
        pill(btnDelete);

        right.add(btnShare);
        right.add(btnRename);
        right.add(btnDelete);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }

    private void initEvents() {
        btnShare.addActionListener(e -> listener.onHeaderShare());
        btnRename.addActionListener(e -> listener.onHeaderRename());
        btnDelete.addActionListener(e -> listener.onHeaderDelete());
    }

    public void setTitleText(String text) {
        Title.setText(text == null ? "..." : text);
    }

    public void setDateText(String text) {
        Date.setText(text == null ? "" : text);
    }

    private void pill(JButton b) {
        b.putClientProperty("JButton.buttonType", "roundRect");
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(MainFrame.BG_HEADER); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
    }
}

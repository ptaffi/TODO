package GUI;

import DAO.UserDAO;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegisterGUI extends JFrame {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPass;
    private JPasswordField txtPass2;
    private JButton btnRegister;
    private JButton btnBack;
    private static final Color PRIMARY = new Color(188, 143, 187);
    private static final Color BG = new Color(250, 245, 250);
    private static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public RegisterGUI() {
        initUI();
        initEvents();
    }

    private void initUI() {
        setTitle("FINI - Register");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(BG);

        JPanel card = new TranslucentCard(0.92f, 22);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(560, 460));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(PRIMARY);
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 20, 10);
        card.add(title, gbc);

        gbc.insets = new Insets(6, 10, 6, 10);

        // username
        gbc.gridy = 1;
        card.add(label("Username"), gbc);
        txtUsername = field(" ");
        gbc.gridy = 2;
        card.add(txtUsername, gbc);

        // email
        gbc.gridy = 3;
        card.add(label("Email"), gbc);
        txtEmail = field(" ");
        gbc.gridy = 4;
        card.add(txtEmail, gbc);

        // pass
        gbc.gridy = 5;
        card.add(label("Password"), gbc);
        txtPass = passField(" ");
        gbc.gridy = 6;
        card.add(txtPass, gbc);

        // pass2
        gbc.gridy = 7;
        card.add(label("Confirm Password"), gbc);
        txtPass2 = passField(" ");
        gbc.gridy = 8;
        card.add(txtPass2, gbc);

        // buttons
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);

        btnBack = new JButton("QUAY LẠI");
        btnRegister = new JButton("TẠO TÀI KHOẢN");

        styleBtn(btnBack, true);
        styleBtn(btnRegister, true);

        row.add(btnBack);
        row.add(btnRegister);

        gbc.gridy = 9;
        gbc.insets = new Insets(18, 10, 5, 10);
        card.add(row, gbc);

        bg.add(card);
        setContentPane(bg);
    }

    private JLabel label(String s) {
        JLabel lb = new JLabel(s);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lb.setForeground(PRIMARY);
        return lb;
    }

    private JTextField field(String placeholder) {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(320, 45));
        t.setFont(FONT);
        t.putClientProperty("JTextField.placeholderText", placeholder);
        t.putClientProperty("JTextField.showClearButton", true);
        return t;
    }

    private JPasswordField passField(String placeholder) {
        JPasswordField t = new JPasswordField();
        t.setPreferredSize(new Dimension(320, 45));
        t.setFont(FONT);
        t.putClientProperty("JTextField.placeholderText", placeholder);
        t.putClientProperty("JPasswordField.showRevealButton", true);
        return t;
    }

    private void styleBtn(JButton b, boolean primary) {
        b.setPreferredSize(new Dimension(320, 52));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (primary) {
            b.setBackground(PRIMARY);
            b.setForeground(Color.WHITE);
        }
    }

    private void initEvents() {
        btnBack.addActionListener(e -> {
            dispose();
            EventQueue.invokeLater(() -> new LoginGUI().setVisible(true));
        });

        btnRegister.addActionListener(e -> doRegister());

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doRegister();
            }
        };
        txtUsername.addKeyListener(enter);
        txtEmail.addKeyListener(enter);
        txtPass.addKeyListener(enter);
        txtPass2.addKeyListener(enter);
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String pass1 = new String(txtPass.getPassword()).trim();
        String pass2 = new String(txtPass2.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ username/email/password!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!", "Sai", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (UserDAO.existsUsernameOrEmail(username, email)) {
                JOptionPane.showMessageDialog(this, "Username hoặc Email đã tồn tại!", "Trùng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDAO.register(username, pass1, email);

            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
            dispose();
            EventQueue.invokeLater(() -> new LoginGUI().setVisible(true));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng ký: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    static class TranslucentCard extends JPanel {
        private final float alpha;
        private final int radius;

        public TranslucentCard(float alpha, int radius) {
            this.alpha = alpha;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);

            new RegisterGUI().setVisible(true);
        });
    }
}

package GUI;

import com.formdev.flatlaf.FlatLightLaf;
import DAO.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import java.awt.image.BufferedImage;
// import java.io.IOException;

public class LoginGUI extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    private static final Color PRIMARY_COLOR = new Color(188, 143, 187);
    private static final Color SECONDARY_COLOR = new Color(221, 168, 220);
    private static final Color TEXT_COLOR = new Color(188, 143, 187);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color BUTTON_MAIN = new Color(188, 143, 187);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Color FOCUS_COLOR  = new Color(218, 164, 214);
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("control", new Color(250, 245, 250));
            UIManager.put("Panel.background", new Color(250, 245, 250));
            UIManager.put("TextField.background", new Color(250, 245, 250));
            UIManager.put("TextField.foreground", new Color(60, 45, 30));
            UIManager.put("TextArea.background", new Color(250, 245, 250));
            UIManager.put("TextArea.foreground", new Color(60, 45, 30));
            // UIManager.put("Label.foreground", new Color(60, 45, 30));
            UIManager.put("Button.background", BUTTON_MAIN);
            UIManager.put("Button.foreground", BUTTON_TEXT);
            UIManager.put("Button.focusColor", FOCUS_COLOR);
            UIManager.put("Button.hoverBackground", BUTTON_MAIN.brighter());
            UIManager.put("Button.pressedBackground", BUTTON_MAIN.darker());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new LoginGUI().setVisible(true);
    }

    public LoginGUI() {
        initUI();
        initEvents();
    }

        private void initUI() {
        setTitle("FINI");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(250, 245, 250)); 

        JPanel card = createLoginCard();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 30, 30, 30);
        bg.add(card, gbc);

        setContentPane(bg);
    }

    private JPanel createLoginCard() {
        TranslucentCard card = new TranslucentCard(0.92f, 22); // opacity, radius
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(520, 420));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        //Title
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitle.setForeground(PRIMARY_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 25, 10);
        card.add(lblTitle, gbc);

        // User 
        JLabel lblUser = new JLabel("User Name/ Email");
        lblUser.setFont(FONT_BOLD);
        lblUser.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 6, 10);
        card.add(lblUser, gbc);

        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(320, 45));
        txtUser.putClientProperty("JTextField.placeholderText", "Nhập tài khoản");
        txtUser.putClientProperty("JTextField.showClearButton", true);
        txtUser.setFont(FONT_PLAIN);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 12, 10);
        card.add(txtUser, gbc);

        // Pass
        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(FONT_BOLD);
        lblPass.setForeground(TEXT_COLOR);
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 10, 6, 10);
        card.add(lblPass, gbc);

        txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(320, 45));
        txtPass.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        txtPass.putClientProperty("JPasswordField.showRevealButton", true);
        txtPass.setFont(FONT_PLAIN);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 10, 18, 10);
        card.add(txtPass, gbc);

        // Button
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setPreferredSize(new Dimension(320, 52));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(22, 10, 5, 10);
        card.add(btnLogin, gbc);

        return card;
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> checkLogin());

        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) checkLogin();
            }
        };
        txtUser.addKeyListener(enterKey);
        txtPass.addKeyListener(enterKey);

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(SECONDARY_COLOR); }
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(PRIMARY_COLOR); }
        });
    }

    private void checkLogin() {
        String identifier = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (identifier.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // JOptionPane.showMessageDialog(this, "OK (demo)", "Message", JOptionPane.INFORMATION_MESSAGE);
     try {
        User u = UserDAO.login(identifier, pass); // login trả về User 

        if (u != null) {
            int userId = u.getId(); 
            this.dispose();
            EventQueue.invokeLater(() -> new MainFrame(u.getId(), u.getUsername(), u.getEmail()).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản/email hoặc mật khẩu!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }

    static class BackgroundPanel extends JPanel {
        @Override
         protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(222, 168, 220));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
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
}

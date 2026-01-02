package GUI;

import DAO.UserDAO;
import util.MailService;

import javax.swing.*;
import java.awt.*;

public class ForgotPassword extends JDialog {

    private JTextField txtEmail = new JTextField();
    private JTextField txtCode = new JTextField();
    private JPasswordField txtNewPass = new JPasswordField();
    private JPasswordField txtNewPass2 = new JPasswordField();

    private JButton btnSend = new JButton("Gửi OTP");
    private JButton btnReset = new JButton("Đổi mật khẩu");

    public ForgotPassword(Window owner) {
        super(owner, "Quên mật khẩu", ModalityType.APPLICATION_MODAL);
        initUI();
        initEvents();
    }

    private void initUI() {
        setSize(420, 380);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(8, 8));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        p.add(field("Email", txtEmail));
        p.add(Box.createVerticalStrut(6));
        p.add(field("OTP", txtCode));
        p.add(Box.createVerticalStrut(6));
        p.add(field("Mật khẩu mới", txtNewPass));
        p.add(Box.createVerticalStrut(6));
        p.add(field("Nhập lại mật khẩu mới", txtNewPass2));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 12, 0));
        btnSend.setPreferredSize(new Dimension(0, 36));
        btnReset.setPreferredSize(new Dimension(0, 36));

        btnRow.add(btnSend);
        btnRow.add(btnReset);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 12, 10, 12));
        bottom.add(btnRow);

        add(p, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnReset.setEnabled(false);
    }
    private JPanel field(String label, JTextField tf) {
        tf.setPreferredSize(new Dimension(0, 28)); 

        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);
        return p;
    }

    private void initEvents() {
        btnSend.addActionListener(e -> onSendOtp());
        btnReset.addActionListener(e -> onReset());
    }

    private void onSendOtp() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập email!");
            return;
        }

        try {
            String code = UserDAO.createResetCodeByEmail(email, 5); // 5 phút
            if (code == null) {
                JOptionPane.showMessageDialog(this, "Email không tồn tại hoặc chưa đăng ký email.");
                return;
            }

            // gửi mail
            MailService.sendOtp(email, code);

            JOptionPane.showMessageDialog(this, "Đã gửi OTP về email!");
            btnReset.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi gửi OTP: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void onReset() {
        String email = txtEmail.getText().trim();
        String code = txtCode.getText().trim();
        String p1 = new String(txtNewPass.getPassword()).trim();
        String p2 = new String(txtNewPass2.getPassword()).trim();

        if (email.isEmpty() || code.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ email/otp/mật khẩu!");
            return;
        }
        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!");
            return;
        }

        try {
            boolean ok = UserDAO.resetPasswordByEmailAndCode(email, code, p1);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "OTP sai hoặc hết hạn!");
                return;
            }

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đổi mật khẩu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

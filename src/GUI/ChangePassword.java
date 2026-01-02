package GUI;

import DAO.UserDAO;

import javax.swing.*;
import java.awt.*;

public class ChangePassword extends JDialog {

    private JPasswordField txtOld = new JPasswordField();
    private JPasswordField txtNew1 = new JPasswordField();
    private JPasswordField txtNew2 = new JPasswordField();

    private boolean ok = false;

    public ChangePassword(Window owner, int userId) {
        super(owner, "Đổi mật khẩu", ModalityType.APPLICATION_MODAL);
        initUI(userId);
    }

   private void initUI(int userId) {
    setSize(420, 380);
    setLocationRelativeTo(getOwner());
    setLayout(new BorderLayout(8, 8));

    JPanel p = new JPanel(new GridLayout(0, 1, 8, 8));
    p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    p.add(new JLabel("Mật khẩu cũ:"));
    p.add(txtOld);
    p.add(new JLabel("Mật khẩu mới:"));
    p.add(txtNew1);
    p.add(new JLabel("Nhập lại mật khẩu mới:"));
    p.add(txtNew2);

    JButton btn = new JButton("Cập nhật");
    btn.setPreferredSize(new Dimension(0, 42)); 
    btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f)); 
    btn.addActionListener(e -> {
        String oldP = new String(txtOld.getPassword()).trim();
        String n1 = new String(txtNew1.getPassword()).trim();
        String n2 = new String(txtNew2.getPassword()).trim();

        if (oldP.isEmpty() || n1.isEmpty() || n2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đủ dữ liệu!");
            return;
        }
        if (!n1.equals(n2)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp!");
            return;
        }

        try {
            boolean okChange = UserDAO.changePassword(userId, oldP, n1);
            if (!okChange) {
                JOptionPane.showMessageDialog(this, "Mật khẩu cũ sai!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            ok = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    JPanel bottom = new JPanel(new BorderLayout());
    bottom.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
    bottom.add(btn, BorderLayout.CENTER);

    add(p, BorderLayout.CENTER);
    add(bottom, BorderLayout.SOUTH);
}

    public boolean isOk() {
        return ok;
    }
}

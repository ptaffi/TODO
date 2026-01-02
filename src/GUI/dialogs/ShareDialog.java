package GUI.dialogs;

import DAO.ShareDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ShareDialog extends JDialog {

    private final int listId;
    private final int userId;

    private final JTextField txtCode = new JTextField();
    private final JTextField txtAccept = new JTextField();

    public ShareDialog(JFrame owner, int listId, int userId) {
        super(owner, "Share", true);
        this.listId = listId;
        this.userId = userId;

        setSize(520, 360);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Generate code", GeneratePanel());
        tabs.addTab("Accept code", AcceptPanel());
        add(tabs);
    }

    private JPanel GeneratePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        p.add(new JLabel("Expire (days)"), gbc);
        gbc.gridy++;

        Integer[] opts = {1, 7, 14, 30};
        JComboBox<Integer> cbDays = new JComboBox<>(opts);
        cbDays.setPreferredSize(new Dimension(0, 40));
        p.add(cbDays, gbc);

        gbc.gridy++;
        p.add(new JLabel("Share code"), gbc);
        gbc.gridy++;

        txtCode.setPreferredSize(new Dimension(0, 40));
        txtCode.setEditable(false);
        p.add(txtCode, gbc);

        gbc.gridy++;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnGen = new JButton("Generate");
        JButton btnCopy = new JButton("Copy");
        row.add(btnGen);
        row.add(btnCopy);
        p.add(row, gbc);

        btnGen.addActionListener(e -> {
            try {
                int days = (Integer) cbDays.getSelectedItem();
                String code = ShareDAO.genShareCode(listId, userId, days);
                txtCode.setText(code);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Generate failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCopy.addActionListener(e -> {
            String s = txtCode.getText().trim();
            if (s.isEmpty()) return;
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
            JOptionPane.showMessageDialog(this, "Copied!");
        });

        return p;
    }

    private JPanel AcceptPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        p.add(new JLabel("Paste share code"), gbc);
        gbc.gridy++;

        txtAccept.setPreferredSize(new Dimension(0, 40));
        p.add(txtAccept, gbc);

        gbc.gridy++;
        JButton btnAccept = new JButton("Accept & copy to my lists");
        p.add(btnAccept, gbc);

        btnAccept.addActionListener(e -> {
            String code = txtAccept.getText().trim();
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập code.");
                return;
            }
            try {
                ShareDAO.acceptShareAndCopy(code, userId);
                JOptionPane.showMessageDialog(this, "Đã copy list về tài khoản.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Accept failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        return p;
    }
}

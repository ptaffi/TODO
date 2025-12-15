package GUI.dialogs;

import model.Task;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;

public class TaskDialog extends JDialog {

    public boolean ok = false;

    public String title = "";
    public String note = "";
    public Date dueDate = null;
    public boolean completed = false;

    private final JTextField txtTitle = new JTextField();
    private final JTextArea txtNote = new JTextArea(4, 20);
    private final JTextField txtDue = new JTextField(); // yyyy-mm-dd or empty
    private final JCheckBox cbDone = new JCheckBox("Completed");

    public TaskDialog(JFrame owner, String dialogTitle, Task existing) {
        super(owner, dialogTitle, true);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        content.add(new JLabel("Title"), gbc);
        gbc.gridy++;
        txtTitle.setPreferredSize(new Dimension(0, 40));
        content.add(txtTitle, gbc);

        gbc.gridy++;
        content.add(new JLabel("Note"), gbc);
        gbc.gridy++;

        gbc.fill = GridBagConstraints.BOTH;
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setEditable(true);

        JScrollPane sp = new JScrollPane(txtNote);
        sp.setPreferredSize(new Dimension(0, 120));
        content.add(sp, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(new JLabel("Due date (yyyy-mm-dd, optional)"), gbc);
        gbc.gridy++;
        txtDue.setPreferredSize(new Dimension(0, 40));
        content.add(txtDue, gbc);

        gbc.gridy++;
        content.add(cbDone, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = new JButton("Cancel");
        JButton btnOk = new JButton("Save");
        buttons.add(btnCancel);
        buttons.add(btnOk);

        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        if (existing != null) {
            txtTitle.setText(existing.getTitle());
            txtNote.setText(existing.getNote() == null ? "" : existing.getNote());
            txtDue.setText(existing.getDueDate() == null ? "" : existing.getDueDate().toString());
            cbDone.setSelected(existing.isCompleted());
        }

        btnCancel.addActionListener(e -> dispose());
        btnOk.addActionListener(e -> {
            String t = txtTitle.getText().trim();
            String n = txtNote.getText().trim();
            String d = txtDue.getText().trim();

            if (t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title không được rỗng!");
                return;
            }

            Date dd = null;
            if (!d.isEmpty()) {
                try {
                    dd = Date.valueOf(d);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Due date sai định dạng (yyyy-mm-dd)!");
                    return;
                }
            }

            this.title = t;
            this.note = n;
            this.dueDate = dd;
            this.completed = cbDone.isSelected();
            this.ok = true;

            dispose();
        });
    }
}

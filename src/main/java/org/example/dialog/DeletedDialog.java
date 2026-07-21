package org.example.dialog;

import javax.swing.*;
import java.awt.*;

public class DeletedDialog extends JDialog {

    private boolean setupCompleted;

    public DeletedDialog(Frame parent) {
        super(parent, "Deleted?", ModalityType.APPLICATION_MODAL);
        setSize(200, 100);
        setLocationRelativeTo(parent);
        setupCompleted = false;

        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnOk());
        buttonPanel.add(btnCansel());
        add(buttonPanel);
        setVisible(true);
    }

    private Component btnOk() {
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(e -> {
            setupCompleted = true;
            dispose();
        });
        return btnOk;
    }
    private Component btnCansel() {
        JButton btnCansel = new JButton("Cansel");
        btnCansel.addActionListener(e -> {
            setupCompleted = false;
            dispose();
        });
        return btnCansel;
    }

    public boolean isSetupCompleted() {
        return setupCompleted;
    }
}

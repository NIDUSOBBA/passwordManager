package org.example.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.javakeyring.PasswordAccessException;
import org.example.utile.KeyringMasterKeyUtil;

import javax.swing.*;
import java.awt.*;

public class MasterKeyPromptDialog extends JDialog {
    private JTextField masterKeyField;
    private KeyringMasterKeyUtil masterKeyUtil;

    private boolean isValid = false;

    public MasterKeyPromptDialog(Frame parent, KeyringMasterKeyUtil masterKeyUtil) {
        super(parent, "Enter master key", ModalityType.APPLICATION_MODAL);
        this.masterKeyUtil = masterKeyUtil;
        setSize(600,150);
        setLocationRelativeTo(parent);
        FlatLightLaf.setup();
        add(createMessage());
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(3,1,10,10));

        JPanel passPanel = new JPanel(new BorderLayout(5,0));
        masterKeyField = new JTextField();
        passPanel.add(masterKeyField, BorderLayout.CENTER);
        add(passPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnOk());
        add(buttonPanel);
    }

    private JButton btnOk() {
        JButton buttonOk = new JButton("Ok");
        buttonOk.addActionListener(e -> {
            if (!masterKeyField.getText().isEmpty() || !masterKeyField.getText().isBlank()){
                try {
                    if (masterKeyField.getText().equals(masterKeyUtil.get())){
                        isValid = true;
                        dispose();
                    }else {
                        notValidKey();
                    }
                } catch (PasswordAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }else {
                notValidKey();
            }
        });
        return buttonOk;
    }

    private void notValidKey() {
        JOptionPane.showMessageDialog(this,"Not valid master key");
    }

    private JTextPane createMessage() {
        JTextPane pane = new JTextPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        String message = "Please enter yor master key";
        pane.setText(message);
        return pane;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }
}

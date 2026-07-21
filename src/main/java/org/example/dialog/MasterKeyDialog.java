package org.example.dialog;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.utile.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

//Диалог для создания нового мастер ключа
public class MasterKeyDialog extends JDialog {

    private JTextField masterKeyField;
    private boolean setupCompleted = false;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";


    public MasterKeyDialog(Frame parent) {
        super(parent, "Enter the master key", ModalityType.APPLICATION_MODAL);
        setSize(800, 310);
        setLocationRelativeTo(parent);
        setResizable(false);
        FlatLightLaf.setup();
        add(createHtmlPane());
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 1, 10, 10));

        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        masterKeyField = new JTextField();
        passPanel.add(masterKeyField, BorderLayout.CENTER);

        passPanel.add(btnGen(), BorderLayout.EAST);
        add(passPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(btnOk());
        buttonPanel.add(btnCansel());

        add(buttonPanel);
    }

    private JButton btnGen() {
        JButton btnGenerate = new JButton("🎲 Generate");
        btnGenerate.setFocusPainted(false);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGenerate.setBackground(new Color(70, 130, 180)); // Синий
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 12));

        btnGenerate.addActionListener(e -> {
            String generated = generateSecurePassword();
            masterKeyField.setText(generated);
        });
        return btnGenerate;
    }

    private JButton btnOk() {
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(e -> {
            if (!masterKeyField.getText().isEmpty()) {
                handleContinue();
            } else {
                AppLogger.warn("Master key can't be empty");
            }
        });
        return btnOk;
    }

    private void handleContinue() {
        int choice = JOptionPane.showConfirmDialog(this,
                "<html><b>Are you sure you want to install this key?</b><br>" +
                        "ATTENTION: The key cannot be changed after creation. Please remember your key.<br>",
                "Confirmation of the key installation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION){
            setupCompleted = true;
            dispose();
        }
    }

    private JButton btnCansel() {
        JButton btnCansel = new JButton("Cansel");
        btnCansel.addActionListener(e -> {
            setupCompleted = false;
            dispose();
        });
        return btnCansel;
    }

    private String generateSecurePassword() {
        StringBuilder sb = new StringBuilder(30);
        for (int i = 0; i < 30; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(PASSWORD_CHARS.length());
            sb.append(PASSWORD_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public String getMasterKey() {
        return masterKeyField.getText().trim();
    }

    public boolean isSetupCompleted() {
        return setupCompleted;
    }

    public JTextPane createHtmlPane() {
        JTextPane htmlPane = new JTextPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);

        htmlPane.setText("<html><body>" +
                "<h1 style='color: #333;'>Master Key</h1>" +
                "<p style='color: #666;'>The master key is a secret code that is used to encrypt and decrypt your data." +
                "It is important to keep your master key safe and secure." +
                "If it is lost, access to the data may be lost" +
                "</p>");

        htmlPane.setText(htmlPane.getText());
        return htmlPane;
    }
}

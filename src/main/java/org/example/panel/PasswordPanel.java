package org.example.panel;

import org.example.controller.WindowManager;
import org.example.dto.PasswordDto;
import org.example.service.PasswordService;
import org.example.utile.AppLogger;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PasswordPanel implements BasePage, BaseMethod {
    private final PasswordService passwordService;
    private DefaultTableModel passwordModel;
    private WindowManager windowManager;
    private JTable table;
    private static JComboBox<PasswordDto> passwordComboBox;


    public PasswordPanel(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }


    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Пароль"};
        passwordModel = DefaultModel.creteModel(cols);
        table = new JTable(passwordModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel) {
        JButton btnAdd = new JButton("Добавить");
        btnAdd.addActionListener(e -> add());
        JButton bthDelete = new JButton("Удалить");
        bthDelete.addActionListener(e -> delete());
        JPanel batons = new JPanel();
        batons.add(btnAdd);
        batons.add(bthDelete);
        jPanel.add(batons, BorderLayout.SOUTH);
    }

    @Override
    public void loadTable() {
        passwordComboBox = new JComboBox<>();
        List<PasswordDto> all = passwordService.getAll();
        System.out.println("All passwords: " + all);
        if (!all.isEmpty()){
            for (PasswordDto p : all) {
                passwordModel.addRow(new Object[]{
                        p.id(),
                        p.encryptedPassword()
                });
                passwordComboBox.addItem(p);
            }
        }
    }

    @Override
    public void add() {
        JTextField password = new JTextField();
        Object[] message = {
                "Password", password,
        };
        int option = JOptionPane.showConfirmDialog(windowManager, message, "New password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String passwordText = password.getText();
            if (!passwordText.isEmpty()) {
                try {
                    passwordService.create(passwordText);
                    PasswordDto last = passwordService.getLast();
                    passwordModel.addRow(new Object[]{last.id(), last.encryptedPassword()});
                    passwordComboBox.addItem(last);
                    AppLogger.info("Password added");
                } catch (Exception e) {
                    AppLogger.error("Password add exception: ", e);
                }
            } else {
                AppLogger.warn("Password can't be empty");
            }
        } else {
            AppLogger.info("Password is not added");
        }
    }

    @Override
    public void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirmDialog = JOptionPane.showConfirmDialog(windowManager, "Delete password?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                int id = (int) passwordModel.getValueAt(selectedRow, 0);
                try {
                    passwordModel.removeRow(selectedRow);
                    passwordService.deleteById(id);
                    passwordComboBox.removeItemAt(selectedRow);
                    AppLogger.info("Password deleted");
                } catch (Exception e) {
                    AppLogger.error("Password delete exception: ", e);
                }
            }
        } else {
            AppLogger.warn("Password is not selected");
        }
    }

    public static JComboBox<PasswordDto> getPasswordComboBox() {
        return passwordComboBox;
    }


}

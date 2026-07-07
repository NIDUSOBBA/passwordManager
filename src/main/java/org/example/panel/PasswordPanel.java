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

public class PasswordPanel implements BasePage,BaseMethod{
    private DefaultTableModel passwordModel;
    private WindowManager windowManager;
    private final PasswordService passwordService;
    private JTable table;

    public PasswordPanel(PasswordService passwordService){
        this.passwordService = passwordService;
    }
    @Override
    public void setWindowManager(WindowManager windowManager){
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
    public void buttonIn(JPanel jPanel){
        JButton btnAdd = new JButton("Добавить");
        btnAdd.addActionListener(e -> add());
        JButton bthDelete = new JButton("Удалить");
        bthDelete.addActionListener(e -> delete());
        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    }

    @Override
    public void loadTable() {
        List<PasswordDto> all = passwordService.getAll();
        for (PasswordDto p: all){
            passwordModel.addRow(new Object[]{
                    p.id(),
                    p.encryptedPassword()
            });
        }
    }

    @Override
    public void add() {
        JTextField password = new JTextField();
        Object[] message = {
          "Password", password
        };

        int option = JOptionPane.showConfirmDialog(windowManager, message,"New password",JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            String passwordText = password.getText();

            if (!passwordText.isEmpty()){
                passwordService.create(passwordText);
                PasswordDto last = passwordService.getLast();
                passwordModel.addRow(new Object[]{last.id(),last.encryptedPassword()});
                AppLogger.info("New password added");
            }else {
                AppLogger.warn("Password can't be empty");
            }
        }
    }

    @Override
    public void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0){
            int confirmDialog = JOptionPane.showConfirmDialog(windowManager, "Delete password?");
            if (confirmDialog == JOptionPane.YES_OPTION){
                passwordModel.removeRow(selectedRow);
                passwordService.deleteById(selectedRow);
                AppLogger.info("Password deleted");
            }
        }else {
            AppLogger.warn("Password is not selected");
        }
    }


}

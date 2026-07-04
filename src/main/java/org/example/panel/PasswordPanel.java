package org.example.panel;

import org.example.dto.PasswordDto;
import org.example.service.PasswordService;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PasswordPanel implements BasePage,BaseMethod{
    private DefaultTableModel passwordModel;
    private final PasswordService passwordService;

    public PasswordPanel(PasswordService passwordService){
        this.passwordService = passwordService;
    }


    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Пароль"};
        passwordModel = DefaultModel.creteModel(cols);
        JTable table = new JTable(passwordModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel){
        JButton btnAdd = new JButton("Добавить");
//        btnAdd.addActionListener(e -> addPassword());
        JButton bthDelete = new JButton("Удалить");
//        bthDelete.addActionListener(e -> deletePassword(table, model, tableId));

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
        /*
        JTextField password = new JTextField();
        Object[] message = {
          "Password", password
        };

        int option = JOptionPane.showConfirmDialog(, message,"New password",JOptionPane.OK_CANCEL_OPTION);
         */
    }

    @Override
    public void delete() {

    }
}

package org.example.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PasswordPanel extends BasePage{
    private DefaultTableModel passwordModel;

    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Пароль"};
        passwordModel = defModel(cols);
        JTable table = new JTable(passwordModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel){
        JButton btnAdd = new JButton("Добавить");
//        btnAdd.addActionListener(e -> addPassword());
        JButton btnUpdate =  new JButton("Обновить");
//        btnUpdate.addActionListener(e -> updatePassword(table, model,tableId));
        JButton bthDelete = new JButton("Удалить");
//        bthDelete.addActionListener(e -> deletePassword(table, model, tableId));

        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(btnUpdate);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    }
}

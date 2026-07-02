package org.example.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccountPanel extends BasePage{
    private DefaultTableModel accountModel;

    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Сервис", "Почта", "Имя", "Пароль", "Время создания", "Время обновления"};
        accountModel = defModel(cols);
        JTable table = new JTable(accountModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel) {
        JButton btnAdd = new JButton("Добавить");
//        btnAdd.addActionListener(e -> addAccount());
        JButton btnUpdate =  new JButton("Обновить");
//        btnUpdate.addActionListener(e -> updateAccount(table, model,tableId));
        JButton bthDelete = new JButton("Удалить");
//        bthDelete.addActionListener(e -> deleteAccount(table, model, tableId));

        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(btnUpdate);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    }
}

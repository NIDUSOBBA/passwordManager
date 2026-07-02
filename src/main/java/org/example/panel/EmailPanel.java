package org.example.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmailPanel extends BasePage{
    private DefaultTableModel emailModel;
    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Почта"};
        emailModel = defModel(cols);
        JTable table = new JTable(emailModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel) {
        JButton btnAdd = new JButton("Добавить");
//        btnAdd.addActionListener(e -> addEmail());
        JButton btnUpdate =  new JButton("Обновить");
//        btnUpdate.addActionListener(e -> updateEmail(table, model,tableId));
        JButton bthDelete = new JButton("Удалить");
//        bthDelete.addActionListener(e -> deleteEmail(table, model, tableId));

        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(btnUpdate);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    }
}

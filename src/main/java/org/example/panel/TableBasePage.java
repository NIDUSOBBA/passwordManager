package org.example.panel;

import javax.swing.*;
import java.awt.*;

public abstract class TableBasePage {

    abstract JPanel createPanel();
    void buttonIn(JPanel jPanel){
        JButton btnAdd = new JButton("Добавить");
        btnAdd.addActionListener(e -> add());
        JButton bthDelete = new JButton("Удалить");
        bthDelete.addActionListener(e -> delete());

        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    };
    abstract void createTable();
    abstract void add();
    abstract void delete();
}

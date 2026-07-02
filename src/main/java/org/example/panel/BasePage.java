package org.example.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public abstract class BasePage {

    public abstract JPanel createPanel();
    public abstract void buttonIn(JPanel jPanel);
    protected DefaultTableModel defModel(String[] cols){
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }
}

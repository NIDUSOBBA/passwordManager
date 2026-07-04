package org.example.utile;

import javax.swing.table.DefaultTableModel;

public class DefaultModel {

    public static DefaultTableModel creteModel(String[] cols){
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }
}

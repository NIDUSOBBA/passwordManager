package org.example.utile;

import javax.swing.table.DefaultTableModel;

public class DefaultModel {

    public static DefaultTableModel creteModel(String[] cols){
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
            //Сортируем id как числа остальное как объекты
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class; //
                }
                return Object.class;
            }
        };
    }
}

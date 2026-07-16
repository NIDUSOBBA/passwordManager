package org.example.panel;


import org.example.controller.MasterWindow;

public interface TableBaseMethod {

    void loadTable();
    void add();
    void delete();
    void setWindowManager(MasterWindow masterWindow);
}

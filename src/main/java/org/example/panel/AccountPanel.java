package org.example.panel;

import org.example.controller.WindowManager;
import org.example.dto.AccountResponseDtoCompose;
import org.example.service.AccountService;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountPanel implements BasePage,BaseMethod{
    private DefaultTableModel accountModel;
    private JTable table;
    private WindowManager windowManager;
    private final AccountService accountService;

    public AccountPanel(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Сервис", "Почта", "Имя", "Пароль", "Время создания", "Время обновления"};
        accountModel = DefaultModel.creteModel(cols);
        table = new JTable(accountModel);
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
        return panel;
    }

    @Override
    public void buttonIn(JPanel jPanel) {
        JButton btnAdd = new JButton("Добавить");
        btnAdd.addActionListener(e -> add());
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

    @Override
    public void loadTable() {
        List<AccountResponseDtoCompose> allCompose = accountService.getAllCompose();
        for (AccountResponseDtoCompose a: allCompose){
            accountModel.addRow(new Object[]{
                    a.id(),
                    a.serviceName(),
                    a.email(),
                    a.username(),
                    a.encryptedPassword(),
                    a.created(),
                    a.updated()
            });
        }
    }

    @Override
    public void add() {
        JTextField service = new JTextField();

    }

    @Override
    public void delete() {

    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

}

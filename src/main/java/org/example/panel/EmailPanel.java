package org.example.panel;

import org.example.controller.WindowManager;
import org.example.dto.EmailDto;
import org.example.service.EmailService;
import org.example.utile.AppLogger;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmailPanel implements BasePage, BaseMethod{
    private DefaultTableModel emailModel;
    private JTable table;
    private WindowManager windowManager;
    private final EmailService emailService;

    public EmailPanel(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Почта"};
        emailModel = DefaultModel.creteModel(cols);
        table = new JTable(emailModel);
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
        JButton bthDelete = new JButton("Удалить");
        bthDelete.addActionListener(e -> delete());

        JPanel btns = new JPanel();
        btns.add(btnAdd);
        btns.add(bthDelete);
        jPanel.add(btns, BorderLayout.SOUTH);
    }

    @Override
    public void loadTable() {
        List<EmailDto> all = emailService.getAll();
        for (EmailDto e: all){
            emailModel.addRow(new Object[]{
                    e.id(),
                    e.email()
            });
        }
    }

    @Override
    public void add() {
        JTextField email = new JTextField();

        Object[] message = {
          "Email", email
        };
        int option = JOptionPane.showConfirmDialog(windowManager, message,"New email",JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            String emailText = email.getText();
            if (!emailText.isEmpty()){
                emailService.create(emailText);
                EmailDto last = emailService.getLast();
                emailModel.addRow(new Object[]{last.id(),last.email()});
                AppLogger.info("Email added");
            }else {
                AppLogger.warn("Email can't be empty");
            }
        }
    }

    @Override
    public void delete() {
        int tableSelectedRow = table.getSelectedRow();
        if (tableSelectedRow >= 0){
            int confirmDialog = JOptionPane.showConfirmDialog(windowManager, "Delete email?");
            if (confirmDialog == JOptionPane.YES_OPTION){
                emailModel.removeRow(tableSelectedRow);
                emailService.deleteById(tableSelectedRow);
                AppLogger.info("Email deleted");
            }
        }else {
            AppLogger.warn("Email is not selected");
        }


    }


}

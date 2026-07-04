package org.example.panel;

import org.example.dao.EmailDao;
import org.example.dto.EmailDto;
import org.example.service.EmailService;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmailPanel implements BasePage, BaseMethod{
    private DefaultTableModel emailModel;
    private EmailService emailService;

    public EmailPanel(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public JPanel createPanel() {
        String[] cols = {"id", "Почта"};
        emailModel = DefaultModel.creteModel(cols);
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
        JButton bthDelete = new JButton("Удалить");
//        bthDelete.addActionListener(e -> deleteEmail(table, model, tableId));

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

    }

    @Override
    public void delete() {

    }
}

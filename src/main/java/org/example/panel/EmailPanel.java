package org.example.panel;

import org.example.controller.MasterWindow;
import org.example.dto.EmailDto;
import org.example.service.EmailService;
import org.example.utile.AppLogger;
import org.example.utile.DefaultModel;
import org.example.utile.NotifyDataChanged;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class EmailPanel extends TableBasePage implements TableBaseMethod {
    private final EmailService emailService;
    private DefaultTableModel emailModel;
    private MasterWindow masterWindow;
    private JTable table;
    private static JComboBox<EmailDto> emailComboBox;


    public EmailPanel(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void setWindowManager(MasterWindow masterWindow) {
        this.masterWindow = masterWindow;
    }

    @Override
    public JPanel createPanel() {
        createTable();
        JPanel panel = new JPanel(new BorderLayout());
        buttonIn(panel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
        return panel;
    }

    @Override
    void createTable() {
        String[] cols = {"id", "Mail"};
        emailModel = DefaultModel.creteModel(cols);
        table = new JTable(emailModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(emailModel);
        table.setRowSorter(sorter);
    }

    @Override
    public void loadTable() {
        emailComboBox = new JComboBox<>();
        List<EmailDto> all = emailService.getAll();
        for (EmailDto e : all) {
            emailModel.addRow(new Object[]{
                    e.id(),
                    e.email()
            });
            emailComboBox.addItem(e);
        }
    }

    @Override
    public void add() {
        JTextField email = new JTextField();

        Object[] message = {
                "Email", email
        };
        int option = JOptionPane.showConfirmDialog(masterWindow, message, "New email", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String emailText = email.getText();
            if (!emailText.isEmpty()) {
                try {
                    emailService.create(emailText);
                    EmailDto last = emailService.getLast();
                    emailModel.addRow(new Object[]{last.id(), last.email()});
                    emailComboBox.addItem(last);
                    AppLogger.info("Email added");
                } catch (Exception e) {
                    AppLogger.error("Email add exception: ", e);
                }
            } else {
                AppLogger.warn("Email can't be empty");
            }
        } else {
            AppLogger.warn("Email is not added");
        }
    }

    @Override
    public void delete() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int confirmDialog = JOptionPane.showConfirmDialog(masterWindow, "Delete email?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                int modelRow = table.convertRowIndexToModel(viewRow);

                int id = (int) emailModel.getValueAt(modelRow, 0);
                try {
                    emailModel.removeRow(modelRow);
                    emailService.deleteById(id);
                    emailComboBox.removeItemAt(viewRow);
                    AppLogger.info("Email deleted");
                    NotifyDataChanged.syncAfterRemoval();
                } catch (Exception e) {
                    AppLogger.error("Email delete exception: ", e);
                }
            }
        } else {
            AppLogger.warn("Email is not selected");
        }
    }

    public static JComboBox<EmailDto> getEmailComboBox() {
        return emailComboBox;
    }


}

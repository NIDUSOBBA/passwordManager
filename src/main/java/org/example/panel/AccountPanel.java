package org.example.panel;

import org.example.controller.MasterWindow;
import org.example.dto.*;
import org.example.service.AccountService;
import org.example.utile.AppLogger;
import org.example.utile.DefaultModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AccountPanel extends TableBasePage implements TableBaseMethod {
    private final AccountService accountService;
    private DefaultTableModel accountModel;
    private JTable table;
    private MasterWindow masterWindow;

    public AccountPanel(AccountService accountService) {
        this.accountService = accountService;
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
    public void createTable(){
        String[] cols = {"id", "Service", "Mail", "Name", "Password", "Created", "Updated"};
        accountModel = DefaultModel.creteModel(cols);
        table = new JTable(accountModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(accountModel);
        table.setRowSorter(sorter);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        edited();
                    }
                }
            }
        });
    }

    @Override
    public void loadTable() {
        List<AccountResponseDtoCompose> allCompose = accountService.getAllCompose();
        for (AccountResponseDtoCompose a : allCompose) {
            accountModel.addRow(createColumns(a));
        }

    }

    @Override
    public void add() {
        JTextField service = new JTextField();
        JComboBox<EmailDto> email = EmailPanel.getEmailComboBox();
        JTextField username = new JTextField();
        JComboBox<PasswordDto> password = PasswordPanel.getPasswordComboBox();
        Object[] message = {
                "Service", service,
                "Email", email,
                "Username", username,
                "Password", password
        };

        int option = JOptionPane.showConfirmDialog(masterWindow, message, "New account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (service.getText().isEmpty()) {
                AppLogger.warn("Service can't be empty");
                return;
            }
            String serviceText = service.getText();
            EmailDto emailText = (EmailDto) email.getSelectedItem();
            String usernameText = username.getText();
            PasswordDto passwordText = (PasswordDto) password.getSelectedItem();
            try {
                AccountCreateDto account = new AccountCreateDto(serviceText, emailText.id(), usernameText, passwordText.id());
                accountService.create(account);
                AccountResponseDtoCompose last = accountService.getLastCompose();
                accountModel.addRow(createColumns(last));
                AppLogger.info("Account added");
            } catch (Exception e) {
                AppLogger.error("Account add exception: ", e);
            }
        } else {
            AppLogger.warn("Account is not added");
        }
    }

    @Override
    public void delete() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int confirmDialog = JOptionPane.showConfirmDialog(masterWindow, "Delete account?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                int modelRow = table.convertRowIndexToModel(viewRow);

                int id = (int) accountModel.getValueAt(modelRow, 0);

                try {
                    accountModel.removeRow(modelRow);
                    accountService.deleteById(id);
                    AppLogger.info("Account deleted");
                } catch (Exception e) {
                    AppLogger.error("Account delete exception: ", e);
                }
            } else {
                AppLogger.info("Account not deleted");
            }

        } else {
            AppLogger.warn("Account is not selected");
        }
    }

    public void edited() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) accountModel.getValueAt(selectedRow, 0);
            JTextField service = new JTextField();
            service.setText(accountModel.getValueAt(selectedRow, 1).toString());
            JTextField email = new JTextField();
            email.setText(accountModel.getValueAt(selectedRow, 2).toString());
            JTextField username = new JTextField();
            username.setText(accountModel.getValueAt(selectedRow, 3).toString());
            JTextField password = new JTextField();
            password.setText(accountModel.getValueAt(selectedRow, 4).toString());
            JComboBox<EmailDto> newEmail = EmailPanel.getEmailComboBox();
            JComboBox<PasswordDto> newPassword = PasswordPanel.getPasswordComboBox();
            Object[] message = {
                    "Service", service,
                    "Email", email,
                    "New email", newEmail,
                    "Username", username,
                    "Password", password,
                    "New password", newPassword
            };
            int option = JOptionPane.showConfirmDialog(masterWindow, message, "Edited account", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String serviceText = service.getText();
                EmailDto emailText = (EmailDto) newEmail.getSelectedItem();
                String usernameText = username.getText();
                PasswordDto passwordText = (PasswordDto) newPassword.getSelectedItem();
                try {
                    AccountUpdateDto account = new AccountUpdateDto(id, serviceText, emailText.id(), usernameText, passwordText.id());
                    accountService.update(account);
                    AccountResponseDtoCompose last = accountService.getById(id);
                    accountModel.setValueAt(last.id(), selectedRow, 0);
                    accountModel.setValueAt(last.serviceName(), selectedRow, 1);
                    accountModel.setValueAt(last.email(), selectedRow, 2);
                    accountModel.setValueAt(last.username(), selectedRow, 3);
                    accountModel.setValueAt(last.encryptedPassword(), selectedRow, 4);
                    accountModel.setValueAt(last.created(), selectedRow, 5);
                    accountModel.setValueAt(last.updated(), selectedRow, 6);
                    accountModel.fireTableDataChanged();
                    AppLogger.info("Account edited");
                } catch (Exception e) {
                    AppLogger.error("Account edited exception: ", e);
                }
            } else {
                AppLogger.info("Account not updated");
            }
        } else {
            AppLogger.warn("Account is not selected");
        }
    }

    private Object[] createColumns(AccountResponseDtoCompose account) {
        return new Object[]{
                account.id(),
                account.serviceName(),
                account.email(),
                account.username(),
                account.encryptedPassword(),
                account.created(),
                account.updated()
        };
    }

    @Override
    public void setWindowManager(MasterWindow masterWindow) {
        this.masterWindow = masterWindow;
    }

    public void deleteAllRow(){
        int rowCount = accountModel.getRowCount();
        while (rowCount != 0){
            rowCount--;
            accountModel.removeRow(rowCount);
        }
    }

}

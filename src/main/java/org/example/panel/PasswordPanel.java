package org.example.panel;

import org.example.controller.MasterWindow;
import org.example.dialog.DeletedDialog;
import org.example.dto.PasswordDto;
import org.example.service.PasswordService;
import org.example.utile.AppLogger;
import org.example.utile.DefaultModel;
import org.example.utile.NotifyDataChanged;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class PasswordPanel extends TableBasePage implements TableBaseMethod {
    private final PasswordService passwordService;
    private DefaultTableModel passwordModel;
    private MasterWindow masterWindow;
    private JTable table;
    private static JComboBox<PasswordDto> passwordComboBox;


    public PasswordPanel(PasswordService passwordService) {
        this.passwordService = passwordService;
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
        String[] cols = {"id", "Password"};
        passwordModel = DefaultModel.creteModel(cols);
        table = new JTable(passwordModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(passwordModel);
        table.setRowSorter(sorter);
    }

    @Override
    public void loadTable() {
        passwordComboBox = new JComboBox<>();
        List<PasswordDto> all = passwordService.getAll();
        if (!all.isEmpty()) {
            for (PasswordDto p : all) {
                passwordModel.addRow(new Object[]{
                        p.id(),
                        p.encryptedPassword()
                });
                passwordComboBox.addItem(p);
            }
        }
    }

    @Override
    public void add() {
        JTextField password = new JTextField();
        Object[] message = {
                "Password", password,
        };
        int option = JOptionPane.showConfirmDialog(masterWindow, message, "New password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String passwordText = password.getText();
            if (!passwordText.isEmpty()) {
                try {
                    passwordService.create(passwordText);
                    PasswordDto last = passwordService.getLast();
                    passwordModel.addRow(new Object[]{last.id(), last.encryptedPassword()});
                    passwordComboBox.addItem(last);
                    AppLogger.info("Password added");
                } catch (Exception e) {
                    AppLogger.error("Password add exception: ", e);
                }
            } else {
                AppLogger.warn("Password can't be empty");
            }
        } else {
            AppLogger.warn("Password is not added");
        }
    }

    @Override
    public void delete() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            DeletedDialog deletedDialog = new DeletedDialog(null);
            if (deletedDialog.isSetupCompleted()) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                int id = (int) passwordModel.getValueAt(modelRow, 0);
                try {
                    passwordModel.removeRow(modelRow);
                    passwordService.deleteById(id);
                    passwordComboBox.removeItemAt(viewRow);
                    AppLogger.info("Password deleted");
                    NotifyDataChanged.syncAfterRemoval();
                } catch (Exception e) {
                    AppLogger.error("Password delete exception: ", e);
                }
            }else {
                AppLogger.info("Password is not deleted");
            }
        } else {
            AppLogger.warn("Password is not selected");
        }
    }

    public static JComboBox<PasswordDto> getPasswordComboBox() {
        return passwordComboBox;
    }

}

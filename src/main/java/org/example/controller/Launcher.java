package org.example.controller;

import com.formdev.flatlaf.FlatDarkLaf;
import com.github.javakeyring.Keyring;
import org.example.connection.SQLiteConnection;
import org.example.dao.AccountDao;
import org.example.dao.EmailDao;
import org.example.dao.MetadataDao;
import org.example.dao.PasswordDao;
import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.LogPanel;
import org.example.panel.PasswordPanel;
import org.example.service.*;
import org.example.utile.DatabaseInitializer;
import org.example.utile.KeyringMasterKeyUtil;
import org.example.utile.ResponseComposerAccount;

import javax.swing.*;
import java.sql.Connection;

public class Launcher {

    public static void start()  {
        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            MetadataDao metadataDao = new MetadataDao(connection);
            MasterKeyService masterKeyService = new MasterKeyService(
                    new KeyringMasterKeyUtil(Keyring.create()));
            String masterKey = masterKeyService.get();

            if (masterKey == null) {
                do {
                    masterKeyService.masterKeyInit();
                    masterKey = masterKeyService.get();
                } while (masterKey == null);
            }

            Launcher.managerVaultIn(masterKeyService, metadataDao, connection);
        } catch (Exception e) {
            System.err.println("Launcher initialize exception: " + e.getMessage());
        }
    }

    public static void managerVaultIn(MasterKeyService masterKeyService, MetadataDao metadataDao, Connection connection) throws Exception {
        VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKeyService.get(), metadataDao);
        PasswordDao passwordDao = new PasswordDao(connection, vaultEncryptionService);
        EmailDao emailDao = new EmailDao(connection);
        AccountDao accountDao = new AccountDao(connection);
        ResponseComposerAccount responseComposerAccount = new ResponseComposerAccount(emailDao, passwordDao);
        windowIn(new AccountService(accountDao, responseComposerAccount),
                new PasswordService(passwordDao),
                new EmailService(emailDao));
    }

    public static void windowIn(AccountService accountService, PasswordService passwordService, EmailService emailService) {
        LogPanel logPanel = new LogPanel();
        AccountPanel accountPanel = new AccountPanel(accountService);
        PasswordPanel passwordPanel = new PasswordPanel(passwordService);
        EmailPanel emailPanel = new EmailPanel(emailService);
        SwingUtilities.invokeLater(() -> {
            try {
                FlatDarkLaf.setup();

                UIManager.put("Component.arc", 8);
                UIManager.put("Button.arc", 8);
                UIManager.put("TextComponent.arc", 6);
                UIManager.put("ScrollBar.thumbArc", 999);
                UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
            } catch (Exception e) {
                System.err.println("Exception windows initialization: " + e.getMessage());
            }
            WindowManager app = new WindowManager(logPanel, accountPanel, passwordPanel, emailPanel);
            app.start();
            app.setVisible(true);
        });
    }

}

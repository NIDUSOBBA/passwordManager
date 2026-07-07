package org.example.controller;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.dao.AccountDao;
import org.example.dao.EmailDao;
import org.example.dao.MetadataDao;
import org.example.dao.PasswordDao;
import org.example.panel.AccountPanel;
import org.example.panel.EmailPanel;
import org.example.panel.LogPanel;
import org.example.panel.PasswordPanel;
import org.example.service.*;
import org.example.utile.AppLogger;
import org.example.utile.ResponseComposerAccount;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;

public class Launcher {

    public static void start(MasterKeyService masterKeyService, MetadataDao metadataDao, Connection connection) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Launcher.managerVaultIn(masterKeyService, metadataDao, connection, countDownLatch);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            AppLogger.error("InterruptedException: ", e);
        }
    }

    public static void managerVaultIn(MasterKeyService masterKeyService, MetadataDao metadataDao, Connection connection,CountDownLatch countDownLatch) throws Exception {
        VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKeyService.get(), metadataDao);
        PasswordDao passwordDao = new PasswordDao(connection, vaultEncryptionService);
        EmailDao emailDao = new EmailDao(connection);
        AccountDao accountDao = new AccountDao(connection);
        ResponseComposerAccount responseComposerAccount = new ResponseComposerAccount(emailDao, passwordDao);
        windowIn(new AccountService(accountDao, responseComposerAccount),
                new PasswordService(passwordDao),
                new EmailService(emailDao),
                countDownLatch);
    }

    public static void windowIn(AccountService accountService, PasswordService passwordService, EmailService emailService,CountDownLatch countDownLatch) {
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
                AppLogger.error("Exception windows initialization: ", e);
            }
            WindowManager app = new WindowManager(logPanel, accountPanel, passwordPanel, emailPanel);
            passwordPanel.setWindowManager(app);
            emailPanel.setWindowManager(app);
            accountPanel.setWindowManager(app);
            app.start();
            app.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    countDownLatch.countDown();
                }
            });
            app.setVisible(true);
        });

    }

}

package org.example.controller;

import com.formdev.flatlaf.FlatDarkLaf;
import com.github.javakeyring.BackendNotSupportedException;
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
import org.example.utile.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;

public class Launcher {
    private static MasterKeyService masterKeyService;
    private static AccountService accountService;
    private static PasswordService passwordService;
    private static EmailService emailService;

    public static void start() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try (Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            masterKeyIn();
            repositoryIn(connection);
            windowIn(countDownLatch);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                AppLogger.error("InterruptedException: ", e);
            }
        } catch (Exception e) {
            AppLogger.error("Launcher initialize exception: ", e);
        }

    }

    //Создание мастер ключа, его чтение и создание диалога о мастер ключе
    public static void masterKeyIn () throws BackendNotSupportedException, InterruptedException {
        KeyringMasterKeyUtil keyringMasterKeyUtil = new KeyringMasterKeyUtil(Keyring.create());
        MasterKeyDialogManager masterKeyDialogManager = new MasterKeyDialogManager(keyringMasterKeyUtil);
        masterKeyService = new MasterKeyService(keyringMasterKeyUtil);
        String masterKey = masterKeyService.get();
        if (masterKey == null) {
            masterKeyDialogManager.masterKeyInit();
            masterKeyService.masterKeyCreate(masterKeyDialogManager.getCrDiKey());
        }
        reminderIn(masterKeyDialogManager);
    }

    //Если пользователь долго не заходит в приложение, переспрашивает мастер ключ
    public static void reminderIn(MasterKeyDialogManager masterKeyDialogManager) throws BackendNotSupportedException, InterruptedException {
        MasterKeyReminder masterKeyReminder = new MasterKeyReminder(masterKeyDialogManager);
        masterKeyReminder.start();
    }

    //Создание всех основных классов приложения
    public static void repositoryIn(Connection connection) throws Exception {
        MetadataDao metadataDao = new MetadataDao(connection);
        VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKeyService.get(), metadataDao);
        PasswordDao passwordDao = new PasswordDao(connection, vaultEncryptionService);
        EmailDao emailDao = new EmailDao(connection);
        AccountDao accountDao = new AccountDao(connection);
        ResponseComposerAccount responseComposerAccount = new ResponseComposerAccount(emailDao, passwordDao);
        accountService  = new AccountService(accountDao, responseComposerAccount);
        passwordService = new PasswordService(passwordDao);
        emailService = new EmailService(emailDao);
    }

    //Запуск основного окна приложения
    public static void windowIn(CountDownLatch countDownLatch) {
        LogPanel logPanel = new LogPanel();
        AccountPanel accountPanel = new AccountPanel(accountService);
        NotifyDataChanged.setAccountPanel(accountPanel);
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
            MasterWindow app = new MasterWindow(logPanel, accountPanel, passwordPanel, emailPanel);
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

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
import org.example.utile.ResponseComposerAccount;

import javax.swing.*;
import java.sql.Connection;
import java.util.Scanner;

import static org.example.utile.Const.*;

public class Launcher {

    private final AccountService accountService;
    private final PasswordService passwordService;
    private final EmailService emailService;

    public Launcher(AccountService accountService, PasswordService passwordService, EmailService emailService) {
        this.accountService = accountService;
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean flag = true;
            System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
            while (flag) {
                String line = scanner.nextLine();
                if (line.equals(HELP)) {
                    System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
                } else if (line.equals(END)) {
                    flag = false;
                } else {
                    distribution(line, scanner);
                }
                System.out.println(PLUG);
            }
        }
    }

    public static Launcher getManagerVault(MasterKeyService masterKeyService, MetadataDao metadataDao, Connection connection) throws Exception {
        VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKeyService.get(), metadataDao);
        PasswordDao passwordDao = new PasswordDao(connection, vaultEncryptionService);
        EmailDao emailDao = new EmailDao(connection);
        AccountDao accountDao = new AccountDao(connection);
        ResponseComposerAccount responseComposerAccount = new ResponseComposerAccount(emailDao,passwordDao);
        windowIn(passwordDao);
        return new Launcher(
                new AccountService(accountDao,responseComposerAccount),
                new PasswordService(passwordDao),
                new EmailService(emailDao)
        );
    }

    public static void windowIn(PasswordDao passwordDao){
        LogPanel logPanel = new LogPanel();
        AccountPanel accountPanel = new AccountPanel();
        PasswordPanel passwordPanel = new PasswordPanel();
        EmailPanel emailPanel = new EmailPanel();
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
            WindowManager app = new WindowManager(logPanel,accountPanel,passwordPanel,emailPanel);
            app.start();
            app.setVisible(true);
        });
    }

    public void distribution(String line, Scanner scanner) {
        if (line.startsWith("a")) {
            accountService.search(line, scanner);
        } else if (line.startsWith("p")) {
            passwordService.search(line, scanner);
        } else if (line.startsWith("e")) {
            emailService.search(line, scanner);
        } else {
            System.out.println(NOT_EXISTENT_COMMAND);
        }
    }
}

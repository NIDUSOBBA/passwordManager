package org.example.controller;

import org.example.dao.AccountDao;
import org.example.dao.EmailDao;
import org.example.dao.MetadataDao;
import org.example.dao.PasswordDao;
import org.example.exception.ServiceExceptionHandler;
import org.example.service.AccountService;
import org.example.service.EmailService;
import org.example.service.PasswordService;
import org.example.service.VaultEncryptionService;

import java.sql.Connection;
import java.util.Scanner;

import static org.example.utile.Const.*;

public class ManagerVault {

    private final AccountService accountService;
    private final PasswordService passwordService;
    private final EmailService emailService;

    public ManagerVault(AccountService accountService, PasswordService passwordService, EmailService emailService) {
        this.accountService = accountService;
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    public void start(){
        try(Scanner scanner = new Scanner(System.in)) {
            boolean flag = true;
            System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
            while (flag){
                String line = scanner.nextLine();
                if(line.equals(HELP)){
                    System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
                }else if (line.equals(END)){
                    flag = false;
                }else {
                    distribution(line,scanner);
                }
                System.out.println(PLUG);
            }
        }
    }

    public static ManagerVault getManagerVault(String masterKey, MetadataDao metadataDao, Connection connection) throws Exception {
        VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKey, metadataDao);
        ServiceExceptionHandler serviceExceptionHandler = new ServiceExceptionHandler();

        PasswordDao passwordDao = new PasswordDao(connection,vaultEncryptionService);
        EmailDao emailDao = new EmailDao(connection);
        AccountDao accountDao = new AccountDao(connection);
        return new ManagerVault(
                new AccountService(accountDao, serviceExceptionHandler),
                new PasswordService(passwordDao,serviceExceptionHandler),
                new EmailService(emailDao,serviceExceptionHandler)
        );
    }

    public void distribution(String line, Scanner scanner){
        if (line.startsWith("a")){
            accountService.search(line,scanner);
        }else if(line.startsWith("p")){
            passwordService.search(line,scanner);
        }else if(line.startsWith("e")){
            emailService.search(line,scanner);
        }else {
            System.out.println(NOT_EXISTENT_COMMAND);
        }
    }
}

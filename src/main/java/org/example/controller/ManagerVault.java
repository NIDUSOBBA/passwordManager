package org.example.controller;

import org.example.connection.SQLiteConnection;
import org.example.service.AccountService;

import java.sql.Connection;
import java.util.Scanner;

import static org.example.utile.Const.*;

public class ManagerVault {

    private final Connection connection;
    private final AccountService accountService;

    public ManagerVault(AccountService accountService) {
        this.accountService = accountService;
        this.connection = SQLiteConnection.getConnection();
    }

    public void start(){
        try(Scanner scanner = new Scanner(System.in)) {
            boolean flag = true;
            System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
            while (flag){
                String line = scanner.nextLine();
                if(line.equals("h")){
                    System.out.println(PLUG + "\n" + CONCLUSION + "\n" + COMMANDS);
                }else if (line.equals("end")){
                    flag = false;
                }else {

                }
            }
        }
    }

    public void distribution(String line, Scanner scanner){
        if (line.startsWith("a")){
            accountService.search(line,scanner);
        }else if(line.startsWith("p")){

        }else if(line.startsWith("e")){

        }else {
            System.out.println(NOT_EXISTENT_COMMAND);
        }
    }
}

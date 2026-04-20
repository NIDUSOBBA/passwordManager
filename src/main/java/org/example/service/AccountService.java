package org.example.service;

import org.example.dao.AccountDao;
import org.example.dto.AccountCreateDto;
import org.example.dto.AccountUpdateDto;
import org.example.utile.Const;

import java.util.Scanner;

import static org.example.utile.Const.*;

public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void search(String line,Scanner scanner){
        if (line.length() == 2){
            if (line.equals("ag")){
                System.out.println(accountDao.getAll());
            }else if (line.equals("au")){
                System.out.println(UPDATE_ACCOUNT);
                update(scanner.nextLine());
            }else {
                System.out.println(CREATE_ACCOUNT);
                create(scanner.nextLine());
            }
        }else {

        }
    }

    public void create(String account){
        String[] split = account.split(" ");
        if (split.length != 4){
            System.out.println(INVALID_FIELD + CREATE_ACCOUNT);
            return;
        }
        try {
            accountDao.crete(
                    new AccountCreateDto(
                            split[0],
                            Integer.parseInt(split[1]),
                            split[2],
                            Integer.parseInt(split[3])
                    ));

        }catch (NumberFormatException e){
            System.out.println(INVALID_FIELD + CREATE_ACCOUNT);
        }
    }

    public void update(String account){
        String[] split = account.split(" ");
        if (split.length != 4){
            System.out.println(INVALID_FIELD + UPDATE_ACCOUNT);
            return;
        }
        try {
           accountDao.updateById(
                   new AccountUpdateDto(
                           Integer.parseInt(split[0]),
                           split[1],
                           split[2],
                           Integer.parseInt(split[3])
                   )
           );
        }catch (NumberFormatException e){
            System.out.println(INVALID_FIELD + UPDATE_ACCOUNT);
        }
    }
}

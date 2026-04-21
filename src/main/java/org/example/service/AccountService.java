package org.example.service;

import org.example.dao.AccountDao;
import org.example.dto.AccountCreateDto;
import org.example.dto.AccountUpdateDto;
import org.example.exception.ServiceExceptionHandler;

import java.util.Scanner;

import static org.example.utile.Const.*;

public class AccountService extends BaseService {

    private final AccountDao accountDao;
    private final ServiceExceptionHandler serviceExceptionHandler;

    public AccountService(AccountDao accountDao, ServiceExceptionHandler serviceExceptionHandler) {
        super(serviceExceptionHandler);
        this.accountDao = accountDao;
        this.serviceExceptionHandler = serviceExceptionHandler;
    }

    @Override
    public void search(String line, Scanner scanner) {
        if (line.length() == 2) {
            switch (line) {
                case "ag" -> handleGetAllAccounts();
                case "au" -> handleUpdateAccount(scanner);
                default -> handleCreateAccount(scanner);
            }
        } else {
            if (line.startsWith("ag")) {
                getById(line);
            } else {
                deleteById(line);
            }
        }
    }

    @Override
    public void create(String account) {
        String[] split = account.split(" ");
        if (serviceExceptionHandler.validSplitResponse(split, VALID_ACCOUNT_SPLIT_LENGTH, INVALID_FIELD + CREATE_ACCOUNT)) {
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

        } catch (NumberFormatException e) {
            System.out.println(INVALID_FIELD + CREATE_ACCOUNT);
        }
    }

    @Override
    protected void performGetById(int id) {
        System.out.println(accountDao.getById(id));
    }

    @Override
    public void update(String account) {
        String[] split = account.split(" ");
        if (serviceExceptionHandler.validSplitResponse(split, VALID_ACCOUNT_SPLIT_LENGTH, INVALID_FIELD + UPDATE_ACCOUNT)) {
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
        } catch (NumberFormatException e) {
            System.out.println(INVALID_FIELD + UPDATE_ACCOUNT);
        }
    }

    @Override
    protected void performDeleteById(int id) {
        accountDao.deleteById(id);
    }

    private void handleGetAllAccounts() {
        System.out.println(accountDao.getAll());
    }

    private void handleUpdateAccount(Scanner scanner) {
        System.out.println(UPDATE_ACCOUNT);
        update(scanner.nextLine());
    }

    private void handleCreateAccount(Scanner scanner) {
        System.out.println(CREATE_ACCOUNT);
        create(scanner.nextLine());
    }

}

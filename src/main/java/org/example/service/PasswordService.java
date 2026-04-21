package org.example.service;

import org.example.dao.PasswordDao;
import org.example.dto.PasswordDto;
import org.example.exception.ServiceExceptionHandler;

import java.util.Scanner;

import static org.example.utile.Const.*;

public class PasswordService extends BaseService {

    private final PasswordDao passwordDao;
    private final ServiceExceptionHandler serviceExceptionHandler;

    public PasswordService(PasswordDao passwordDao, ServiceExceptionHandler serviceExceptionHandler) {
        super(serviceExceptionHandler);
        this.passwordDao = passwordDao;
        this.serviceExceptionHandler = serviceExceptionHandler;
    }

    @Override
    public void search(String line, Scanner scanner) {
        if (line.length() == 2) {
            switch (line) {
                case "pg" -> handleGetAllPassword();
                case "pu" -> handleUpdatePassword(scanner);
                default -> handleCreatePassword(scanner);
            }
        } else {
            if (line.startsWith("pg")) {
                getById(line);
            } else {
                deleteById(line);
            }
        }
    }

    @Override
    public void create(String password) {
        passwordDao.crete(password);
    }

    @Override
    protected void performGetById(int id) {
        System.out.println(passwordDao.getById(id));
    }

    @Override
    protected void performDeleteById(int id) {
        passwordDao.deleteById(id);
    }

    @Override
    public void update(String account) {
        String[] split = account.split(" ");
        if (serviceExceptionHandler.validSplitResponse(split, VALID_REQUEST_ID_SPLIT_LENGTH, INVALID_FIELD + UPDATE_PASSWORD)) {
            return;
        }
        try {
            passwordDao.updateById(
                    new PasswordDto(
                            Integer.parseInt(split[0]),
                            split[1]
                    )
            );
        } catch (NumberFormatException e) {
            System.out.println(INVALID_FIELD + UPDATE_PASSWORD);
        }
    }

    private void handleGetAllPassword() {
        System.out.println(passwordDao.getAll());
    }

    private void handleUpdatePassword(Scanner scanner) {
        System.out.println(UPDATE_PASSWORD);
        update(scanner.nextLine());
    }

    private void handleCreatePassword(Scanner scanner) {
        System.out.println(CREATE_PASSWORD);
        create(scanner.nextLine());
    }
}

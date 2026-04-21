package org.example.service;

import org.example.dao.EmailDao;
import org.example.exception.ServiceExceptionHandler;

import java.util.Scanner;

import static org.example.utile.Const.*;

public class EmailService extends BaseService {

    private final EmailDao emailDao;
    private final ServiceExceptionHandler serviceExceptionHandler;

    public EmailService(EmailDao emailDao, ServiceExceptionHandler serviceExceptionHandler) {
        super(serviceExceptionHandler);
        this.emailDao = emailDao;
        this.serviceExceptionHandler = serviceExceptionHandler;
    }

    @Override
    public void search(String line, Scanner scanner) {
        if (line.length() == 2) {
            switch (line) {
                case "eg" -> handleGetAllEmail();
                case "eu" -> update(line);
                default -> handleCreateEmail(scanner);
            }
        } else {
            if (line.startsWith("eg")) {
                getById(line);
            } else {
                deleteById(line);
            }
        }
    }

    @Override
    public void create(String line) {
        emailDao.create(line);
    }

    @Override
    protected void performGetById(int id) {
        System.out.println(emailDao.getById(id));
    }

    @Override
    protected void performDeleteById(int id) {
        emailDao.deleteById(id);
    }

    @Override
    public void update(String line) {
        System.out.println(INVALID_FIELD + COMMANDS);

    }

    private void handleGetAllEmail() {
        System.out.println(emailDao.getAll());
    }


    private void handleCreateEmail(Scanner scanner) {
        System.out.println(CREATE_EMAIL);
        create(scanner.nextLine());
    }
}

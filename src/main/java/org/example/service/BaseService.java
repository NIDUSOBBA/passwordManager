package org.example.service;

import org.example.exception.ServiceExceptionHandler;
import static org.example.utile.Const.*;
import java.util.Scanner;

public abstract class BaseService {
    protected final ServiceExceptionHandler serviceExceptionHandler;
    
    public BaseService(ServiceExceptionHandler serviceExceptionHandler) {
        this.serviceExceptionHandler = serviceExceptionHandler;
    }
    
    public abstract void search(String line, Scanner scanner);
    
    public abstract void create(String data);
    
    public void getById(String data) {
        String[] split = data.split(" ");
        if (serviceExceptionHandler.validSplitResponse(split, 2, INVALID_FIELD + COMMANDS)) {
            return;
        }
        try {
            int id = Integer.parseInt(split[1]);
            performGetById(id);
        } catch (NumberFormatException e) {
            System.out.println(INVALID_FIELD + COMMANDS);
        }
    }
    
    protected abstract void performGetById(int id);
    
    public void deleteById(String data) {
        String[] split = data.split(" ");
        if (serviceExceptionHandler.validSplitResponse(split, 2, INVALID_FIELD + COMMANDS)) {
            return;
        }
        try {
            int id = Integer.parseInt(split[1]);
            performDeleteById(id);
        } catch (NumberFormatException e) {
            System.out.println(INVALID_FIELD + COMMANDS);
        }
    }
    
    protected abstract void performDeleteById(int id);
    
    public abstract void update(String data);

}

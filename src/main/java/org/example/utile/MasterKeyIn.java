package org.example.utile;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.Keyring;
import com.github.javakeyring.PasswordAccessException;

import java.util.Scanner;

import static org.example.utile.Const.*;

public class MasterKeyIn {
    private static final String SERVICE_NAME = "PasswordManager";
    private static final String ACCOUNT_NAME = "MasterKeyService";

    public void masterKeyInitialization() {
        String loadKey = loadMasterKey();
        while (loadKey.isBlank()) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println(CREATE_MASTER_KEY_MESSAGE);
                String key = scanner.nextLine();
                System.out.println(MASTER_KEY_WARNING + key);
                System.out.println(MASTER_KEY_REJECTION);
                String answer = scanner.nextLine();
                if (answer.equals("да")) {
                    saveMasterKey(key);
                } else {
                    System.out.println(PLUG);
                }
            }
            loadKey = loadMasterKey();
        }
    }

    //Сохранение ключа в системном хранилище ОС
    private void saveMasterKey(String masterKey) {
        try {
            Keyring keyring = Keyring.create();
            keyring.setPassword(SERVICE_NAME, ACCOUNT_NAME, masterKey);
        } catch (BackendNotSupportedException e) {
            System.err.println("Ошибка взаимодействия с хранилищем ОС: " + e.getMessage());
        } catch (PasswordAccessException e) {
            System.err.println("Не удалось сохранить MasterKey: " + e.getMessage());
        }
    }

    //Получение ключа из системного хранилища ОС
    public String loadMasterKey() {
        try {
            Keyring keyring = Keyring.create();
            return keyring.getPassword(SERVICE_NAME, ACCOUNT_NAME);
        } catch (BackendNotSupportedException e) {
            System.err.println("Ошибка взаимодействия с хранилищем ОС: " + e.getMessage());
        } catch (PasswordAccessException e) {
            System.err.println("Такого ключа не существует: " + e.getMessage());
        }
        return "";
    }
}

package org.example.service;

import org.example.utile.KeyringMasterKeyUtil;

import java.util.Scanner;

public class MasterKeyService {
    private final KeyringMasterKeyUtil keyUtil;

    public MasterKeyService(KeyringMasterKeyUtil keyUtil) {
        this.keyUtil = keyUtil;
    }

    public void masterKeyInit(){
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Ведите мастер ключ:");
            String key = scanner.nextLine();
            System.out.println("Вы хотите сделать этот ключ мастер ключом?\nДа\nНет");
            String result = scanner.nextLine();
            if (result.equals("Нет")){
                System.out.println("Введите ключ:");
            }else{
                try {
                    keyUtil.create(key);
                } catch (Exception e) {
                    System.out.println("Ошибка сохранения мастер-ключа: " + e.getMessage());
                }
            }
        }
    }

    public String get(){
        try {
            return keyUtil.get();
        } catch (Exception e) {
            System.out.println("Ошибка получения мастер-ключа: " + e.getMessage());
            return null;
        }
    }
}

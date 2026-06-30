package org.example;

import com.github.javakeyring.Keyring;
import org.example.connection.SQLiteConnection;
import org.example.controller.Launcher;
import org.example.dao.MetadataDao;
import org.example.service.MasterKeyService;
import org.example.utile.DatabaseInitializer;
import org.example.utile.KeyringMasterKeyUtil;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            MetadataDao metadataDao = new MetadataDao(connection);
            MasterKeyService masterKeyService = new MasterKeyService(
                    new KeyringMasterKeyUtil(Keyring.create()));
            String masterKey = masterKeyService.get();

            if (masterKey ==  null){
                do {
                    masterKeyService.masterKeyInit();
                    masterKey = masterKeyService.get();
                }while (masterKey == null);
            }

            Launcher launcher = Launcher.getManagerVault(masterKeyService, metadataDao, connection);
            launcher.start();
        } catch (Exception e) {
            System.out.println("Launcher initialize exception: " + e.getMessage());
        }

    }
}
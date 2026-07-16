package org.example;

import com.github.javakeyring.Keyring;
import org.example.connection.SQLiteConnection;
import org.example.controller.Launcher;
import org.example.controller.MasterKeyWindow;
import org.example.dao.MetadataDao;
import org.example.service.MasterKeyService;
import org.example.utile.DatabaseInitializer;
import org.example.utile.KeyringMasterKeyUtil;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            MetadataDao metadataDao = new MetadataDao(connection);
            MasterKeyWindow masterKeyWindow = new MasterKeyWindow(null);
            MasterKeyService masterKeyService = new MasterKeyService(
                    new KeyringMasterKeyUtil(Keyring.create()),
                    masterKeyWindow
            );
            String masterKey = masterKeyService.get();

            if (masterKey == null) {
                masterKeyService.masterKeyInit();
                masterKeyService.masterKeyCreate(masterKeyService.getGenKey());
            }

            Launcher.start(masterKeyService, metadataDao, connection);

        } catch (Exception e) {
            System.err.println("Launcher initialize exception: " + e.getMessage());
        }
    }
}
package org.example;

import org.example.connection.SQLiteConnection;
import org.example.dao.AccountDao;
import org.example.dao.EmailDao;
import org.example.dao.MetadataDao;
import org.example.dao.PasswordDao;
import org.example.dto.AccountResponseDto;
import org.example.utile.DatabaseInitializer;
import org.example.service.VaultEncryptionService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            String masterKey = System.getenv("MASTER_KEY");
            if (masterKey == null) {
                masterKey = System.getProperty("master.key");
            }
            MetadataDao metadataDao = new MetadataDao(connection);

            String s = """
                    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                    █▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█
                    █░██░██░██░██░██░██░██░██░██░░░░░░░░░░█
                    █░██░██░██░██░██░██░██░██░██░░░░░░░░░░█
                    █▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█
                    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                    ░░█░░░░█▀▀▀█░█▀▀█░█▀▀▄░▀█▀░█▄░░█░█▀▀█░░
                    ░░█░░░░█░░░█░█▄▄█░█░░█░░█░░█░█░█░█░▄▄░░
                    ░░█▄▄█░█▄▄▄█░█░░█░█▄▄▀░▄█▄░█░░▀█░█▄▄█░░
                    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                    """;

            VaultEncryptionService vaultEncryptionService = new VaultEncryptionService(masterKey, metadataDao);

            PasswordDao passwordDao = new PasswordDao(connection,vaultEncryptionService);
            EmailDao emailDao = new EmailDao(connection);
            AccountDao dao = new AccountDao(connection);

        } catch (Exception e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }

    }
}
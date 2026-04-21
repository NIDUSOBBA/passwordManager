package org.example.utile;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase(Connection connection){
        String createMetadata = """
                CREATE TABLE IF NOT EXISTS vault_metadata (
                    id INTEGER PRIMARY KEY CHECK (id = 1), 
                    salt BLOB NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
        String createPasswordVaultTable = """
                CREATE TABLE IF NOT EXISTS password (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                encrypted_password BLOB NOT NULL,
                                password_fingerprint TEXT UNIQUE,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                            );
                """;
        String createEmailTable = """
                CREATE TABLE IF NOT EXISTS email (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                email TEXT NOT NULL UNIQUE,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                            );
                """;
        String createAccountTable = """
                CREATE TABLE IF NOT EXISTS account (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                service_name TEXT NOT NULL UNIQUE,
                                email_id INTEGER,
                                username INTEGER,
                                password_id INTEGER NOT NULL,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                updated_at DATETIME,
                                FOREIGN KEY (email_id) REFERENCES email(id) ON DELETE SET NULL,
                                FOREIGN KEY (password_id) REFERENCES password(id) ON DELETE RESTRICT
                            );
                """;
        try(Statement statement = connection.createStatement()) {
            statement.execute(createMetadata);
            statement.execute(createPasswordVaultTable);
            statement.execute(createEmailTable);
            statement.execute(createAccountTable);

        } catch (SQLException e) {
            System.out.println("DatabaseInitializer exception : " + e.getMessage());
        }
    }
}

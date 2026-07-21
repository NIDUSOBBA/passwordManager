package org.example.utile;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase(Connection connection){
        String createMetadataTable = """
                CREATE TABLE IF NOT EXISTS vault_metadata (
                    id INTEGER PRIMARY KEY CHECK (id = 1),
                    salt TEXT NOT NULL,
                    created_at TIMESTAMP 
                );
                """;
        String createPasswordVaultTable = """
                CREATE TABLE IF NOT EXISTS password (
                                id INTEGER PRIMARY KEY,
                                encrypted_password TEXT NOT NULL,
                                password_fingerprint TEXT UNIQUE,
                                created_at DATETIME 
                            );
                """;
        String createEmailTable = """
                CREATE TABLE IF NOT EXISTS email (
                                id INTEGER PRIMARY KEY,
                                email TEXT NOT NULL UNIQUE,
                                created_at DATETIME 
                            );
                """;
        String createAccountTable = """
                CREATE TABLE IF NOT EXISTS account (
                                id INTEGER PRIMARY KEY,
                                service_name TEXT NOT NULL UNIQUE,
                                email_id INTEGER,
                                username INTEGER,
                                password_id INTEGER,
                                created_at DATETIME,
                                updated_at DATETIME,
                                FOREIGN KEY (email_id) REFERENCES email(id) ON DELETE SET NULL,
                                FOREIGN KEY (password_id) REFERENCES password(id) ON DELETE SET NULL
                            );
                """;
        try(Statement statement = connection.createStatement()) {
            statement.execute(createMetadataTable);
            statement.execute(createPasswordVaultTable);
            statement.execute(createEmailTable);
            statement.execute(createAccountTable);

        } catch (SQLException e) {
            AppLogger.error("DatabaseInitializer exception : ",e);
        }
    }
}

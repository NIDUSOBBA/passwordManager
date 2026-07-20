package org.example.connection;

import org.example.utile.PathDeterminant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteConnection {

    public static Connection getConnection() {
        Connection connection = null;
        String bdUrl = "jdbc:sqlite:" + PathDeterminant.locationDisk() + "password_vault.db";
        try {
            connection = DriverManager.getConnection(bdUrl);
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (Exception e) {
            System.err.println("Connection exception: " + e.getMessage());
        }
        return connection;
    }

}

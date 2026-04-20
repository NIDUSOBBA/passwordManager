package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteConnection {

    public static Connection getConnection() {
        String url = "jdbc:sqlite:password_vault.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            try(Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (Exception e) {
            System.out.println("Connection exception: " + e.getMessage());
        }
        return connection;
    }

}

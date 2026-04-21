package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.example.utile.Const.*;

public class SQLiteConnection {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(BD_URL);
            try(Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (Exception e) {
            System.out.println("Connection exception: " + e.getMessage());
        }
        return connection;
    }

}

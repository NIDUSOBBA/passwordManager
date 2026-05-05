package org.example;

import org.example.connection.SQLiteConnection;
import org.example.controller.ManagerVault;
import org.example.dao.MetadataDao;
import org.example.exception.MissingEnvironmentVariableException;
import org.example.utile.DatabaseInitializer;

import java.sql.Connection;
import static org.example.utile.Const.*;

public class Main {
    public static void main(String[] args){
        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            String masterKey = System.getenv(MASTER_KEY);
            if (masterKey == null) {
                throw new MissingEnvironmentVariableException(MASTER_KEY);
            }
            MetadataDao metadataDao = new MetadataDao(connection);

            ManagerVault managerVault = ManagerVault.getManagerVault(masterKey, metadataDao, connection);
            managerVault.start();
        } catch (Exception e) {
            System.out.println("Manager initialize exception: " + e.getMessage());
        }
    }
}
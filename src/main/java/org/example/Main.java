package org.example;

import org.example.connection.SQLiteConnection;
import org.example.controller.ManagerVault;
import org.example.dao.MetadataDao;
import org.example.utile.DatabaseInitializer;

import java.sql.Connection;


public class Main {
    public static void main(String[] args){
        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            String masterKey = System.getenv("MASTER_KEY");
            if (masterKey == null) {
                masterKey = System.getProperty("master.key");
            }
            MetadataDao metadataDao = new MetadataDao(connection);

            ManagerVault managerVault = ManagerVault.getManagerVault(masterKey, metadataDao, connection);
            managerVault.start();
        } catch (Exception e) {
            System.out.println("Manager initialize exception: " + e.getMessage());
        }
    }
}
package org.example;

import org.example.connection.SQLiteConnection;
import org.example.controller.ManagerVault;
import org.example.dao.MetadataDao;
import org.example.utile.DatabaseInitializer;
import org.example.utile.MasterKeyIn;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        try(Connection connection = SQLiteConnection.getConnection()) {
            DatabaseInitializer.initializeDatabase(connection);
            MetadataDao metadataDao = new MetadataDao(connection);
            MasterKeyIn masterKeyIn = new MasterKeyIn();
            String key = masterKeyIn.loadMasterKey();
            if (key.isBlank()){
                masterKeyIn.masterKeyInitialization();
            }
            String masterKey = masterKeyIn.loadMasterKey();


            ManagerVault managerVault = ManagerVault.getManagerVault(masterKey, metadataDao, connection);
            managerVault.start();
        } catch (Exception e) {
            System.out.println("Manager initialize exception: " + e.getMessage());
        }

    }
}
package org.example.dao;

import org.example.utile.AppLogger;

import java.sql.*;

public class MetadataDao {

    private final Connection connection;

    public MetadataDao(Connection connection) {
        this.connection = connection;
    }

    public void create(byte[] bytes) {
        String createQuery = """
                INSERT INTO vault_metadata (id, salt) 
                VALUES (1, ?);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(createQuery)) {
            preparedStatement.setBytes(1, bytes);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error("Metadata create exception: ", e);
        }
    }

    public byte[] get() {
        String selectQuery = """
                SELECT salt 
                FROM vault_metadata;
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            return resultSet.getBytes("salt");
        } catch (SQLException e) {
            AppLogger.error("Metadata get exception: ", e);
        }
        return null;
    }
}

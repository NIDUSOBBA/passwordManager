package org.example.dao;

import org.example.dto.EmailDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailDao {
    private final Connection connection;

    public EmailDao(Connection connection) {
        this.connection = connection;
    }

    public void create(String email) {
        String insertQuery = """
                INSERT INTO email (email)
                VALUES (?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Email create exception: " + e.getMessage());
        }
    }

    public EmailDto getById(int id) {
        String selectQuery = """
                SELECT id, email
                FROM email
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return new EmailDto(
                    resultSet.getInt("id"),
                    resultSet.getString("email")
            );
        } catch (SQLException e) {
            System.out.println("Email getById exception: " + e.getMessage());
        }
        return null;
    }

    public List<EmailDto> getAll() {
        String selectQuery = """
                SELECT id, email 
                FROM email;
                """;
        List<EmailDto> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                resultList.add(new EmailDto(
                        resultSet.getInt("id"),
                        resultSet.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Email getAll exception: " + e.getMessage());
        }
        return resultList;
    }

    public void deleteById(int id) {
        String deleteQuery = """
                DELETE *
                FROM email
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Email deleteById exception: " + e.getMessage());
        }
    }
}

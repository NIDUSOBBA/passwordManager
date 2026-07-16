package org.example.dao;

import org.example.dto.EmailDto;
import org.example.utile.AppLogger;
import org.example.utile.TimestampUtil;

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
                INSERT INTO email (email,created_at)
                VALUES (?,?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, email);
            statement.setTimestamp(2, TimestampUtil.getCurrentTimestamp());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Email duplicated");
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
            throw new RuntimeException(e);
        }
    }

    public List<EmailDto> getAll() {
        String selectQuery = """
                SELECT id, email 
                FROM email
                GROUP BY id;
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
            throw new RuntimeException(e);
        }
        return resultList;
    }

    public void deleteById(int id) {
        String deleteQuery = """
                DELETE FROM email
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EmailDto getLast() {
        String selectQuery = """
                SELECT *
                FROM email
                WHERE id = (
                 SELECT MAX(id)
                 FROM email
                );
                """;
        try(Statement statement = connection.createStatement()) {
            ResultSet execute = statement.executeQuery(selectQuery);
            return new EmailDto(
                    execute.getInt("id"),
                    execute.getString("email")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

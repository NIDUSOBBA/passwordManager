package org.example.dao;

import org.example.dto.AccountCreateDto;
import org.example.dto.AccountResponseDto;
import org.example.dto.AccountUpdateDto;
import org.example.utile.AppLogger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private final Connection connection;

    public AccountDao(Connection connection) {
        this.connection = connection;
    }

    public void crete(AccountCreateDto account) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String insertQuery = """
                INSERT INTO account (service_name, email_id, username, password_id, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?,?);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, account.serviceName());
            preparedStatement.setInt(2, account.email());
            preparedStatement.setString(3, account.username());
            preparedStatement.setInt(4, account.encryptedPassword());
            preparedStatement.setObject(5, LocalDateTime.now());
            preparedStatement.setObject(6, LocalDateTime.now());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Dublicat account");
        }
    }

    public AccountResponseDto getById(int id) {
        String selectQuery = """
                SELECT id, service_name, email_id, username, password_id, created_at, updated_at
                FROM account 
                WHERE id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            int emailId = resultSet.getInt("email_id");
            int passwordId = resultSet.getInt("password_id");
            return new AccountResponseDto(
                    resultSet.getInt("id"),
                    resultSet.getString("service_name"),
                    emailId,
                    resultSet.getString("username"),
                    passwordId,
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at")
            );
        } catch (SQLException e) {
            System.err.println("Account getById exception: " + e.getMessage());
        }
        return null;
    }

    public List<AccountResponseDto> getAll() {
        String selectQuery = """
                SELECT id, service_name, email_id, username, password_id, created_at, updated_at
                FROM account
                GROUP BY id;
                """;
        List<AccountResponseDto> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int emailId = resultSet.getInt("email_id");
                int passwordId = resultSet.getInt("password_id");
                resultList.add(new AccountResponseDto(
                        resultSet.getInt("id"),
                        resultSet.getString("service_name"),
                        emailId,
                        resultSet.getString("username"),
                        passwordId,
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at")
                ));
            }
        } catch (Exception e) {
            System.err.println("Account getAll exception: " + e.getMessage());
        }
        return resultList;
    }

    public void update(AccountUpdateDto account) {
        String updateQuery = """
                UPDATE account
                SET service_name = ?,email_id = ?, username = ?, password_id = ?, updated_at = ?
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, account.serviceName());
            statement.setInt(2, account.emailId());
            statement.setString(3, account.username());
            statement.setInt(4, account.passwordId());
            statement.setObject(5, LocalDateTime.now());
            statement.setInt(6, account.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Account updateById exception: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String deleteQuery = """
                DELETE FROM account 
                WHERE id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.println("Account deleteById exception: " + e.getMessage());
        }
    }

    public AccountResponseDto getLast(){
        String getLast = """
                SELECT *
                FROM account
                WHERE id = (
                 SELECT MAX(id)
                 FROM account
                 );
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getLast);
            return new AccountResponseDto(
                    resultSet.getInt("id"),
                    resultSet.getString("service_name"),
                    resultSet.getInt("email_id"),
                    resultSet.getString("username"),
                    resultSet.getInt("password_id"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

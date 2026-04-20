package org.example.dao;


import org.example.dto.AccountCreateDto;
import org.example.dto.AccountResponseDto;
import org.example.dto.AccountUpdateDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private final Connection connection;

    public AccountDao(Connection connection) {
        this.connection = connection;
    }

    public void crete(AccountCreateDto account) {
        String insertQuery = """
                INSERT INTO account (service_name, email_id, username, password_id, updated_at)
                VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, account.serviceName());
            preparedStatement.setInt(2, account.email());
            preparedStatement.setString(3, account.username());
            preparedStatement.setInt(4, account.encryptedPassword());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Account create exception: " + e.getMessage());
        }
    }

    public AccountResponseDto getById(int id){
        String selectQuery = """
                SELECT id, service_name, email_id, username, password_id
                FROM account 
                WHERE id = ?;
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return new AccountResponseDto(
                    resultSet.getInt("id"),
                    resultSet.getString("service_name"),
                    resultSet.getInt("email_id"),
                    resultSet.getString("username"),
                    resultSet.getInt("password_id")
            );
        } catch (SQLException e) {
            System.out.println("Account getById exception: " + e.getMessage());
        }
        return null;
    }

    public List<AccountResponseDto> getAll() {
        String selectQuery = """
                SELECT id, service_name, email_id, username, password_id
                FROM account;
                """;
        List<AccountResponseDto> resultList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                resultList.add(new AccountResponseDto(
                        resultSet.getInt("id"),
                        resultSet.getString("service_name"),
                        resultSet.getInt("email_id"),
                        resultSet.getString("username"),
                        resultSet.getInt("password_id")
                ));
            }
        } catch (Exception e) {
            System.out.println("Account getAll exception: " + e.getMessage());
        }
        return resultList;
    }

    public void updateById(AccountUpdateDto account){
        String updateQuery = """
                UPDATE account
                SET service_name = ?, username = ?, password_id = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?;
                """;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1,account.serviceName());
            statement.setString(2,account.username());
            statement.setInt(3,account.passwordId());
            statement.setInt(4,account.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Account updateById exception: " + e.getMessage());
        }
    }

    public void deleteById(int id){
        String deleteQuery = """
                DELETE FROM account 
                WHERE id = ?;
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Account deleteById exception: " + e.getMessage());
        }
    }
}

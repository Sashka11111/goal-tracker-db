package com.liamtseva.persistence.repository.impl;

import com.liamtseva.persistence.entity.User;
import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.repository.contract.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class UserRepositoryImpl implements UserRepository {

  private DataSource dataSource;

  public UserRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addUser(User user) {
    String query = "INSERT INTO Users (username, password, profile_image) VALUES (?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, user.username());
      preparedStatement.setString(2, user.password());
      preparedStatement.setBytes(3, user.profileImage());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public User findByUsername(String username) throws EntityNotFoundException {
    String query = "SELECT * FROM Users WHERE username = ?";
    User user = null;

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

      preparedStatement.setString(1, username); // Встановлюємо значення параметра username
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          user = mapUser(resultSet); // Якщо є результати, встановлюємо значення user
        } else {
          throw new EntityNotFoundException("User with username " + username + " not found");
        }
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Error while fetching user by username: " + username, e);
    }

    return user;
  }

  @Override
  public boolean isUsernameExists(String username) {
    String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, username);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          int count = resultSet.getInt(1);
          return count > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private User mapUser(ResultSet resultSet) throws SQLException {
    return new User(
        resultSet.getInt("id_user"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getBytes("profile_image")
    );
  }
}

package com.liamtseva.persistence.dao;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
/*
  private final Connection connection;

  public UserDAO() {
    try {
       this.connection = DatabaseConnection.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to obtain database connection", e);
    }
  }
  // Додавання користувача
  public void addUser(User user) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users(username, password, profile_image) VALUES (?, ?, ?)");
      preparedStatement.setString(1, user.username());
      preparedStatement.setString(2, user.password());
      preparedStatement.setString(3, user.profileImage());
      preparedStatement.executeUpdate();
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public User getUserByLogin(String username) {
    User user = null;
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        // Створюємо об'єкт користувача на основі результату запиту
        user = new User(resultSet.getInt("id_user"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("profile_image"));
        preparedStatement.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }
  // Отримання списку користувачів
  public List<User> getAllUsers() {
    List<User> userList = new ArrayList<>();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");
      while (resultSet.next()) {
        User user = new User(resultSet.getInt("id_user"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("profile_image"));
        userList.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userList;
  }

  // Отримання користувача за ідентифікатором
  public User getUserById(int userId) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE id_user = ?");
      preparedStatement.setInt(1, userId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return new User(resultSet.getInt("id_user"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("profile_image"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Оновлення інформації про користувача
  public void updateUser(User user) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Users SET username = ?, password = ?, profile_image = ? WHERE id_user = ?");
      preparedStatement.setString(1, user.username());
      preparedStatement.setString(2, user.password());
      preparedStatement.setString(3, user.profileImage());
      preparedStatement.setInt(4, user.id());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Видалення користувача за ідентифікатором
  public void deleteUser(int userId) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id_user = ?");
      preparedStatement.setInt(1, userId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }*/
}
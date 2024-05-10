package com.liamtseva.domain.exception;

import com.liamtseva.persistence.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserValidator {
  // Мінімальна довжина паролю
  private static final int MIN_PASSWORD_LENGTH = 6;

  // Паттерн для перевірки складності паролю
  private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";

  // Перевірка унікальності імені користувача
  public static boolean isUsernameValid(String username) {
    return username != null && !username.isEmpty();
  }

  // Перевірка паролю
  public static boolean isPasswordValid(String password) {
    return password != null && password.length() >= MIN_PASSWORD_LENGTH && Pattern.matches(PASSWORD_PATTERN, password);
  }


  public static boolean isUsernameExists(String username) {
    Connection connection = DatabaseConnection.getConnection();
    if (connection != null) {
      String selectQuery = "SELECT * FROM Users WHERE username = ?";
      try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next(); // Повертає true, якщо ім'я користувача вже існує
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("Не вдалося підключитися до бази даних.");
    }
    return false;
  }
}

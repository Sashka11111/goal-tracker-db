package com.liamtseva.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {

  private static Connection connection;

  public static Connection getConnection() {
    try {
      // Завантаження драйвера SQLite
      Class.forName("org.sqlite.JDBC");

      // Підключення до бази даних SQLite
      String url = "jdbc:sqlite:Data/GoalTrackerDb";
      connection = DriverManager.getConnection(url);

    } catch (ClassNotFoundException | SQLException e) {
      System.err.println("Помилка підключення до бази даних SQLite: " + e.getMessage());
    }
    return connection;
  }
}

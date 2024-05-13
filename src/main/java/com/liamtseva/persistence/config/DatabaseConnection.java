package com.liamtseva.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

  private static final String JDBC_URL = "jdbc:sqlite:db/GoalTrackerDb.sqlite";
  private static DatabaseConnection instance;
  private static HikariDataSource dataSource;

  public DatabaseConnection() {
    // Приватний конструктор, щоб заборонити створення інстанцій ззовні
  }

  public static synchronized DatabaseConnection getInstance() {
    if (instance == null) {
      instance = new DatabaseConnection();
    }
    return instance;
  }

  public static synchronized Connection getConnection() throws SQLException {
    if (dataSource == null) {
      initializeDataSource(); // Ініціалізація джерела даних, якщо воно ще не було ініціалізоване
    }
    return dataSource.getConnection();
  }

  public static void initializeDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(JDBC_URL);
    dataSource = new HikariDataSource(config);
  }

  public DataSource getDataSource() {
    if (dataSource == null) {
      initializeDataSource(); // Ініціалізація джерела даних, якщо воно ще не було ініціалізоване
    }
    return dataSource;
  }

  public void closePool() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}

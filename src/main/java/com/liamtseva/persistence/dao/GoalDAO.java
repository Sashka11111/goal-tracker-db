package com.liamtseva.persistence.dao;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {
/*
  private final Connection connection;

  public GoalDAO() {
    try {
      this.connection = DatabaseConnection.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to obtain database connection", e);
    }
  }

  // Додавання цілі
  public void addGoal(Goal goal) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Goals(id_user, name_goal, description, id_category, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setInt(4, goal.categoryId());
      preparedStatement.setDate(5, java.sql.Date.valueOf(goal.startDate()));
      preparedStatement.setDate(6, java.sql.Date.valueOf(goal.endDate()));
      preparedStatement.setString(7, goal.status());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Отримання списку всіх цілей
  public List<Goal> getAllGoals() {
    List<Goal> goals = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Goals");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Goal goal = new Goal(resultSet.getInt("id_goal"), resultSet.getInt("id_user"),
            resultSet.getString("name_goal"), resultSet.getString("description"),
            resultSet.getInt("id_category"), resultSet.getDate("start_date").toLocalDate(),
            resultSet.getDate("end_date").toLocalDate(), resultSet.getString("status"));
        goals.add(goal);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }

  // Оновлення цілі
  public void updateGoal(Goal goal) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Goals SET name_goal = ?, description = ?, id_category = ?, start_date = ?, end_date = ?, status = ? WHERE id_goal = ?");
      preparedStatement.setString(1, goal.nameGoal());
      preparedStatement.setString(2, goal.description());
      preparedStatement.setInt(3, goal.categoryId());
      preparedStatement.setDate(4, java.sql.Date.valueOf(goal.startDate()));
      preparedStatement.setDate(5, java.sql.Date.valueOf(goal.endDate()));
      preparedStatement.setString(6, goal.status());
      preparedStatement.setInt(7, goal.id());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Видалення цілі за ідентифікатором
  public void deleteGoal(int goalId) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Goals WHERE id_goal = ?");
      preparedStatement.setInt(1, goalId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }*/
}

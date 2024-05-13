package com.liamtseva.persistence.dao;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StepDAO {
/*
  private final Connection connection;

  public StepDAO() {
    try {
      this.connection = DatabaseConnection.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to obtain database connection", e);
    }
  }

  // Додавання кроку
  public void addStep(Step step) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Steps(id_goal, description) VALUES (?, ?)");
      preparedStatement.setInt(1, step.goalId());
      preparedStatement.setString(2, step.description());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Отримання списку кроків за ідентифікатором цілі
  public List<Step> getStepsByGoalId(int goalId) {
    List<Step> steps = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Steps WHERE id_goal = ?");
      preparedStatement.setInt(1, goalId);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Step step = new Step(resultSet.getInt("id_step"), resultSet.getInt("id_goal"), resultSet.getString("description"));
        steps.add(step);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return steps;
  }

  // Оновлення кроку
  public void updateStep(Step step) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Steps SET description = ? WHERE id_step = ?");
      preparedStatement.setString(1, step.description());
      preparedStatement.setInt(2, step.id());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Видалення кроку за ідентифікатором
  public void deleteStep(int stepId) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Steps WHERE id_step = ?");
      preparedStatement.setInt(1, stepId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }*/
}

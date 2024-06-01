package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.repository.contract.StepRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StepRepositoryImpl implements StepRepository {
  private final DataSource dataSource;

  public StepRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public int addStep(Step step) throws EntityNotFoundException {
    String sql = "INSERT INTO Steps (id_goal, goal_name, description, status) VALUES (?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setInt(1, step.goalId());
      statement.setString(2, step.goalName());
      statement.setString(3, step.description());
      statement.setString(4, step.status());

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        throw new EntityNotFoundException("Creating step failed, no rows affected.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new EntityNotFoundException("Creating step failed, no ID obtained.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Step> getStepsByGoalId(int goalId) {
    List<Step> steps = new ArrayList<>();
    String query = "SELECT * FROM Steps WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Step step = extractStepFromResultSet(resultSet);
          steps.add(step);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return steps;
  }

  @Override
  public void updateStep(Step step) throws EntityNotFoundException {
    String query = "UPDATE Steps SET description = ?, status = ? WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, step.description());
      preparedStatement.setString(2, step.status());
      preparedStatement.setInt(3, step.id());
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows == 0) {
        throw new EntityNotFoundException("Step with id " + step.id() + " not found");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to update step.", e);
    }
  }

  @Override
  public void updateStepStatusByName(String description, String newStatus) throws EntityNotFoundException {
    String query = "UPDATE Steps SET status = ? WHERE description = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newStatus);
      statement.setString(2, description);
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Step " + description + " not found");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to update step status", e);
    }
  }

  @Override
  public List<Step> searchStepsByUserIdAndText(int userId, String searchText) {
    List<Step> steps = new ArrayList<>();
    String query = "SELECT * FROM Steps " +
        "JOIN Goals  ON Steps.id_goal = Goals.id_goal " +
        "WHERE Goals.id_user = ? AND Steps.description LIKE ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      preparedStatement.setString(2, "%" + searchText + "%");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Step step = extractStepFromResultSet(resultSet);
          steps.add(step);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return steps;
  }

  @Override
  public void deleteStep(int id) throws EntityNotFoundException {
    String query = "DELETE FROM Steps WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      int deletedRows = preparedStatement.executeUpdate();
      if (deletedRows == 0) {
        throw new EntityNotFoundException("Step with id " + id + " not found");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to delete step.", e);
    }
  }

  private Step extractStepFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_step");
    int goalId = resultSet.getInt("id_goal");
    String goalName = resultSet.getString("goal_name");
    String description = resultSet.getString("description");
    String status = resultSet.getString("status");
    return new Step(id, goalId, goalName, description, status);
  }
}

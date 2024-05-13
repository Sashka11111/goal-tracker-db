package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.repository.contract.GoalRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class GoalRepositoryImpl implements GoalRepository {
  private DataSource dataSource;

  public GoalRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addGoal(Goal goal) {
    String query = "INSERT INTO Goals (id_user, name_goal, description, id_category, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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

  @Override
  public Goal getGoalById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM Goals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return extractGoalFromResultSet(resultSet);
      } else {
        throw new EntityNotFoundException("Goal with id " + id + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching goal with id " + id);
    }
  }

  @Override
  public List<Goal> getAllGoals() {
    List<Goal> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals";
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)) {
      while (resultSet.next()) {
        Goal goal = extractGoalFromResultSet(resultSet);
        goals.add(goal);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }

  @Override
  public void updateGoal(Goal goal) throws EntityNotFoundException {
    String query = "UPDATE Goals SET id_user = ?, name_goal = ?, description = ?, id_category = ?, start_date = ?, end_date = ?, status = ? WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setInt(4, goal.categoryId());
      preparedStatement.setDate(5, java.sql.Date.valueOf(goal.startDate()));
      preparedStatement.setDate(6, java.sql.Date.valueOf(goal.endDate()));
      preparedStatement.setString(7, goal.status());
      preparedStatement.setInt(8, goal.id());
      int rowsUpdated = preparedStatement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Goal with id " + goal.id() + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while updating goal with id " + goal.id());
    }
  }

  @Override
  public void deleteGoal(int id) throws EntityNotFoundException {
    String query = "DELETE FROM Goals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      int rowsDeleted = preparedStatement.executeUpdate();
      if (rowsDeleted == 0) {
        throw new EntityNotFoundException("Goal with id " + id + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while deleting goal with id " + id);
    }
  }

  private Goal extractGoalFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_goal");
    int userId = resultSet.getInt("id_user");
    String nameGoal = resultSet.getString("name_goal");
    String description = resultSet.getString("description");
    int categoryId = resultSet.getInt("id_category");
    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
    LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
    String status = resultSet.getString("status");
    return new Goal(id, userId, nameGoal, description, categoryId, startDate, endDate, status);
  }
}

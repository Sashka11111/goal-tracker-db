package com.liamtseva.persistence.repository.impl;

import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GoalRepositoryImpl implements GoalRepository {
  private final Connection connection;

  public GoalRepositoryImpl(Connection connection) {
    this.connection = connection;
  }
    @Override
  public void addGoal(Goal goal) {
    String query = "INSERT INTO Goals (id_user,name_goal, description, id_category, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, goal.userId());
      statement.setString(2, goal.nameGoal());
      statement.setString(3, goal.description());
      statement.setInt(4, goal.categoryId());
      statement.setDate(5, java.sql.Date.valueOf(goal.startDate()));
      statement.setDate(6, java.sql.Date.valueOf(goal.endDate()));
      statement.setString(7, goal.status());
      statement.executeUpdate();
    } catch (SQLException e) {
      // Handle SQLException
      e.printStackTrace();
    }
  }

  @Override
  public Goal getGoalById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM Goals WHERE id_goal = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return extractGoalFromResultSet(resultSet);
      } else {
        throw new EntityNotFoundException("Goal with id " + id + " not found");
      }
    } catch (SQLException e) {
      // Handle SQLException
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching goal with id " + id);
    }
  }

  @Override
  public List<Goal> getAllGoals() {
    List<Goal> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        goals.add(extractGoalFromResultSet(resultSet));
      }
    } catch (SQLException e) {
      // Handle SQLException
      e.printStackTrace();
    }
    return goals;
  }

  @Override
  public void updateGoal(Goal goal) throws EntityNotFoundException {
    String query = "UPDATE Goals SET id_user = ?, name_goal = ?,description = ?, id_category = ?, start_date = ?, end_date = ?, status = ? WHERE id_goal = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, goal.userId());
      statement.setString(2, goal.nameGoal());
      statement.setString(3, goal.description());
      statement.setInt(4, goal.categoryId());
      statement.setDate(5, java.sql.Date.valueOf(goal.startDate()));
      statement.setDate(6, java.sql.Date.valueOf(goal.endDate()));
      statement.setString(7, goal.status());
      statement.setInt(8, goal.id());
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Goal with id " + goal.id() + " not found");
      }
    } catch (SQLException e) {
      // Handle SQLException
      e.printStackTrace();
      throw new EntityNotFoundException("Error while updating goal with id " + goal.id());
    }
  }

  @Override
  public void deleteGoal(int id) throws EntityNotFoundException {
    String query = "DELETE FROM Goals WHERE id_goal = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, id);
      int rowsDeleted = statement.executeUpdate();
      if (rowsDeleted == 0) {
        throw new EntityNotFoundException("Goal with id " + id + " not found");
      }
    } catch (SQLException e) {
      // Handle SQLException
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

package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.repository.contract.GoalRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalRepositoryImpl implements GoalRepository {
  private final DataSource dataSource;

  public GoalRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public int addGoal(Goal goal) {
    String query = "INSERT INTO Goals (id_user, name_goal, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setDate(4, Date.valueOf(goal.startDate()));
      preparedStatement.setDate(5, Date.valueOf(goal.endDate()));
      preparedStatement.setString(6, goal.status());
      preparedStatement.executeUpdate();

      // Отримання згенерованого ідентифікатора
      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getInt(1);
        } else {
          throw new SQLException("Creating goal failed, no ID obtained.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
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
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        goals.add(extractGoalFromResultSet(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }

  @Override
  public Goal getGoalByName(String name) throws EntityNotFoundException {
    String query = "SELECT * FROM Goals WHERE name_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, name);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return extractGoalFromResultSet(resultSet);
      } else {
        throw new EntityNotFoundException("Goal with name " + name + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching goal with name " + name);
    }
  }

  @Override
  public void updateGoalStatusByName(String goalName, String newStatus) throws EntityNotFoundException {
    String query = "UPDATE Goals SET status = ? WHERE name_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newStatus);
      statement.setString(2, goalName);
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Goal with name " + goalName + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Goal> filterGoalsByUserId(int userId) {
    List<Goal> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals WHERE id_user = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          goals.add(extractGoalFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }
  @Override
  public List<Goal> findGoalsByUserId(int userId) {
    List<Goal> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals WHERE id_user = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          goals.add(extractGoalFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }

  @Override
  public List<Goal> getAllGoalsByUserId(int userId) {
    List<Goal> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals WHERE id_user = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          goals.add(extractGoalFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }

  @Override
  public void updateGoal(Goal goal) throws EntityNotFoundException {
    String query = "UPDATE Goals SET id_user = ?, name_goal = ?, description = ?, start_date = ?, end_date = ?, status = ? WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setDate(4, Date.valueOf(goal.startDate()));
      preparedStatement.setDate(5, Date.valueOf(goal.endDate()));
      preparedStatement.setString(6, goal.status());
      preparedStatement.setInt(7, goal.id());
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
  public void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException {
    String query = "UPDATE Goals SET status = ? WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newStatus);
      statement.setInt(2, goalId);
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Goal with id " + goalId + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
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

  @Override
  public void addCategoryToGoal(int goalId, int categoryId) {
    String query = "INSERT INTO CategoryGoals (id_goal, id_category) VALUES (?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      preparedStatement.setInt(2, categoryId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeCategoryFromGoal(int goalId, int categoryId) {
    String query = "DELETE FROM CategoryGoals WHERE id_goal = ? AND id_category = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      preparedStatement.setInt(2, categoryId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Integer> getCategoriesByGoalId(int goalId) {
    List<Integer> categoryIds = new ArrayList<>();
    String query = "SELECT id_category FROM CategoryGoals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          categoryIds.add(resultSet.getInt("id_category"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categoryIds;
  }

  @Override
  public List<Integer> getGoalsByCategoryId(int categoryId) {
    List<Integer> goalIds = new ArrayList<>();
    String query = "SELECT id_goal FROM CategoryGoals WHERE id_category = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, categoryId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          goalIds.add(resultSet.getInt("id_goal"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goalIds;
  }

  private Goal extractGoalFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_goal");
    int userId = resultSet.getInt("id_user");
    String nameGoal = resultSet.getString("name_goal");
    String description = resultSet.getString("description");
    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
    LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
    String status = resultSet.getString("status");
    return new Goal(id, userId, nameGoal, description, startDate, endDate, status);
  }
}

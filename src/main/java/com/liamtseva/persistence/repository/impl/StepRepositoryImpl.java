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
  public void addStep(Step step) {
    String query = "INSERT INTO Steps (id_step, id_goal, step_description) VALUES (?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, step.id());
      preparedStatement.setInt(2, step.goalId());
      preparedStatement.setString(3, step.description());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
  }

  @Override
  public Step getStepById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM Steps WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return extractStepsFromResultSet(resultSet);
        } else {
          throw new EntityNotFoundException("Step with id " + id + " not found");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching step with id " + id);
    }
  }

  @Override
  public List<Step> getAllSteps() {
    List<Step> steps = new ArrayList<>();
    String query = "SELECT * FROM Steps";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        Step step = extractStepsFromResultSet(resultSet);
        steps.add(step);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
    return steps;
  }

  @Override
  public void updateStep(Step step) throws EntityNotFoundException {
    String query = "UPDATE Steps SET step_description = ? WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, step.description());
      preparedStatement.setInt(2, step.id());
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows == 0) {
        throw new EntityNotFoundException("Step with id " + step.id() + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
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
      e.printStackTrace();
      // Handle exception
    }
  }

  private Step extractStepsFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_step");
    int goalId = resultSet.getInt("id_goal");
    String description = resultSet.getString("step_description");
    return new Step(id, goalId, description);
  }
}

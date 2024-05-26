package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.repository.contract.CategoryRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

  private DataSource dataSource;

  public CategoryRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addCategory(Category category) throws EntityNotFoundException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Category(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, category.name());
      preparedStatement.executeUpdate();
      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        category.id();
      } else {
        throw new SQLException("Adding category failed, no ID obtained.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to add category.", e);
    }
  }

  @Override
  public void updateCategory(Category category) throws EntityNotFoundException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Category SET name = ? WHERE id_category = ?");
      preparedStatement.setString(1, category.name());
      preparedStatement.setInt(2, category.id());
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 0) {
        throw new EntityNotFoundException("Failed to update category, category not found.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to update category.", e);
    }
  }

  @Override
  public void deleteCategory(int categoryId) throws EntityNotFoundException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Category WHERE id_category = ?");
      preparedStatement.setInt(1, categoryId);
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 0) {
        throw new EntityNotFoundException("Failed to delete category, category not found.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to delete category.", e);
    }
  }

  @Override
  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Category");
      while (resultSet.next()) {
        Category category = new Category(resultSet.getInt("id_category"), resultSet.getString("name"));
        categories.add(category);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get all categories.", e);
    }
    return categories;
  }

  @Override
  public Category getCategoryById(int categoryId) throws EntityNotFoundException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Category WHERE id_category = ?");
      preparedStatement.setInt(1, categoryId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return new Category(resultSet.getInt("id_category"), resultSet.getString("name"));
      } else {
        throw new EntityNotFoundException("Category not found.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to get category by id.", e);
    }
  }

  // Методи для роботи зі зв'язком багато-до-багатьох

  public void addCategoryToGoal(int categoryId, int goalId) {
    String query = "INSERT INTO CategoryGoals (id_category, id_goal) VALUES (?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, categoryId);
      preparedStatement.setInt(2, goalId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to add category to goal.", e);
    }
  }

  public void removeCategoryFromGoal(int categoryId, int goalId) throws EntityNotFoundException {
    String query = "DELETE FROM CategoryGoals WHERE id_category = ? AND id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, categoryId);
      preparedStatement.setInt(2, goalId);
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows == 0) {
        throw new EntityNotFoundException("Category or Goal not found.");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to remove category from goal.", e);
    }
  }

  public List<Integer> getCategoriesByGoalId(int goalId) {
    List<Integer> categories = new ArrayList<>();
    String query = "SELECT id_category FROM CategoryGoals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        categories.add(resultSet.getInt("id_category"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get categories by goal id.", e);
    }
    return categories;
  }

  public List<Integer> getGoalsByCategoryId(int categoryId) {
    List<Integer> goals = new ArrayList<>();
    String query = "SELECT id_goal FROM CategoryGoals WHERE id_category = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, categoryId);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        goals.add(resultSet.getInt("id_goal"));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get goals by category id.", e);
    }
    return goals;
  }
}

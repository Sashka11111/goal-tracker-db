package com.liamtseva.persistence.repository.impl;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.exception.EntityNotFoundException;
import com.liamtseva.persistence.repository.contract.CategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

  private final Connection connection;

  public CategoryRepositoryImpl() {
    this.connection = DatabaseConnection.getConnection();
  }

  @Override
  public void addCategory(Category category) throws EntityNotFoundException {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Category(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, category.name());
      preparedStatement.executeUpdate();
      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        category.id();
      } else {
        throw new SQLException("Adding category failed, no ID obtained.");
      }
      preparedStatement.close();
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to add category.", e);
    }
  }

  @Override
  public void updateCategory(Category category) throws EntityNotFoundException {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Category SET name = ? WHERE id_category = ?");
      preparedStatement.setString(1, category.name());
      preparedStatement.setInt(2, category.id());
      int affectedRows = preparedStatement.executeUpdate();
      preparedStatement.close();
      if (affectedRows == 0) {
        throw new EntityNotFoundException("Failed to update category, category not found.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to update category.", e);
    }
  }

  @Override
  public void deleteCategory(int categoryId) throws EntityNotFoundException {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Category WHERE id_category = ?");
      preparedStatement.setInt(1, categoryId);
      int affectedRows = preparedStatement.executeUpdate();
      preparedStatement.close();
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
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Category");
      while (resultSet.next()) {
        Category category = new Category(resultSet.getInt("id_category"), resultSet.getString("name"));
        categories.add(category);
      }
      statement.close();
    } catch (SQLException e) {
      try {
        throw new EntityNotFoundException("Failed to get all categories.", e);
      } catch (EntityNotFoundException ex) {
        throw new RuntimeException(ex);
      }
    }
    return categories;
  }

  @Override
  public Category getCategoryById(int categoryId) throws EntityNotFoundException {
    try {
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
}

package com.liamtseva.persistence.dao;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
/*
  public void addCategory(Category category) {
    String sql = "INSERT INTO Category(name) VALUES (?)";
    try (Connection connection = DatabaseConnection.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, category.name());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    String sql = "SELECT * FROM Category";
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        Category category = new Category(resultSet.getInt("id_category"), resultSet.getString("name"));
        categories.add(category);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categories;
  }

  public void updateCategory(Category category) {
    String sql = "UPDATE Category SET name = ? WHERE id_category = ?";
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, category.name());
      preparedStatement.setInt(2, category.id());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteCategory(int categoryId) {
    String sql = "DELETE FROM Category WHERE id_category = ?";
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, categoryId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
*/
}

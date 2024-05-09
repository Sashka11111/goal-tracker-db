package com.liamtseva.goals;

import com.liamtseva.goals.dao.GoalDAO;
import com.liamtseva.goals.entity.Goal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MyGoalsController {

  @FXML
  private TableColumn<?, ?> MyGoals_col_Category;

  @FXML
  private TableColumn<?, ?> MyGoals_col_Description;

  @FXML
  private TableColumn<?, ?> MyGoals_col_Goal;

  @FXML
  private TableColumn<?, ?> MyGoals_col_endDate;

  @FXML
  private TableColumn<?, ?> MyGoals_col_startDate;

  @FXML
  private TableColumn<?, ?> MyGoals_col_status;

  @FXML
  private TableView<?> MyGoals_tableView;
  @FXML
  private TextField description;

  @FXML
  private TextField goal;

  @FXML
  private Button btn_add;

  @FXML
  private Button btn_clear;

  @FXML
  private Button btn_delete;

  @FXML
  private Button btn_update;

  @FXML
  private ComboBox<String> category;

  @FXML
  private DatePicker endDate;

  @FXML
  private DatePicker startDate;

  @FXML
  void initialize() {
    btn_add.setOnAction(event -> addGoal());
    btn_clear.setOnAction(event -> clearFields());
    btn_delete.setOnAction(event -> deleteGoal());
    btn_update.setOnAction(event -> updateGoal());
    fillCategoryComboBox();
  }
  private void fillCategoryComboBox() {
    try (Connection connection = DatabaseHandler.connect()) {
      String sql = "SELECT name FROM Category";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        try (ResultSet resultSet = statement.executeQuery()) {
          ObservableList<String> categories = FXCollections.observableArrayList();
          while (resultSet.next()) {
            String name = resultSet.getString("name");
            categories.add(name);
          }
          category.setItems(categories);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @FXML
  void addGoal() {
    // Отримання даних з полів введення
    String goalDescription = description.getText();
    String goalTitle = goal.getText();
    String selectedCategory = category.getValue();
    LocalDate startDateValue = startDate.getValue();
    LocalDate endDateValue = endDate.getValue();

    // Перевірка на те, що всі поля заповнені
    if (goalDescription.isEmpty() || goalTitle.isEmpty() || selectedCategory == null || startDateValue == null || endDateValue == null) {
      // Виведення повідомлення про помилку, якщо не всі поля заповнені
      // Наприклад, ви можете використати Alert або показати повідомлення на UI
      System.out.println("Будь ласка, заповніть всі поля.");
      return;
    }

    // Створення об'єкта цілі з отриманими даними
    Goal goal = new Goal(0, 1, goalTitle, goalDescription, 1, startDateValue, endDateValue, "active");
    goal.setIdUser(1); // Задайте id користувача, якщо він залогований
    // Встановлення айді категорії за назвою категорії, можливо, за допомогою методу, який знаходить індекс категорії в ComboBox
    goal.setIdCategory(1);

    // Спроба додати ціль до бази даних
    try {
      Connection connection = DatabaseHandler.connect();
      GoalDAO goalDAO = new GoalDAO(connection);
      goalDAO.addGoal(goal);
      System.out.println("Ціль успішно додана до бази даних!");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Помилка під час додавання цілі до бази даних.");
    }
  }
  // Метод для отримання айді категорії за її назвою





  @FXML
  void clearFields() {
    // Очистити значення полів вводу
    description.clear();
    goal.clear();
    category.getSelectionModel().clearSelection();
    startDate.setValue(null);
    endDate.setValue(null);
  }

  @FXML
  void deleteGoal() {
    // Отримати ідентифікатор цілі, яку потрібно видалити
    // int goalId = ...;

    // Викликати метод для видалення цілі з бази даних або списку цілей
    // goalDAO.deleteGoal(goalId);
  }

  @FXML
  void updateGoal() {
    // Отримати дані з полів вводу та оновити ціль
    // String updatedDescription = description.getText();
    // String updatedTitle = goal.getText();
    // Object updatedCategory = category.getValue();
    // LocalDate updatedStartDate = startDate.getValue();
    // LocalDate updatedEndDate = endDate.getValue();

    // Отримати ідентифікатор цілі, яку потрібно оновити
    // int goalId = ...;

    // Викликати метод для оновлення цілі в базі даних або списку цілей
    // goalDAO.updateGoal(new Goal(goalId, ...));
  }
}

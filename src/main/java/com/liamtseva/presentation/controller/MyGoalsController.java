package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.presentation.viewmodel.GoalViewModel;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.List;

public class MyGoalsController {

  @FXML
  private TableColumn<GoalViewModel, String> MyGoals_col_Description;

  @FXML
  private TableColumn<GoalViewModel, String> MyGoals_col_Goal;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> MyGoals_col_endDate;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> MyGoals_col_startDate;

  @FXML
  private TableColumn<GoalViewModel, String> MyGoals_col_status;

  @FXML
  private TableView<GoalViewModel> MyGoals_tableView;

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
  private ComboBox<Category> category;

  @FXML
  private DatePicker endDate;

  @FXML
  private DatePicker startDate;

  private final GoalRepository goalRepository;
  private final CategoryRepository categoryRepository;

  public MyGoalsController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення GoalRepositoryImpl з DatabaseConnection
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення CategoryRepositoryImpl з DatabaseConnection
  }

  @FXML
  void initialize() {
    loadGoals();
    // Ініціалізація ComboBox категорій
    ObservableList<Category> categories = FXCollections.observableArrayList();
    categories.addAll(categoryRepository.getAllCategories());
    category.setItems(categories);

    MyGoals_col_Goal.setCellValueFactory(cellData -> cellData.getValue().nameGoalProperty());
    MyGoals_col_Description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    MyGoals_col_startDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    MyGoals_col_endDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
    MyGoals_col_status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    // Загрузка цілей з бази даних та відображення їх у таблиці
    loadGoals();

    // Обробники подій для кнопок
    btn_add.setOnAction(event -> onAddClicked());
    btn_clear.setOnAction(event -> onClearClicked());
    btn_delete.setOnAction(event -> onDeleteClicked());
  }

  // Завантаження цілей з бази даних
  private void loadGoals() {
    List<Goal> goals = goalRepository.getAllGoals();
    ObservableList<GoalViewModel> goalViewModels = FXCollections.observableArrayList();
    for (Goal goal : goals) {
      goalViewModels.add(new GoalViewModel(goal));
    }
    MyGoals_tableView.setItems(goalViewModels);
  }
  // Логіка додавання нової цілі
  private void onAddClicked() {
    String goalName = goal.getText();
    String goalDescription = description.getText();
    Category selectedCategory = category.getValue();
    LocalDate goalStartDate = startDate.getValue();
    LocalDate goalEndDate = endDate.getValue();

    if (goalName != null && !goalName.isEmpty() && selectedCategory != null && goalStartDate != null && goalEndDate != null) {
      // Передавати userId в конструктор Goal можна отримавши його з поточного користувача або якщо це поки що не реалізовано, то вказати 0
      Goal newGoal = new Goal(0, 0, goalName, goalDescription, selectedCategory.id(), goalStartDate, goalEndDate, "Активна");
      goalRepository.addGoal(newGoal);
      loadGoals();
      clearFields();
    } else {
      // Вивести повідомлення про помилку, якщо не всі поля заповнені
    }
  }


  // Логіка очищення полів вводу
  private void onClearClicked() {
    clearFields();
  }

  // Логіка видалення обраної цілі
  private void onDeleteClicked() {
    GoalViewModel selectedGoal = MyGoals_tableView.getSelectionModel().getSelectedItem();
    if (selectedGoal != null) {
      try {
        goalRepository.deleteGoal(selectedGoal.getIdGoal());
        loadGoals();
        clearFields();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
        // Обробити виняток відповідним чином
      }
    }
  }

  // Очищення полів вводу
  private void clearFields() {
    goal.clear();
    description.clear();
    category.getSelectionModel().clearSelection();
    startDate.setValue(null);
    endDate.setValue(null);
  }
}

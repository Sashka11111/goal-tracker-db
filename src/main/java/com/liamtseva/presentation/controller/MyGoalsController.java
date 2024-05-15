package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
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
  private final UserRepository userRepository;

  public MyGoalsController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення GoalRepositoryImpl з DatabaseConnection
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення CategoryRepositoryImpl з DatabaseConnection
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
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
  @FXML
  private void onAddClicked() {
    String goalName = goal.getText();
    String goalDescription = description.getText();
    Category selectedCategory = category.getValue();
    LocalDate goalStartDate = startDate.getValue();
    LocalDate goalEndDate = endDate.getValue();

    if (goalName != null && !goalName.isEmpty() && selectedCategory != null && goalStartDate != null && goalEndDate != null) {
      // Отримати поточного користувача за його ім'ям
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      if (currentUser != null) {
        // Створити новий об'єкт цілі з отриманими значеннями та ідентифікатором користувача
        Goal newGoal = new Goal(0, currentUser.id(), goalName, goalDescription, selectedCategory.id(), goalStartDate, goalEndDate, "Активна");
        // Додати нову ціль у репозиторій цілей
        goalRepository.addGoal(newGoal);
        // Оновити таблицю цілей у графічному інтерфейсі
        loadGoals();
        // Очистити поля введення після додавання цілі
        clearFields();
      } else {
        // Обробити випадок, коли користувач не знайдений
      }
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
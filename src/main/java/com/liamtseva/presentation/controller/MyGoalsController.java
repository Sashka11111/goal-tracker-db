package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.AuthenticatedUser;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.List;
import org.controlsfx.control.CheckComboBox;

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
  private CheckComboBox<Category> categories;

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
    // Ініціалізація CheckComboBox категорій
    ObservableList<Category> categoryList = FXCollections.observableArrayList();
    categoryList.addAll(categoryRepository.getAllCategories());
    categories.getItems().addAll(categoryList);

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
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> goals = goalRepository.getAllGoalsByUserId(currentUser.id());
      ObservableList<GoalViewModel> goalViewModels = FXCollections.observableArrayList();
      for (Goal goal : goals) {
        goalViewModels.add(new GoalViewModel(goal));
      }
      MyGoals_tableView.setItems(goalViewModels);
    }
  }

  // Логіка додавання нової цілі
  @FXML
  private void onAddClicked() {
    String goalName = goal.getText();
    String goalDescription = description.getText();
    List<Category> selectedCategories = categories.getCheckModel().getCheckedItems();
    LocalDate goalStartDate = startDate.getValue();
    LocalDate goalEndDate = endDate.getValue();

    if (goalName != null && !goalName.isEmpty() && !selectedCategories.isEmpty() && goalStartDate != null && goalEndDate != null) {
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      if (currentUser != null) {
        Goal newGoal = new Goal(0, currentUser.id(), goalName, goalDescription, goalStartDate, goalEndDate, "Активна");
        int newGoalId = goalRepository.addGoal(newGoal);

        if (newGoalId != -1) {
          for (Category category : selectedCategories) {
            goalRepository.addCategoryToGoal(newGoalId, category.id());
          }
          loadGoals();
          clearFields();
        } else {
          // Обробити помилку створення нової цілі
        }
      }
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
    categories.getCheckModel().clearChecks();
    startDate.setValue(null);
    endDate.setValue(null);
  }
}

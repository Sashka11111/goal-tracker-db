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
import com.liamtseva.domain.validation.GoalValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
  private Button btn_edit;

  @FXML
  private CheckComboBox<Category> categories;

  @FXML
  private Label errorMessage;

  @FXML
  private DatePicker endDate;

  @FXML
  private DatePicker startDate;

  @FXML
  private TextField searchField;

  private final GoalRepository goalRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  private ObservableList<GoalViewModel> goalViewModels;

  public MyGoalsController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource());
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  void initialize() {
    loadGoals();
    ObservableList<Category> categoryList = FXCollections.observableArrayList();
    categoryList.addAll(categoryRepository.getAllCategories());
    categories.getItems().addAll(categoryList);

    MyGoals_col_Goal.setCellValueFactory(cellData -> cellData.getValue().nameGoalProperty());
    MyGoals_col_Description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    MyGoals_col_startDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    MyGoals_col_endDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
    MyGoals_col_status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    loadGoals();

    btn_add.setOnAction(event -> onAddClicked());
    btn_clear.setOnAction(event -> onClearClicked());
    btn_delete.setOnAction(event -> onDeleteClicked());
    btn_edit.setOnAction(event -> onEditClicked());

    searchField.textProperty().addListener((observable, oldValue, newValue) -> filterGoals(newValue));

    MyGoals_tableView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> onGoalSelected(newValue));
  }

  private void loadGoals() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> goals = goalRepository.getAllGoalsByUserId(currentUser.id());
      goalViewModels = FXCollections.observableArrayList();
      for (Goal goal : goals) {
        goalViewModels.add(new GoalViewModel(goal));
      }
      MyGoals_tableView.setItems(goalViewModels);
    }
  }

  private void filterGoals(String searchText) {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> goals = goalRepository.searchGoalsByUserIdAndText(currentUser.id(), searchText);
      ObservableList<GoalViewModel> filteredList = FXCollections.observableArrayList();
      for (Goal goal : goals) {
        filteredList.add(new GoalViewModel(goal));
      }
      MyGoals_tableView.setItems(filteredList);

      if (filteredList.isEmpty()) {
        MyGoals_tableView.setPlaceholder(new Label("На жаль у Вас немає такої цілі"));
      } else {
        MyGoals_tableView.setPlaceholder(null);
      }
    }
  }

  private void onGoalSelected(GoalViewModel selectedGoal) {
    if (selectedGoal != null) {
      goal.setText(selectedGoal.getNameGoal());
      description.setText(selectedGoal.getDescription());
      startDate.setValue(selectedGoal.getStartDate());
      endDate.setValue(selectedGoal.getEndDate());
      categories.getCheckModel().clearChecks();
    }
  }

  @FXML
  private void onAddClicked() {
    clearErrorMessage();

    String goalName = goal.getText();
    String goalDescription = description.getText();
    List<Category> selectedCategories = categories.getCheckModel().getCheckedItems();
    LocalDate goalStartDate = startDate.getValue();
    LocalDate goalEndDate = endDate.getValue();

    String validationError = GoalValidator.validate(goalName, goalDescription, selectedCategories, goalStartDate, goalEndDate);
    if (validationError != null) {
      showErrorMessage(validationError);
      return;
    }

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
        showErrorMessage("Помилка при створенні нової цілі");
      }
    }
  }

  private void onClearClicked() {
    clearFields();
    clearErrorMessage();
  }

  private void onDeleteClicked() {
    GoalViewModel selectedGoal = MyGoals_tableView.getSelectionModel().getSelectedItem();
    if (selectedGoal != null) {
      try {
        // Видаляємо всі зв'язки з категоріями
        List<Integer> categoryIds = goalRepository.getCategoriesByGoalId(selectedGoal.getIdGoal());
        for (int categoryId : categoryIds) {
          goalRepository.removeCategoryFromGoal(selectedGoal.getIdGoal(), categoryId);
        }

        // Видаляємо саму ціль
        goalRepository.deleteGoal(selectedGoal.getIdGoal());
        loadGoals();
        clearFields();
        clearErrorMessage();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
        showErrorMessage("Помилка при видаленні цілі");
      }
    }
  }


  private void onEditClicked() {
    clearErrorMessage();

    GoalViewModel selectedGoal = MyGoals_tableView.getSelectionModel().getSelectedItem();
    if (selectedGoal != null) {
      String goalName = goal.getText();
      String goalDescription = description.getText();
      List<Category> selectedCategories = categories.getCheckModel().getCheckedItems();
      LocalDate goalStartDate = startDate.getValue();
      LocalDate goalEndDate = endDate.getValue();

      String validationError = GoalValidator.validate(goalName, goalDescription, selectedCategories, goalStartDate, goalEndDate);
      if (validationError != null) {
        showErrorMessage(validationError);
        return;
      }

      Goal updatedGoal = new Goal(selectedGoal.getIdGoal(), selectedGoal.getUserId(), goalName, goalDescription, goalStartDate, goalEndDate, selectedGoal.getStatus());

      try {
        goalRepository.updateGoal(updatedGoal);
        // Update categories as well
        goalRepository.clearCategoriesFromGoal(updatedGoal.id());
        for (Category category : selectedCategories) {
          goalRepository.addCategoryToGoal(updatedGoal.id(), category.id());
        }
        loadGoals();
        clearFields();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
        showErrorMessage("Помилка при редагуванні цілі");
      }
    }
  }

  private void clearFields() {
    goal.clear();
    description.clear();
    categories.getCheckModel().clearChecks();
    startDate.setValue(null);
    endDate.setValue(null);
    searchField.clear();
  }

  private void clearErrorMessage() {
    errorMessage.setText("");
  }

  private void showErrorMessage(String message) {
    errorMessage.setText(message);
  }
}

package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.contract.StepRepository;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.persistence.repository.impl.StepRepositoryImpl;
import com.liamtseva.presentation.viewmodel.StepViewModel;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StepsToGoalController {

  @FXML
  private TableView<StepViewModel> Steps_tableView;

  @FXML
  private TableColumn<StepViewModel, String> Steps_col_NameGoal;

  @FXML
  private TableColumn<StepViewModel, String> Steps_col_description;

  @FXML
  private Button btn_add;

  @FXML
  private Button btn_clear;

  @FXML
  private Button btn_delete;
  @FXML
  private Button btn_edit;

  @FXML
  private TextField description;

  @FXML
  private ComboBox<Goal> goal;

  private final StepRepository stepRepository;
  private final GoalRepository goalRepository;

  public StepsToGoalController() {
    this.stepRepository = new StepRepositoryImpl(new DatabaseConnection().getDataSource());
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  void initialize() {
    Steps_col_NameGoal.setCellValueFactory(cellData -> cellData.getValue().goalNameProperty());
    Steps_col_description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    loadSteps();
    Steps_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        description.setText(newSelection.getDescription()); // Встановлюємо опис кроку у поле вводу
      }
    });
    // Обробники подій для кнопок
    btn_add.setOnAction(event -> onAddClicked());
    btn_clear.setOnAction(event -> clearField());
    btn_delete.setOnAction(event -> onDeleteClicked());
    btn_edit.setOnAction(event -> onEditClicked());
  }

  // Завантаження кроків з бази даних
  private void loadSteps() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> userGoals = goalRepository.filterGoalsByUserId(currentUser.id());
      ObservableList<Goal> observableUserGoals = FXCollections.observableArrayList(userGoals);

      for (Goal goal : userGoals) {
        List<Step> stepsForGoal = stepRepository.getStepsByGoalId(goal.id());
        for (Step step : stepsForGoal) {
          Steps_tableView.getItems().add(new StepViewModel(step));
        }
      }

      goal.setItems(observableUserGoals);
      goal.setCellFactory(param -> new ListCell<>() {
        @Override
        protected void updateItem(Goal item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null || item.nameGoal() == null) {
            setText(null);
          } else {
            setText(item.nameGoal());
          }
        }
      });
    }
  }

  private void onAddClicked() {
    Goal selectedGoal = goal.getValue();
    String stepDescription = description.getText();
    if (selectedGoal != null && !stepDescription.isEmpty()) {
      Step newStep = new Step(0, selectedGoal.id(), selectedGoal.nameGoal(), stepDescription, "Активний"); // Set status to "Активний"
      try {
        int generatedId = stepRepository.addStep(newStep);

        // Create a new Step record with the generated ID
        Step stepWithId = new Step(generatedId, newStep.goalId(), newStep.goalName(), newStep.description(), newStep.status());

        // Add the new Step with the correct ID to the TableView
        Steps_tableView.getItems().add(new StepViewModel(stepWithId));
        clearField();
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
  private void onEditClicked() {
    StepViewModel selectedStep = Steps_tableView.getSelectionModel().getSelectedItem();
    if (selectedStep != null) {
      String newDescription = description.getText();

      if (!newDescription.isEmpty()) {
        selectedStep.setDescription(newDescription);

        try {
          // Оновлюємо крок у базі даних
          stepRepository.updateStep(selectedStep.getStep());
          // Оновлюємо дані у TableView
          Steps_tableView.refresh();
          clearField();
        } catch (EntityNotFoundException e) {
          e.printStackTrace(); // Обробка помилки
        }
      }
    }
  }
  private void onDeleteClicked() {
    StepViewModel selectedStep = Steps_tableView.getSelectionModel().getSelectedItem();
    if (selectedStep != null) {
      try {
        // Видаляємо крок з бази даних
        stepRepository.deleteStep(selectedStep.getIdStep());
        // Видаляємо крок з таблиці
        Steps_tableView.getItems().remove(selectedStep);
        clearField();
      } catch (EntityNotFoundException e) {
        e.printStackTrace(); // Обробка помилки
      }
    }
  }

  private void clearField() {
    description.clear();
    goal.setValue(null);
  }
}

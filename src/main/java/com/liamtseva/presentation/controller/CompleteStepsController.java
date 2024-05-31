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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;

import java.util.List;

public class CompleteStepsController {

  @FXML
  private TableView<StepViewModel> complateSteps_tableView;

  @FXML
  private TableColumn<StepViewModel, String> complateGoal_col_GoalName;

  @FXML
  private TableColumn<StepViewModel, String> complateGoal_col_Step;

  @FXML
  private TableColumn<StepViewModel, String> complateGoal_col_Status;

  @FXML
  private TextField stepDescriptionTextField;

  @FXML
  private ComboBox<String> stepStatusComboBox;

  @FXML
  private Button updateStatusButton;

  private final StepRepository stepRepository;
  private final GoalRepository goalRepository;

  // Конструктор класу CompleteStepsController
  public CompleteStepsController() {
    this.stepRepository = new StepRepositoryImpl(new DatabaseConnection().getDataSource());
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  // Метод для ініціалізації контролера
  @FXML
  public void initialize() {
    // Встановлення фабрик та обробників подій для стовпців таблиці
    complateGoal_col_GoalName.setCellValueFactory(cellData -> cellData.getValue().goalNameProperty());
    complateGoal_col_Step.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    complateGoal_col_Status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    complateGoal_col_Status.setCellFactory(ComboBoxTableCell.forTableColumn("Активний", "Завершений"));
    complateGoal_col_Status.setOnEditCommit(event -> {
      StepViewModel stepViewModel = event.getRowValue();
      String newStatus = event.getNewValue();
      stepViewModel.setStatus(newStatus);
      updateStepStatus(stepViewModel.getDescription(), newStatus);
    });

    complateSteps_tableView.setEditable(true);

    complateSteps_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        stepDescriptionTextField.setText(newSelection.getDescription());
        stepStatusComboBox.setValue(newSelection.getStatus());
      }
    });

    // Завантаження кроків
    loadSteps();

    // Додавання значень в ComboBox статусу кроку
    stepStatusComboBox.getItems().addAll("Активний", "Завершений");

    // Обробник події для кнопки оновлення статусу кроку
    updateStatusButton.setOnAction(event -> {
      StepViewModel selectedStep = complateSteps_tableView.getSelectionModel().getSelectedItem();
      if (selectedStep != null) {
        String newStatus = stepStatusComboBox.getValue();
        selectedStep.setStatus(newStatus);
        updateStepStatus(selectedStep.getDescription(), newStatus);
        complateSteps_tableView.refresh();
      }
    });
  }

  // Метод для завантаження списку кроків
  private void loadSteps() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> goals = goalRepository.filterGoalsByUserId(currentUser.id());
      ObservableList<StepViewModel> stepViewModels = FXCollections.observableArrayList();

      for (Goal goal : goals) {
        List<Step> steps = stepRepository.getStepsByGoalId(goal.id());
        for (Step step : steps) {
          stepViewModels.add(new StepViewModel(step));
        }
      }

      complateSteps_tableView.setItems(stepViewModels);
    }
  }

  // Метод для оновлення статусу кроку
  private void updateStepStatus(String description, String newStatus) {
    try {
      stepRepository.updateStepStatusByName(description, newStatus);
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
    }
  }
}

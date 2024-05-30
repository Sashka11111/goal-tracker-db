package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.StepRepository;
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

  public CompleteStepsController() {
    this.stepRepository = new StepRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  public void initialize() {
    complateGoal_col_GoalName.setCellValueFactory(cellData -> cellData.getValue().goalNameProperty());
    complateGoal_col_Step.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    complateGoal_col_Status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    // Set the status column as a ComboBox for editing with only "Активний" and "Завершений"
    complateGoal_col_Status.setCellFactory(ComboBoxTableCell.forTableColumn("Активний", "Завершений"));
    complateGoal_col_Status.setOnEditCommit(event -> {
      StepViewModel stepViewModel = event.getRowValue();
      String newStatus = event.getNewValue();
      stepViewModel.setStatus(newStatus);
      updateStepStatus(stepViewModel.getDescription(), newStatus);
    });

    complateSteps_tableView.setEditable(true);
    loadSteps();

    // Set up listener for row selection
    complateSteps_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        stepDescriptionTextField.setText(newSelection.getDescription());
        stepStatusComboBox.setValue(newSelection.getStatus());
      }
    });

    // Populate ComboBox with status options
    stepStatusComboBox.getItems().addAll("Активний", "Завершений");

    // Set up update button action
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

  private void loadSteps() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Step> steps = stepRepository.getStepsByGoalId(currentUser.id());
      ObservableList<StepViewModel> stepViewModels = FXCollections.observableArrayList();
      for (Step step : steps) {
        stepViewModels.add(new StepViewModel(step));
      }
      complateSteps_tableView.setItems(stepViewModels);
    }
  }

  private void updateStepStatus(String description, String newStatus) {
    try {
      stepRepository.updateStepStatusByName(description, newStatus);
    } catch (EntityNotFoundException e) {
      e.printStackTrace(); // Handle the exception properly
    }
  }
}

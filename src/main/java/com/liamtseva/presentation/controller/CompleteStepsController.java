package com.liamtseva.presentation.controller;


import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.contract.StepRepository;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.persistence.repository.impl.StepRepositoryImpl;
import com.liamtseva.presentation.viewmodel.GoalViewModel;
import com.liamtseva.presentation.viewmodel.StepViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class CompleteStepsController {

  @FXML
  private TableView<Step> complateSteps_tableView;

  @FXML
  private TableColumn<Step, String> complateGoal_col_GoalName;

  @FXML
  private TableColumn<Step, String> complateGoal_col_Step;

  @FXML
  private TableColumn<Step, Boolean> complateGoal_col_Status;

  @FXML
  private TextField complateGoal_GoalID;

  @FXML
  private ComboBox<String> complateGoal_status;

  @FXML
  private Button complateGoal_btnUpdate;

  private final StepRepository stepRepository;
  public CompleteStepsController() {
    this.stepRepository= new StepRepositoryImpl(new DatabaseConnection().getDataSource());
  }
  @FXML
  public void initialize() {
    complateGoal_col_GoalName.setCellValueFactory(new PropertyValueFactory<>("goalName"));
    complateGoal_col_Step.setCellValueFactory(new PropertyValueFactory<>("stepDescription"));
    complateGoal_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));




    complateGoal_btnUpdate.setOnAction(event -> updateStepStatus());
  }
  @FXML
  void updateStepStatus() {/*
    StepViewModel selectedStep = complateSteps_tableView.getSelectionModel().getSelectedItem();
    String newStatus = complateGoal_status.getValue();
    if (selectedStep != null && newStatus != null) {
      try {
        int goalId = selectedStep.getIdGoal();
        stepRepository.updateStepStatus(goalId, newStatus);

        selectedStep.setStatus(newStatus);
        complateSteps_tableView.refresh();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
        // Обробити виняток відповідним чином
      }
    }*/
  }
}

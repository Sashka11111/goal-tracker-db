package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.presentation.viewmodel.GoalViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class CompleteGoalsController {

  @FXML
  private TableColumn<GoalViewModel, String> complateGoal_col_GoalName;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> complateGoal_col_endGoal;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> complateGoal_col_startGoal;

  @FXML
  private TableColumn<GoalViewModel, String> complateGoal_col_status;

  @FXML
  private TableView<GoalViewModel> complateGoal_tableView;

  @FXML
  private ComboBox<String> complateGoal_status;

  @FXML
  private TextField complateGoal_Goal;

  @FXML
  private Button complateGoal_btnUpdate;

  private final GoalRepository goalRepository;

  public CompleteGoalsController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  void initialize() {
    complateGoal_status.getItems().addAll("Активна", "Завершена", "Відкладена");

    complateGoal_col_GoalName.setCellValueFactory(cellData -> cellData.getValue().nameGoalProperty());
    complateGoal_col_endGoal.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
    complateGoal_col_startGoal.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    complateGoal_col_status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

    loadGoals();

    complateGoal_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        complateGoal_Goal.setText(newSelection.getNameGoal());
        complateGoal_status.setValue(newSelection.getStatus());
      }
    });

    complateGoal_btnUpdate.setOnAction(event -> onUpdateStatusClicked());
  }

  private void loadGoals() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Goal> goals = goalRepository.getAllGoalsByUserId(currentUser.id());
      ObservableList<GoalViewModel> goalViewModels = FXCollections.observableArrayList();
      for (Goal goal : goals) {
        goalViewModels.add(new GoalViewModel(goal));
      }
      complateGoal_tableView.setItems(goalViewModels);
    }
  }

  @FXML
  void onUpdateStatusClicked() {
    GoalViewModel selectedGoal = complateGoal_tableView.getSelectionModel().getSelectedItem();
    String newStatus = complateGoal_status.getValue();
    if (selectedGoal != null && newStatus != null) {
      try {
        int goalId = selectedGoal.getIdGoal();
        goalRepository.updateGoalStatus(goalId, newStatus);

        selectedGoal.setStatus(newStatus);
        complateGoal_Goal.clear();
        complateGoal_status.setValue(null);
        complateGoal_tableView.refresh();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

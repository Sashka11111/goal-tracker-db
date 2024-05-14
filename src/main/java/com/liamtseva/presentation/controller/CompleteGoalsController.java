package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import com.liamtseva.presentation.viewmodel.GoalViewModel;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CompleteGoalsController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TextField complateGoal_GoalID;

  @FXML
  private Button complateGoal_btnUpdate;
  @FXML
  private TableColumn<GoalViewModel, Integer> complateGoal_col_GoalID;

  @FXML
  private TableColumn<GoalViewModel, String> complateGoal_col_GoalName;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> complateGoal_col_endGoal;

  @FXML
  private TableColumn<GoalViewModel, LocalDate> complateGoal_col_startGoal;


  @FXML
  private ComboBox<String> complateGoal_status;

  @FXML
  private TableView<GoalViewModel> complateGoal_tableView;

  private final GoalRepository goalRepository;


  public CompleteGoalsController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення GoalRepositoryImpl з DatabaseConnection
  }@FXML
  void initialize() {
    loadGoals();
    complateGoal_status.getItems().addAll("Активна", "Завершена", "Відкладена");

    complateGoal_col_GoalID.setCellValueFactory(cellData -> cellData.getValue().idGoalProperty().asObject());
    complateGoal_col_GoalName.setCellValueFactory(cellData -> cellData.getValue().nameGoalProperty());
    complateGoal_col_endGoal.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
    complateGoal_col_startGoal.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

    complateGoal_btnUpdate.setOnAction(event -> onUpdateStatusClicked());
  }
  private void loadGoals() {
    List<Goal> goals = goalRepository.getAllGoals();
    ObservableList<GoalViewModel> goalViewModels = FXCollections.observableArrayList();
    for (Goal goal : goals) {
      goalViewModels.add(new GoalViewModel(goal));
    }
    complateGoal_tableView.setItems(goalViewModels);
  }
  @FXML
  void onUpdateStatusClicked() {
    String goalIdString = complateGoal_GoalID.getText();
    String newStatus = complateGoal_status.getValue();
    if (goalIdString != null && !goalIdString.isEmpty() && newStatus != null) {
      try {
        int goalId = Integer.parseInt(goalIdString);
        // Оновлення статусу в базі даних
        goalRepository.updateGoalStatus(goalId, newStatus);

        // Отримання обраного елемента у таблиці
        GoalViewModel selectedGoal = complateGoal_tableView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null) {
          // Оновлення статусу обраної цілі в ObservableList
          selectedGoal.setStatus(newStatus);
        }
      } catch (NumberFormatException | EntityNotFoundException e) {
        e.printStackTrace();
        // Обробити виняток відповідним чином
      }
    }
  }
}

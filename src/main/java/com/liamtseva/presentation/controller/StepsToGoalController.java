package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.domain.validation.StepValidator;
import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.entity.User;
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

import java.util.List;
import java.util.stream.Collectors;

public class StepsToGoalController {

  @FXML
  private TableView<StepViewModel> Steps_tableView;

  @FXML
  private TableColumn<StepViewModel, String> Steps_col_NameGoal;

  @FXML
  private TableColumn<StepViewModel, String> Steps_col_StepStatus;

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
  private Label errorMessage;

  @FXML
  private TextField description;

  @FXML
  private TextField searchField;

  @FXML
  private ComboBox<Goal> goal;

  private final StepRepository stepRepository;
  private final GoalRepository goalRepository;

  // Конструктор для ініціалізації репозиторіїв
  public StepsToGoalController() {
    this.stepRepository = new StepRepositoryImpl(new DatabaseConnection().getDataSource());
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  // Метод, який викликається під час ініціалізації контролера
  @FXML
  void initialize() {
    Steps_col_NameGoal.setCellValueFactory(cellData -> cellData.getValue().goalNameProperty());
    Steps_col_description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    Steps_col_StepStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    loadSteps();
    Steps_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        description.setText(newSelection.getDescription()); // Встановити опис кроку у текстовому полі
      }
    });
    // Обробники подій для кнопок
    btn_add.setOnAction(event -> onAddClicked());
    btn_clear.setOnAction(event -> clearField());
    btn_delete.setOnAction(event -> onDeleteClicked());
    btn_edit.setOnAction(event -> onEditClicked());
    searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSteps(newValue));
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

  // Додавання нового кроку
  private void onAddClicked() {
    Goal selectedGoal = goal.getValue();
    String stepDescription = description.getText();
    List<String> existingDescriptions = Steps_tableView.getItems().stream()
        .map(StepViewModel::getDescription)
        .collect(Collectors.toList());

    // Валідація опису кроку
    String descriptionError = StepValidator.validateDescription(stepDescription);
    if (descriptionError != null) {
      errorMessage.setText(descriptionError);
      return;
    }
    if (selectedGoal == null) {
      errorMessage.setText("Будь ласка, виберіть ціль перед додаванням кроку.");
      return;
    }
    // Перевірка на дублювання опису
    if (StepValidator.isDescriptionDuplicate(stepDescription, existingDescriptions)) {
      errorMessage.setText("Такий крок вже існує");
      return;
    }

    if (selectedGoal != null) {
      Step newStep = new Step(0, selectedGoal.id(), selectedGoal.nameGoal(), stepDescription, "Активний"); // Встановити статус "Активний"
      try {
        int generatedId = stepRepository.addStep(newStep);

        // Створити новий запис Step з згенерованим ID
        Step stepWithId = new Step(generatedId, newStep.goalId(), newStep.goalName(), newStep.description(), newStep.status());

        // Додати новий крок з коректним ID до TableView
        Steps_tableView.getItems().add(new StepViewModel(stepWithId));
        clearField();
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  // Фільтрація кроків
  private void filterSteps(String searchText) {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      List<Step> steps = stepRepository.searchStepsByUserIdAndText(currentUser.id(), searchText);
      ObservableList<StepViewModel> filteredList = FXCollections.observableArrayList();
      for (Step step : steps) {
        filteredList.add(new StepViewModel(step));
      }
      Steps_tableView.setItems(filteredList);

      if (filteredList.isEmpty()) {
        Steps_tableView.setPlaceholder(new Label("На жаль у Вас немає такого кроку"));
      } else {
        Steps_tableView.setPlaceholder(null);
      }
    }
  }

  // Редагування кроку
  private void onEditClicked() {
    StepViewModel selectedStep = Steps_tableView.getSelectionModel().getSelectedItem();
    if (selectedStep != null) {
      String newDescription = description.getText();
      List<String> existingDescriptions = Steps_tableView.getItems().stream()
          .map(StepViewModel::getDescription)
          .collect(Collectors.toList());
      existingDescriptions.remove(selectedStep.getDescription());

      // Валідація опису кроку
      String descriptionError = StepValidator.validateDescription(newDescription);
      if (descriptionError != null) {
        errorMessage.setText(descriptionError);
        return;
      }

      // Перевірка на дублювання опису
      if (StepValidator.isDescriptionDuplicate(newDescription, existingDescriptions)) {
        errorMessage.setText("Такий крок вже існує");
        return;
      }

      selectedStep.setDescription(newDescription);
      try {
        // Оновити крок у базі даних
        stepRepository.updateStep(selectedStep.getStep());
        // Оновити дані в TableView
        Steps_tableView.refresh();
        clearField();
      } catch (EntityNotFoundException e) {
        e.printStackTrace(); // Обробка помилки
      }
    }
  }

  // Видалення кроку
  private void onDeleteClicked() {
    StepViewModel selectedStep = Steps_tableView.getSelectionModel().getSelectedItem();
    if (selectedStep != null) {
      try {
        // Видалити крок з бази даних
        stepRepository.deleteStep(selectedStep.getIdStep());
        // Видалити крок з таблиці
        Steps_tableView.getItems().remove(selectedStep);
        clearField();
      } catch (EntityNotFoundException e) {
        e.printStackTrace(); // Обробка помилки
      }
    }
  }

  // Очистити поля вводу
  private void clearField() {
    description.clear();
    goal.setValue(null);
    errorMessage.setText("");
    searchField.clear();
  }
}

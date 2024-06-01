package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import com.liamtseva.persistence.repository.impl.GoalRepositoryImpl;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyActivityController {

  @FXML
  private Label activeGoalsLabel;

  @FXML
  private Label postponedGoalsLabel;

  @FXML
  private Label completedGoalsLabel;

  @FXML
  private AreaChart<String, Number> areaChart;

  private final GoalRepository goalRepository;

  public MyActivityController() {
    this.goalRepository = new GoalRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  void initialize() {
    loadUserGoals();
  }

  private void loadUserGoals() {
    int userId = AuthenticatedUser.getInstance().getCurrentUser().id();
    List<Goal> goals = goalRepository.filterGoalsByUserId(userId);

    long activeCount = goals.stream().filter(goal -> "Активна".equals(goal.status())).count();
    long postponedCount = goals.stream().filter(goal -> "Відкладена".equals(goal.status())).count();
    long completedCount = goals.stream().filter(goal -> "Завершена".equals(goal.status())).count();

    activeGoalsLabel.setText(String.valueOf(activeCount));
    postponedGoalsLabel.setText(String.valueOf(postponedCount));
    completedGoalsLabel.setText(String.valueOf(completedCount));

    updateAreaChart(goals);
  }

  private void updateAreaChart(List<Goal> goals) {
    XYChart.Series<String, Number> activeSeries = new XYChart.Series<>();
    activeSeries.setName("Активні цілі");

    XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
    completedSeries.setName("Завершені цілі");

    XYChart.Series<String, Number> postponedSeries = new XYChart.Series<>();
    postponedSeries.setName("Відкладені цілі");

    // Collect data by period
    Map<String, Long> activeGoals = goals.stream()
        .filter(goal -> "Активна".equals(goal.status()))
        .collect(Collectors.groupingBy(goal -> goal.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.counting()));

    Map<String, Long> completedGoals = goals.stream()
        .filter(goal -> "Завершена".equals(goal.status()))
        .collect(Collectors.groupingBy(goal -> goal.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.counting()));

    Map<String, Long> postponedGoals = goals.stream()
        .filter(goal -> "Відкладена".equals(goal.status()))
        .collect(Collectors.groupingBy(goal -> goal.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.counting()));

    activeGoals.forEach((period, count) -> activeSeries.getData().add(new XYChart.Data<>(period, count)));
    completedGoals.forEach((period, count) -> completedSeries.getData().add(new XYChart.Data<>(period, count)));
    postponedGoals.forEach((period, count) -> postponedSeries.getData().add(new XYChart.Data<>(period, count)));

    areaChart.getData().clear();
    areaChart.getData().addAll(activeSeries, completedSeries, postponedSeries);
  }
}

package com.liamtseva.presentation.viewmodel;

import com.liamtseva.persistence.entity.Goal;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class GoalViewModel {
  private final SimpleIntegerProperty idGoal;
  private final SimpleIntegerProperty userId;
  private final SimpleStringProperty nameGoal;
  private final SimpleStringProperty description;
  private final SimpleIntegerProperty categoryId;
  private final SimpleObjectProperty<LocalDate> startDate;
  private final SimpleObjectProperty<LocalDate> endDate;
  private final SimpleStringProperty status;

  public GoalViewModel(Goal goal) {
    this.idGoal = new SimpleIntegerProperty(goal.id());
    this.userId = new SimpleIntegerProperty(goal.userId());
    this.nameGoal = new SimpleStringProperty(goal.nameGoal());
    this.description = new SimpleStringProperty(goal.description());
    this.categoryId = new SimpleIntegerProperty(goal.categoryId());
    this.startDate = new SimpleObjectProperty<>(goal.startDate());
    this.endDate = new SimpleObjectProperty<>(goal.endDate());
    this.status = new SimpleStringProperty(goal.status());
  }

  public int getIdGoal() {
    return idGoal.get();
  }

  public SimpleIntegerProperty idGoalProperty() {
    return idGoal;
  }

  public void setIdGoal(int idGoal) {
    this.idGoal.set(idGoal);
  }

  public int getUserId() {
    return userId.get();
  }

  public SimpleIntegerProperty userIdProperty() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId.set(userId);
  }

  public String getNameGoal() {
    return nameGoal.get();
  }

  public SimpleStringProperty nameGoalProperty() {
    return nameGoal;
  }

  public void setNameGoal(String nameGoal) {
    this.nameGoal.set(nameGoal);
  }

  public String getDescription() {
    return description.get();
  }

  public SimpleStringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public int getCategoryId() {
    return categoryId.get();
  }

  public SimpleIntegerProperty categoryIdProperty() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId.set(categoryId);
  }

  public LocalDate getStartDate() {
    return startDate.get();
  }

  public SimpleObjectProperty<LocalDate> startDateProperty() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate.set(startDate);
  }

  public LocalDate getEndDate() {
    return endDate.get();
  }

  public SimpleObjectProperty<LocalDate> endDateProperty() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate.set(endDate);
  }

  public String getStatus() {
    return status.get();
  }

  public SimpleStringProperty statusProperty() {
    return status;
  }

  public void setStatus(String status) {
    this.status.set(status);
  }
}

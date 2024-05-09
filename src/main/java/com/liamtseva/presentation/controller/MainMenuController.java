package com.liamtseva.goals;

import com.liamtseva.goals.entity.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainMenuController {

  @FXML
  private Button btn_myGoals;

  @FXML
  private Button btn_myProfile;

  @FXML
  private Button btn_completeGoal;

  @FXML
  private Button btn_exit;

  @FXML
  private StackPane stackPane;

  @FXML
  private StackPane contentArea;
  @FXML
  private Label lbl_userName;

  @FXML
  void initialize() {
    loadMyGoals();
    btn_myGoals.setOnAction(event -> showMyGoalPage());
    btn_myProfile.setOnAction(event -> showMyProfilelPage());
    btn_completeGoal.setOnAction(event -> showCompleteGoalsPage());
    btn_exit.setOnAction(event ->{
      System.exit(0);
    });
  }
  private void displayUserName(){

  }

  private void moveStackPane(Button button) {
    double buttonX = button.localToParent(button.getBoundsInLocal()).getMinX();
    double buttonY = button.localToParent(button.getBoundsInLocal()).getMinY();
    TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), stackPane);
    transition.setToX(buttonX);
    stackPane.setLayoutY(buttonY);
  }

  private void showMyGoalPage() {
    moveStackPane(btn_myGoals);
    loadFXML("myGoals.fxml");
  }

  private void showMyProfilelPage() {
    moveStackPane(btn_myProfile);
    loadFXML("myProfile.fxml");
  }

  private void showCompleteGoalsPage() {
    moveStackPane(btn_completeGoal);
    loadFXML("completeGoals.fxml");
  }

  private void loadFXML(String fxmlFileName) {
    try {
      Parent fxml = FXMLLoader.load(getClass().getResource(fxmlFileName));
      contentArea.getChildren().clear(); // Очищаємо contentArea
      contentArea.getChildren().add(fxml); // Додаємо завантажений контент
    } catch (IOException ex) {
      Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  private void loadMyGoals() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("myGoals.fxml"));
      AnchorPane myGoalsPane = loader.load();

      // Отримати контролер myGoals.fxml
      MyGoalsController myGoalsController = loader.getController();

      // Вставити myGoalsPane в contentArea
      contentArea.getChildren().clear(); // Очистіть будь-які попередні вміст, якщо потрібно
      contentArea.getChildren().add(myGoalsPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
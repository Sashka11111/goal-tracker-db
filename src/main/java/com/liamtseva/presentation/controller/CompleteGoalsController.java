package com.liamtseva.goals;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
  private TableColumn<?, ?> complateGoal_col_GoalID;

  @FXML
  private TableColumn<?, ?> complateGoal_col_GoalName;

  @FXML
  private TableColumn<?, ?> complateGoal_col_engGoal;

  @FXML
  private TableColumn<?, ?> complateGoal_col_startGoal;

  @FXML
  private TableColumn<?, ?> complateGoal_col_status;

  @FXML
  private ComboBox<?> complateGoal_status;

  @FXML
  private TableView<?> complateGoal_tableView;

  @FXML
  void initialize() {

  }

}

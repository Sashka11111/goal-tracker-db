package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.entity.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MyGoalsController {

  @FXML
  private TableColumn<?, ?> MyGoals_col_Category;

  @FXML
  private TableColumn<?, ?> MyGoals_col_Description;

  @FXML
  private TableColumn<?, ?> MyGoals_col_Goal;

  @FXML
  private TableColumn<?, ?> MyGoals_col_endDate;

  @FXML
  private TableColumn<?, ?> MyGoals_col_startDate;

  @FXML
  private TableColumn<?, ?> MyGoals_col_status;

  @FXML
  private TableView<?> MyGoals_tableView;
  @FXML
  private TextField description;

  @FXML
  private TextField goal;

  @FXML
  private Button btn_add;

  @FXML
  private Button btn_clear;

  @FXML
  private Button btn_delete;

  @FXML
  private Button btn_update;

  @FXML
  private ComboBox<Category> category;

  @FXML
  private DatePicker endDate;

  @FXML
  private DatePicker startDate;


  @FXML
  void initialize() {

  }
}
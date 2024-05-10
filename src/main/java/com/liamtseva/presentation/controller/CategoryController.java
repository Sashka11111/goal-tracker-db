package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.dao.CategoryDAO;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.presentation.viewmodel.CategoryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoryController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private TableColumn<CategoryViewModel, Integer> Category_col_IdCategory;

  @FXML
  private TableColumn<CategoryViewModel, String> Category_col_NameCategory;

  @FXML
  private TableView<CategoryViewModel> Category_tableView;

  @FXML
  private TextField addCategory;

  @FXML
  private Button btn_add;

  @FXML
  private Button btn_clear;

  @FXML
  private Button btn_delete;

  @FXML
  private Button btn_update;

  private final CategoryDAO categoryDAO = new CategoryDAO();
  private final ObservableList<CategoryViewModel> categoryList = FXCollections.observableArrayList();

  @FXML
  void initialize() {
    initializeTable();
    loadData();

    btn_add.setOnAction(event -> addCategory());
    btn_clear.setOnAction(event -> clearFields());
    btn_delete.setOnAction(event -> deleteCategory());
    btn_update.setOnAction(event -> updateCategory());
  }

  private void initializeTable() {
    Category_col_IdCategory.setCellValueFactory(new PropertyValueFactory<>("id"));
    Category_col_NameCategory.setCellValueFactory(new PropertyValueFactory<>("name"));
  }

  private void loadData() {
    ObservableList<Category> categories = FXCollections.observableArrayList(categoryDAO.getAllCategories());
    categoryList.setAll(convertToViewModel(categories));
    Category_tableView.setItems(categoryList);
  }

  private ObservableList<CategoryViewModel> convertToViewModel(ObservableList<Category> categories) {
    ObservableList<CategoryViewModel> viewModels = FXCollections.observableArrayList();
    for (Category category : categories) {
      CategoryViewModel viewModel = new CategoryViewModel(category.id(), category.name());
      viewModels.add(viewModel);
    }
    return viewModels;
  }

  private void addCategory() {
    String categoryName = addCategory.getText().trim();
    if (!categoryName.isEmpty()) {
      Category newCategory = new Category(0, categoryName);
      categoryDAO.addCategory(newCategory);
      loadData(); // Оновлення даних у таблиці
      clearFields(); // Очищення полів вводу
    }
  }


  private void clearFields() {
    addCategory.clear();
  }

  private void deleteCategory() {
    CategoryViewModel selectedCategory = Category_tableView.getSelectionModel().getSelectedItem();
    if (selectedCategory != null) {
      categoryDAO.deleteCategory(selectedCategory.getId());
      categoryList.remove(selectedCategory);
    }
  }

  private void updateCategory() {
    CategoryViewModel selectedCategory = Category_tableView.getSelectionModel().getSelectedItem();
    String newName = addCategory.getText().trim();
    if (selectedCategory != null && !newName.isEmpty()) {
      selectedCategory.setName(newName);
      categoryDAO.updateCategory(new Category(selectedCategory.getId(), newName));
      Category_tableView.refresh();
      clearFields();
    }
  }
}

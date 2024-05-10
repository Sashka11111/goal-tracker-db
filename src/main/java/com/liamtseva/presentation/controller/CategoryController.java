package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.exception.EntityNotFoundException;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.presentation.viewmodel.CategoryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoryController {

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

  private final CategoryRepository categoryRepository;
  private final ObservableList<CategoryViewModel> categoryList = FXCollections.observableArrayList();

  // Конструктор, що приймає CategoryRepository
  public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

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
    if (categoryRepository != null) {
      List<Category> categories = categoryRepository.getAllCategories();
      categoryList.setAll(convertToViewModel(categories));
      Category_tableView.setItems(categoryList);
    }
  }

  private ObservableList<CategoryViewModel> convertToViewModel(List<Category> categories) {
    ObservableList<CategoryViewModel> viewModels = FXCollections.observableArrayList();
    for (Category category : categories) {
      CategoryViewModel viewModel = new CategoryViewModel(category.id(), category.name());
      viewModels.add(viewModel);
    }
    return viewModels;
  }

  private void addCategory() {
    String categoryName = addCategory.getText().trim();
    if (!categoryName.isEmpty() && categoryRepository != null) {
      Category newCategory = new Category(0, categoryName);
      try {
        categoryRepository.addCategory(newCategory);
        loadData();
        clearFields();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private void clearFields() {
    addCategory.clear();
  }

  private void deleteCategory() {
    CategoryViewModel selectedCategory = Category_tableView.getSelectionModel().getSelectedItem();
    if (selectedCategory != null && categoryRepository != null) {
      try {
        categoryRepository.deleteCategory(selectedCategory.getId());
        categoryList.remove(selectedCategory);
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private void updateCategory() {
    CategoryViewModel selectedCategory = Category_tableView.getSelectionModel().getSelectedItem();
    String newName = addCategory.getText().trim();
    if (selectedCategory != null && !newName.isEmpty() && categoryRepository != null) {
      selectedCategory.setName(newName);
      try {
        categoryRepository.updateCategory(new Category(selectedCategory.getId(), newName));
        Category_tableView.refresh();
        clearFields();
      } catch (EntityNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}

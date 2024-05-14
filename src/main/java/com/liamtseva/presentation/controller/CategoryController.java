package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.presentation.viewmodel.CategoryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

  private final CategoryRepository categoryRepository;

  public CategoryController() {
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення CategoryRepositoryImpl з DatabaseConnection
  }


  @FXML
  void initialize() {
    // Прив'язка стовпців до даних таблиці
    Category_col_IdCategory.setCellValueFactory(cellData -> cellData.getValue().idCategoryProperty().asObject());
    Category_col_NameCategory.setCellValueFactory(cellData -> cellData.getValue().nameCategoryProperty());
    // Загрузка категорій з бази даних та відображення їх у таблиці
    loadCategories();

    // Обробники подій для кнопок
    btn_add.setOnAction(event -> onAddClicked());
    btn_clear.setOnAction(event -> onClearClicked());
    btn_delete.setOnAction(event -> onDeleteClicked());
  }

  // Завантаження категорій з бази даних
  private void loadCategories() {
    List<Category> categories = categoryRepository.getAllCategories();
    ObservableList<CategoryViewModel> categoryViewModels = FXCollections.observableArrayList();
    categories.forEach(category -> categoryViewModels.add(new CategoryViewModel(category)));
    Category_tableView.setItems(categoryViewModels);
  }

  // Логіка додавання нової категорії
  private void onAddClicked() {
    String categoryName = addCategory.getText();
    if (!categoryName.isEmpty()) {
      Category newCategory = new Category(0, categoryName);
      try {
        categoryRepository.addCategory(newCategory);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
      }
      loadCategories();
      addCategory.clear();
    } else {
      // Вивести повідомлення про помилку, якщо поле пусте
    }
  }

  // Логіка очищення поля введення нової категорії
  private void onClearClicked() {
    addCategory.clear();
  }

  // Логіка видалення обраної категорії
  private void onDeleteClicked() {
    CategoryViewModel selectedCategoryViewModel = Category_tableView.getSelectionModel().getSelectedItem();
    if (selectedCategoryViewModel != null) {
      try {
        categoryRepository.deleteCategory(selectedCategoryViewModel.getIdCategory());
        loadCategories();
      } catch (EntityNotFoundException e) {
        // Обробка помилки видалення категорії
        e.printStackTrace();
      }
    }
  }
}
package com.liamtseva.persistence.repository;

import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.exception.EntityNotFoundException;
import java.util.List;

public interface CategoryRepository {
  void addCategory(Category category);
  Category getCategoryById(int id) throws EntityNotFoundException;
  List<Category> getAllCategories();
  void updateCategory(Category category) throws EntityNotFoundException;
  void deleteCategory(int id) throws EntityNotFoundException;
}
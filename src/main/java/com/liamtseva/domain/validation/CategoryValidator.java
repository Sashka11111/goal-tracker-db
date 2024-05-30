package com.liamtseva.domain.validation;

import com.liamtseva.persistence.entity.Category;
import java.util.List;

public class CategoryValidator {

  // Метод для перевірки імені категорії
  public static String validateCategoryName(String name, List<Category> existingCategories) {
    if (name == null || name.trim().isEmpty()) {
      return "Ім'я категорії не може бути порожнім";
    } else if (name.length() > 50) {
      return "Ім'я категорії занадто довге";
    } else if (isCategoryNameDuplicate(name, existingCategories)) {
      return "Категорія з таким ім'ям вже існує";
    } else {
      return null; // Якщо ім'я валідне, повертаємо null
    }
  }

  // Метод для перевірки наявності категорії з заданим ім'ям
  private static boolean isCategoryNameDuplicate(String name, List<Category> existingCategories) {
    return existingCategories.stream().anyMatch(category -> category.name().equalsIgnoreCase(name));
  }
}

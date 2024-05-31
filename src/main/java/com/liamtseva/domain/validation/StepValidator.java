package com.liamtseva.domain.validation;

import java.util.List;

public class StepValidator {

  // Метод для перевірки опису кроку
  public static String validateDescription(String description) {
    if (description == null || description.trim().isEmpty()) {
      return "Опис кроку не може бути порожнім";
    } else if (description.length() > 100) {
      return "Опис кроку занадто довгий";
    } else {
      return null; // Якщо опис валідний, повертаємо null
    }
  }

  // Метод для перевірки наявності кроку з заданим описом
  public static boolean isDescriptionDuplicate(String description, List<String> existingDescriptions) {
    return existingDescriptions.contains(description);
  }
}

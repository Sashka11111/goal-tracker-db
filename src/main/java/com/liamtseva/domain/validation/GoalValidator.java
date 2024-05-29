package com.liamtseva.domain.validation;

import com.liamtseva.persistence.entity.Category;
import java.time.LocalDate;
import java.util.List;

public class GoalValidator {

  public static String validate(String goalName, String goalDescription, List<Category> selectedCategories, LocalDate goalStartDate, LocalDate goalEndDate) {
    if (goalName == null || goalName.isEmpty()) {
      return "Назва цілі не може бути порожньою";
    }
    if (goalDescription == null || goalDescription.isEmpty()) {
      return "Опис цілі не може бути порожнім";
    }
    if (selectedCategories == null || selectedCategories.isEmpty()) {
      return "Будь ласка, виберіть хоча б одну категорію";
    }
    if (goalStartDate == null) {
      return "Будь ласка, виберіть дату початку";
    }
    if (goalEndDate == null) {
      return "Будь ласка, виберіть дату завершення";
    }
    if (goalEndDate.isBefore(goalStartDate)) {
      return "Дата завершення не може бути меншою за дату початку";
    }
    return null; // Якщо всі перевірки пройдені успішно
  }
}

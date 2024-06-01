package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.domain.exception.EntityNotFoundException;
import java.util.List;

public interface GoalRepository {
  int addGoal(Goal goal);
  List<Goal> getAllGoalsByUserId(int userId);
  List<Goal> filterGoalsByUserId(int userId);
  void updateGoal(Goal goal) throws EntityNotFoundException;
  void deleteGoal(int id) throws EntityNotFoundException;
  void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException;
  List<Goal> searchGoalsByUserIdAndText(int userId, String searchText);
  void addCategoryToGoal(int goalId, int categoryId);
  void removeCategoryFromGoal(int goalId, int categoryId);
  void clearCategoriesFromGoal(int goalId);
  List<Integer> getCategoriesByGoalId(int goalId);
  List<Integer> getGoalsByCategoryId(int categoryId);
}
package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.domain.exception.EntityNotFoundException;
import java.util.List;

public interface GoalRepository {
  void addGoal(Goal goal);
  Goal getGoalById(int id) throws EntityNotFoundException;
  Goal getGoalByName(String name) throws EntityNotFoundException;
  List<Goal> getAllGoalsByUserId(int userId);
  List<Goal> getAllGoals();
  List<Goal> filterGoalsByUserId(int userId);
  void updateGoal(Goal goal) throws EntityNotFoundException;
  void deleteGoal(int id) throws EntityNotFoundException;
  void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException;
  void updateGoalStatusByName(String goalName, String newStatus) throws EntityNotFoundException;
}
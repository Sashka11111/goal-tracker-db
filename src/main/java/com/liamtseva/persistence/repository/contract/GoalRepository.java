package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Goal;
import com.liamtseva.domain.exception.EntityNotFoundException;
import java.util.List;

public interface GoalRepository {
  void addGoal(Goal goal);
  Goal getGoalById(int id) throws EntityNotFoundException;
  List<Goal> getAllGoals();
  void updateGoal(Goal goal) throws EntityNotFoundException;
  void deleteGoal(int id) throws EntityNotFoundException;
  void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException;

}
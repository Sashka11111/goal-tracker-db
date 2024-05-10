package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.exception.EntityNotFoundException;
import java.util.List;

public interface StepRepository {
  void addStep(Step step);
  Step getStepById(int id) throws EntityNotFoundException;
  List<Step> getAllSteps();
  void updateStep(Step step) throws EntityNotFoundException;
  void deleteStep(int id) throws EntityNotFoundException;
}
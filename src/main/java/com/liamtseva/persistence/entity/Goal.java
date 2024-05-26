package com.liamtseva.persistence.entity;

import java.time.LocalDate;

public record Goal(
    int id,
    int userId,
    String nameGoal,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    String status)
    implements Entity, Comparable<Goal> {

  @Override
  public int compareTo(Goal o) {
    return Integer.compare(this.id, o.id());
  }
}

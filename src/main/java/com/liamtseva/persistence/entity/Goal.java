package com.liamtseva.entity;

import java.time.LocalDate;
import java.util.UUID;

public record Goal(
    UUID id,
    String description,
    String category,
    LocalDate startDate,
    LocalDate endDate,
    String status)
    implements Entity, Comparable<Goal> {

  @Override
  public int compareTo(Goal o) {
    return this.description.compareTo(o.description);
  }
}

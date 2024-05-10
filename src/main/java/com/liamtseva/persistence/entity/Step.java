package com.liamtseva.persistence.entity;

public record Step(
    int id,
    int goalId,
    String description)
    implements Entity, Comparable<Step> {

  @Override
  public int compareTo(Step o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}

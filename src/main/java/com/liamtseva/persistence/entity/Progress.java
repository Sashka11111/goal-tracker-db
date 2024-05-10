package com.liamtseva.persistence.entity;

public record Progress(
    int id,
    int userId,
    int goalId,
    String status)
    implements Entity, Comparable<Progress> {

  @Override
  public int compareTo(Progress o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}

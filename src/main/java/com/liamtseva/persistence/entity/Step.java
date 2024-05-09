package com.liamtseva.entity;

import java.util.UUID;

public record Step(
    UUID id,
    UUID goalId,
    String description)
    implements Entity, Comparable<Step> {

  @Override
  public int compareTo(Step o) {
    // Порівняння за id
    return this.id.compareTo(o.id);
  }
}
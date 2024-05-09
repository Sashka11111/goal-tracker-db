package com.liamtseva.entity;

import java.util.UUID;

public record Progress(
    UUID id,
    UUID goalId,
    UUID userId,
    String status)
    implements Entity, Comparable<Progress> {

  @Override
  public int compareTo(Progress o) {
    // Порівняння за id
    return this.id.compareTo(o.id);
  }
}


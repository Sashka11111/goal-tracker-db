package com.liamtseva.entity;

import java.util.UUID;

public record Category(
    UUID id,
    String name)
    implements Entity, Comparable<Category> {

  @Override
  public int compareTo(Category o) {
    return this.name.compareTo(o.name);
  }
}
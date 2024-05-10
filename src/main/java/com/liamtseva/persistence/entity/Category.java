package com.liamtseva.persistence.entity;

import java.util.UUID;

public record Category(
    int id,
    String name)
    implements Entity, Comparable<Category> {

  @Override
  public int compareTo(Category o) {
    return Integer.compare(this.id, o.id);
  }

}
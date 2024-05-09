package com.liamtseva.entity;

import java.util.UUID;
public record User(UUID  id,
                   String username,
                   String password,
                   String profileImage)
    implements Entity, Comparable<User> {
  @Override
  public int compareTo(User o) {
    return this.username.compareTo(o.username);
  }
}

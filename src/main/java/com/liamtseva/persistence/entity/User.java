package com.liamtseva.persistence.entity;

public record User(
    int id,
    String username,
    String password,
    String profileImage)
    implements Entity, Comparable<User> {
  public User(String username, String password) {
    this(0, username, password, "");
  }
  @Override
  public int compareTo(User o) {
    return this.username.compareTo(o.username);
  }
}

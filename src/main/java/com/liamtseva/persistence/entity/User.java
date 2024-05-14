package com.liamtseva.persistence.entity;

public record User(
    int id,
    String username,
    String password,
    byte[] profileImage)
    implements Entity, Comparable<User> {
  public User(String username, String password, byte[]profileImage) {
    this(0, username, password, profileImage);
  }
  @Override
  public int compareTo(User o) {
    return this.username.compareTo(o.username);
  }
}

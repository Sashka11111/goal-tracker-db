package com.liamtseva.presentation.viewmodel;

import com.liamtseva.persistence.entity.User;

public class UserViewModel {
  private int id;
  private String username;
  private String password;
  private byte[] profileImage;

  public UserViewModel(User user) {
    this.id = user.id();
    this.username = user.username();
    this.password = user.password();
    this.profileImage = user.profileImage();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public byte[] getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(byte[] profileImage) {
    this.profileImage = profileImage;
  }
}

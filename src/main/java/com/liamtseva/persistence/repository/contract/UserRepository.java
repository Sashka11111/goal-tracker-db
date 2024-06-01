package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.User;
import com.liamtseva.domain.exception.EntityNotFoundException;

public interface UserRepository {
  void addUser(User user);
  User findByUsername(String username) throws EntityNotFoundException;
  boolean isUsernameExists(String username);
}
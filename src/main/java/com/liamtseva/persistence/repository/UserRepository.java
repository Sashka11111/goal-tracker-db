package com.liamtseva.persistence.repository;

import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.exception.EntityNotFoundException;
import java.util.List;

public interface UserRepository {
  void addUser(User user);
  User getUserById(int id) throws EntityNotFoundException;
  List<User> getAllUsers();
  void updateUser(User user) throws EntityNotFoundException;
  void deleteUser(int id) throws EntityNotFoundException;
}
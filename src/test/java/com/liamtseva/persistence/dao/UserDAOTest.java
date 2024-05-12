package com.liamtseva.persistence.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.*;

class UserDAOTest {

  private UserDAO userDAO;
  private DatabaseConnection databaseConnection;

  @BeforeEach
  void init() {
    databaseConnection = new DatabaseConnection(); // Ініціалізація об'єкту DatabaseConnection
    userDAO = new UserDAO(); // Ініціалізація об'єкту UserDAO
    List<User> existingUsers = userDAO.getAllUsers(); // Assuming getAllUsers exists

    // Delete existing users one by one (optional)
    for (User user : existingUsers) {
      userDAO.deleteUser(user.id()); // Assuming deleteUser(int) exists
    }
  }

  @AfterEach
  void tearDown() {
   // databaseConnection.closeConnection();
  }

  @Test
  void addUser() {
    // Створюємо користувача з унікальним ім'ям
    User user = new User("testUser1", "testPassword1");

    // Перевіряємо, чи користувач з таким ім'ям вже існує
    User existingUser = userDAO.getUserByLogin(user.username());
    assertNull(existingUser, "User with the same username already exists");

    // Додаємо користувача до бази даних
    userDAO.addUser(user);

    // Отримуємо доданого користувача з бази даних
    User savedUser = userDAO.getUserByLogin(user.username());

    // Перевіряємо, що користувач був успішно доданий
    assertNotNull(savedUser, "User was not added successfully");
    assertEquals(user.username(), savedUser.username(), "Username does not match");
    assertEquals(user.password(), savedUser.password(), "Password does not match");
  }


  @Test
  void getUserByLogin() {
    User user = new User("testUser", "testPassword");
    userDAO.addUser(user);
    User retrievedUser = userDAO.getUserByLogin("testUser");
    assertNotNull(retrievedUser);
    assertEquals("testUser", retrievedUser.username());
    assertEquals("testPassword", retrievedUser.password());
  }

  @Test
  void getAllUsers() {
    // Add a few users to the database
    User user1 = new User("user1", "password1");
    User user2 = new User("user2", "password2");
    userDAO.addUser(user1);
    userDAO.addUser(user2);

    // Retrieve all users from the database
    List<User> users = userDAO.getAllUsers();

    // Verify that the retrieved list is not empty
    assertNotNull(users);
    assertFalse(users.isEmpty(), "No users found in the database");

    // You can further verify the size of the list and compare it to the number of users added
    assertEquals(2, users.size(), "Incorrect number of users retrieved");
    // Alternatively, check if specific users are present in the retrieved list
  }


  @Test
  void getUserById() {
    User user = new User("testUser", "testPassword");
    userDAO.addUser(user);
    User retrievedUser = userDAO.getUserByLogin("testUser");
    assertNotNull(retrievedUser);
    User userById = userDAO.getUserById(retrievedUser.id());
    assertNotNull(userById);
    assertEquals(retrievedUser.username(), userById.username());
    assertEquals(retrievedUser.password(), userById.password());
  }

  @Test
  void updateUser() {

}
  @Test
  void deleteUser() {
    User user = new User("testUser", "testPassword");
    userDAO.addUser(user);
    User retrievedUser = userDAO.getUserByLogin("testUser");
    assertNotNull(retrievedUser);
    userDAO.deleteUser(retrievedUser.id());
    User deletedUser = userDAO.getUserByLogin("testUser");
    assertNull(deletedUser);
  }
}
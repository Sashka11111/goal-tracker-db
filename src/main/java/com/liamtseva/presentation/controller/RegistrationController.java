package com.liamtseva.presentation.controller;

import com.liamtseva.domain.PasswordHashing;
import com.liamtseva.domain.validation.UserValidator;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl; // Імплементація UserRepository
import com.liamtseva.presentation.animation.Shake;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class RegistrationController {

  @FXML
  private Button SignInButton;

  @FXML
  private Button authSignInButton;

  @FXML
  private TextField login_field;

  @FXML
  private PasswordField password_field;

  @FXML
  private Button btn_close;

  @FXML
  private Label errorMessageLabel;

  @FXML
  private ImageView profileImageView;

  private String selectedProfileImagePath;

  private UserRepository userRepository;
  private byte[] imageBytes;

  public RegistrationController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
    // Завантажити зображення за замовчуванням
    imageBytes = loadDefaultImageBytes();
  }

  private byte[] readImageToBytes(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] data = new byte[(int) file.length()];
      fis.read(data);
      return data;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private byte[] loadDefaultImageBytes() {
    try (InputStream is = getClass().getResourceAsStream("/data/profile.png")) {
      if (is == null) {
        throw new IOException("Default profile image not found");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @FXML
  void chooseImageButtonClicked() {
    chooseImage();
  }

  public void chooseImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Profile Image");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
    );
    File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
    if (selectedFile != null) {
      selectedProfileImagePath = selectedFile.getPath();
      Image image = new Image(selectedFile.toURI().toString());
      profileImageView.setImage(image);
      imageBytes = readImageToBytes(selectedFile); // Зчитати зображення в масив байтів
    } else {
      profileImageView.setImage(new Image(getClass().getResourceAsStream("/data/profile.png")));
      imageBytes = null; // Зображення не вибрано
    }
  }

  @FXML
  void initialize() {
    btn_close.setOnAction(event -> {
      System.exit(0);
    });
    authSignInButton.setOnAction(event -> {
      Scene currentScene = authSignInButton.getScene();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
      try {
        Parent root = loader.load();
        currentScene.setRoot(root);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    SignInButton.setOnAction(event -> {
      String username = login_field.getText();
      String password = password_field.getText();
      if (username.isEmpty() || password.isEmpty()) {
        errorMessageLabel.setText("Логін та пароль не повинен бути пустим");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
        return;
      }
      if (UserValidator.isUsernameValid(username) && UserValidator.isPasswordValid(password)) {
        if (!userRepository.isUsernameExists(username)) {
          // Хешування пароля
          String hashedPassword = PasswordHashing.getInstance().hashedPassword(password);

          // Створення нового користувача
          User user = new User(0, username, hashedPassword, imageBytes);
          // Додавання користувача до бази даних через UserRepository
          userRepository.addUser(user);

          Scene currentScene = authSignInButton.getScene();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
          try {
            Parent root = loader.load();
            currentScene.setRoot(root);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          errorMessageLabel.setText("Логін з ім'ям " + username + " уже існує");
          Shake userLoginAnim = new Shake(login_field);
          userLoginAnim.playAnim();
        }
      } else {
        errorMessageLabel.setText("Пароль має мати велику, маленьку букву та цифру.\n" + "Мінімальна довжина паролю: 6 символів");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    });
  }
}

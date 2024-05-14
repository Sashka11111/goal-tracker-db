package com.liamtseva.presentation.controller;

import com.liamtseva.domain.validation.UserValidator;
import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl; // Імплементація UserRepository
import com.liamtseva.presentation.animation.Shake;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.stage.Stage;

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
  private Label errorMessageLabel;

  @FXML
  private ImageView profileImageView;

  private String selectedProfileImagePath;

  private UserRepository userRepository;
  private byte[] imageBytes;

  public RegistrationController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  public void initUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
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

      try {
        Path imagePath = Paths.get(selectedProfileImagePath);
        imageBytes = Files.readAllBytes(imagePath);

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
            User user = new User(username, password, imageBytes);
            userRepository.addUser(user);

            System.out.println("Registration successful.");
            SignInButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Трекер особистих цілей");
            try {
              stage.setScene(new Scene(loader.load()));
              stage.show();
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            errorMessageLabel.setText("Логін з ім'ям " + username + " уже існує");
            Shake userLoginAnim = new Shake(login_field);
            userLoginAnim.playAnim();
          }
        } else {
          errorMessageLabel.setText("Пароль має мати велику, маленьку букву та цифру");
          Shake userLoginAnim = new Shake(login_field);
          Shake userPassAnim = new Shake(password_field);
          userLoginAnim.playAnim();
          userPassAnim.playAnim();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      profileImageView.setImage(new Image(getClass().getResourceAsStream("/data/profile.png")));
    }
  }

  @FXML
  void initialize() {
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
      // Implementation of SignInButton action moved to chooseImage method
    });

    // Логіка кнопки реєстрації користувача
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
          // Створення нового користувача
          User user = new User(username, password,imageBytes);
          // Додавання користувача до бази даних через UserRepository
          userRepository.addUser(user);

          System.out.println("Registration successful.");
          SignInButton.getScene().getWindow().hide();

          // Перехід до головного меню
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
          Stage stage = new Stage();
          stage.setTitle("Трекер особистих цілей");
          try {
            stage.setScene(new Scene(loader.load()));
            stage.show();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          errorMessageLabel.setText("Логін з ім'ям " + username + " уже існує");
          Shake userLoginAnim = new Shake(login_field);
          userLoginAnim.playAnim();
        }
      } else {
        errorMessageLabel.setText("Пароль має мати велику, маленьку букву та цифру");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    });
  }
}
package com.liamtseva.presentation.controller;

import com.liamtseva.domain.exception.UserValidator;
import com.liamtseva.persistence.dao.UserDAO;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.presentation.animation.Shake;
import java.io.File;
import java.io.IOException;
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
      selectedProfileImagePath = selectedFile.getPath(); // Збереження шляху до обраного зображення
      Image image = new Image(selectedFile.toURI().toString());
      profileImageView.setImage(image);
    } else {
      // Set default image if no image is selected
      profileImageView.setImage(new Image(getClass().getResourceAsStream("/data/profile.png")));
    }
  }

  @FXML
  void initialize() {
    authSignInButton.setOnAction(event -> {
      // Отримуємо сцену з кнопки
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
        if (!UserValidator.isUsernameExists(username)) {
          // Створюємо об'єкт користувача
          User user = new User(username, password);
          // Створюємо об'єкт класу UserDAO
          UserDAO userDAO = new UserDAO();

          // Додаємо користувача до бази даних
          userDAO.addUser(user);

          System.out.println("Registration successful.");
          SignInButton.getScene().getWindow().hide();

          // Завантажуємо головне меню
          FXMLLoader loader = new FXMLLoader(
              getClass().getResource("/view/mainMenu.fxml"));
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
          // Відображення повідомлення про помилку та застосування анімації Shake
          Shake userLoginAnim = new Shake(login_field);
          userLoginAnim.playAnim();
        }
      } else {
        // Відображення повідомлення про помилку та застосування анімації Shake
        errorMessageLabel.setText("Пароль має мати велику, маленьку букву та цифру");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    });
  }
}

package com.liamtseva.goals;

import com.liamtseva.goals.animation.Shake;
import com.liamtseva.goals.entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorizationController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button authSignInButton;

  @FXML
  private Button loginSingUpButton;

  @FXML
  private TextField login_field;

  @FXML
  private PasswordField password_field;
  @FXML
  private Label errorMessageLabel;

  @FXML
  void initialize() {
    loginSingUpButton.setOnAction(event ->{
      // Отримуємо сцену з кнопки
      Scene currentScene = loginSingUpButton.getScene();
      // Завантажуємо нову сцену з файлу registration.fxml
      FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
      try {
        Parent root = loader.load();
        // Встановлюємо нову сцену на поточному вікні
        currentScene.setRoot(root);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    authSignInButton.setOnAction(event ->{
      String loginText = login_field.getText().trim();
      String loginPassword = password_field.getText().trim();

      if (!loginText.equals("") && !loginPassword.equals(""))
        loginUser(loginText, loginPassword);
      else
        errorMessageLabel.setText("Логін та пароль не повинен бути пустим");
      Shake userLoginAnim = new Shake(login_field);
      Shake userPassAnim = new Shake(password_field);
      userLoginAnim.playAnim();
      userPassAnim.playAnim();
    });
  }

  private void loginUser(String loginText, String loginPassword) {
    DatabaseHandler dbHandler = new DatabaseHandler();
    User user = new User();
    user.setUsername(loginText);
    user.setPassword(loginPassword);

    // Отримуємо зв'язок з базою даних
    Connection connection = dbHandler.connect();
    if (connection != null) {
      // Виконуємо запит до бази даних
      boolean loggedIn = dbHandler.loginUser(user.getUsername(), user.getPassword());
      if (loggedIn) {
        System.out.println("Login successful");
        loginSingUpButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        try {
          loader.load();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Трекер особистих цілей");
        stage.setScene(new Scene(root));
        stage.showAndWait();
      } else {
        errorMessageLabel.setText("Невірний логін або пароль");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    } else {
      System.out.println("Failed to connect to the database.");
    }
  }
}
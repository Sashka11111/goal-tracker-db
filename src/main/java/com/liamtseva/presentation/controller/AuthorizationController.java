package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.dao.UserDAO;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.presentation.animation.Shake;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
       try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration.fxml"));
         Parent root = loader.load();
         Scene newScene = new Scene(root);
// Встановлюємо нову сцену на поточному вікні
         Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         primaryStage.setScene(newScene);
         primaryStage.show();

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    authSignInButton.setOnAction(event ->{
      String loginText = login_field.getText().trim();
      String loginPassword = password_field.getText().trim();

      if (!loginText.equals("") && !loginPassword.equals("")) {
        // Перевірка логіну та пароля користувача
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByLogin(loginText);
        if (user != null && user.password().equals(loginPassword)) {
          loginSingUpButton.getScene().getWindow().hide();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainMenu.fxml"));
          Parent root;
          try {
            root = loader.load();
          } catch (IOException e) {
            e.printStackTrace();
            return;
          }

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
        errorMessageLabel.setText("Логін та пароль не повинен бути пустим");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    });
  }
}

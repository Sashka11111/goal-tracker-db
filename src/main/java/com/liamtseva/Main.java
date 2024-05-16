package com.liamtseva;

import com.liamtseva.persistence.config.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

  private static DatabaseConnection databaseConnection;

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("Трекер особистих цілей");
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    // Закриття пулу з'єднань під час зупинки додатка
    if (databaseConnection != null) {
      databaseConnection.closePool();
    }
  }

  public static void main(String[] args) {
    databaseConnection = DatabaseConnection.getInstance();
    try {
      databaseConnection.initializeDataSource();
      launch(args);
    } finally {
      // При завершенні роботи додатка також закриємо пул
      if (databaseConnection != null) {
        databaseConnection.closePool();
      }
    }
  }
}

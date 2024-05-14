package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.config.DatabaseConnection;
import com.liamtseva.persistence.entity.Tips;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.contract.TipsRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.persistence.repository.impl.TipsRepositoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TipsController implements Initializable {

  @FXML
  private ListView<String> tipsListView;

  private final TipsRepository tipsRepository;

  public TipsController() {
    this.tipsRepository = new TipsRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення CategoryRepositoryImpl з DatabaseConnection
  }
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Отримання списку порад з репозиторію
    List<Tips> tipsList = tipsRepository.getAllTips();

    // Очищення списку, щоб уникнути дублювання елементів при оновленні
    tipsListView.getItems().clear();

    // Додавання кожної поради до ListView
    for (Tips tip : tipsList) {
      tipsListView.getItems().add(tip.tipsText());
    }
  }
}

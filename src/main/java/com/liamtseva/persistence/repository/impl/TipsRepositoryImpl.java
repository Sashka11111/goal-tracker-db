package com.liamtseva.persistence.repository.impl;

import com.liamtseva.persistence.entity.Tips;
import com.liamtseva.persistence.repository.contract.TipsRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class TipsRepositoryImpl implements TipsRepository {

  private DataSource dataSource;

  public TipsRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Tips> getAllTips() {
    List<Tips> tips = new ArrayList<>();
    String query = "SELECT * FROM Tips";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        Tips tip = mapTip(resultSet);
        tips.add(tip);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tips;
  }
  private Tips mapTip(ResultSet resultSet) throws SQLException {
    return new Tips(
        resultSet.getInt("id_tip"),
        resultSet.getString("tip_text")
    );
  }
}

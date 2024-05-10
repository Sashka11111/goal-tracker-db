package com.liamtseva.presentation.viewmodel;

import javafx.beans.property.*;

public class CategoryViewModel {

  private final IntegerProperty id = new SimpleIntegerProperty();
  private final StringProperty name = new SimpleStringProperty();

  public CategoryViewModel() {
    // Порожній конструктор
  }

  public CategoryViewModel(int id, String name) {
    this.id.set(id);
    this.name.set(name);
  }

  // Геттери та сеттери для id
  public int getId() {
    return id.get();
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  // Геттери та сеттери для name
  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  @Override
  public String toString() {
    return "CategoryViewModel{" +
        "id=" + id +
        ", name=" + name +
        '}';
  }
}

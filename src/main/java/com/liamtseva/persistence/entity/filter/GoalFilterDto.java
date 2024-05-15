package com.liamtseva.persistence.entity.filter;

public class GoalFilterDto {
  private String status;

  public GoalFilterDto(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "GoalFilterDto{" +
        "status='" + status + '\'' +
        '}';
  }
}

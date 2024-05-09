package com.liamtseva.goals.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
  private TranslateTransition tt;
  public  Shake(Node node){
    tt = new TranslateTransition(Duration.millis(70),node);
    tt.setFromX(0f);
    tt.setByX(10f);
    tt.setCycleCount(3);
    tt.setAutoReverse(true);
    tt.setOnFinished(event -> node.setTranslateX(0));
  }
  public void playAnim(){
    tt.playFromStart();
  }
}

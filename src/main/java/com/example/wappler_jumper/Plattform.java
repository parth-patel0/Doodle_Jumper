package com.example.wappler_jumper;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Plattform extends Pane {
    private int xPos;
    private int yPos;
    private int len;
    private int height;

    public Plattform(int xPos, int yPos, int len, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.len = len;
        this.height = height;

        Rectangle rect = new Rectangle(len, height);
        rect.setX(xPos);
        rect.setY(yPos);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setFill(Color.LIGHTBLUE);

        getChildren().add(rect);
    }
}

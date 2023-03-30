package com.example.wappler_jumper;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class main_plattform {
    private int xPos;
    private int yPos;
    private int len;
    private int height;

    public main_plattform(int xPos, int yPos, int len, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.len = len;
        this.height = height;

        Rectangle plattform = new Rectangle(xPos, yPos);
        plattform.setArcHeight(height);
        plattform.setArcWidth(len);
        plattform.setFill(Color.ANTIQUEWHITE);

    }
}

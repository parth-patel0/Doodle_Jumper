package com.example.wappler_jumper;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Platform extends Pane {
    private int xPos;
    private int yPos;
    private int len;
    private int height;

    public Platform(int xPos, int yPos, int len, int height) {
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

    /**
     * gets the xPos of the plattform
     * @return the xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * gets the yPos of the plattform
     * @return the yPos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * gets the length of the platform
     * @return the length
     */
    public int getLen() {
        return len;
    }

    /**
     * gets the height of the platform
     * @return the height
     */
    public int getHeigt() {
        return height;
    }
}

package com.example.wappler_jumper;

public class MovingPlatform extends Platform {

    private double v;
    public MovingPlatform(int xPos, int yPos, int len, int height, double v) {
        super(xPos, yPos, len, height);
        this.v = v;
    }


}

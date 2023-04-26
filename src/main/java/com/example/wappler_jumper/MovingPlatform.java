package com.example.wappler_jumper;

public class MovingPlatform extends Platform {

    private int v;
    private int s = 30;
    private boolean movingRight = true;

    public MovingPlatform(int xPos, int yPos, int len, int height, int v) {
        super(xPos, yPos, len, height);
        this.v = v;
        move();
    }

    /**
     * this method lets the MovingPlatform objects move form left to the center to right and back
     * total 40px
     * velocity 1 (slow)
     */
    public void move () {
        //checks if the platform is moving right
        if (movingRight) {
            //change the xPos by the velocity
            setTranslateX(getTranslateX() + v);
            if (getTranslateX() > s) {
                movingRight = false;
            }
        }
        //checks if the platfrom is moving left
        else {
            //change the xPos by the -velocity
            setTranslateX(getTranslateX() - v);
            if (getTranslateX() < -s) {
                movingRight = true;
            }
        }
    }

}

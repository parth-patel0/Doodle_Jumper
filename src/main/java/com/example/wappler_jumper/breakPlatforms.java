package com.example.wappler_jumper;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class breakPlatforms extends Platform {
    private boolean broken;
    int breakduration;

    public breakPlatforms(int xPos, int yPos, int len, int height, int breakDuration) {
        super(xPos, yPos, len, height);
        broken = false;
        this.breakduration = breakDuration;
        //breakAfterDuration(breakDuration);
    }

    /**
     * Check if the platform is broken.
     *
     * @return true if the platform is broken, false otherwise.
     */
    public boolean isBroken() {
        return broken;
    }

    public int getBreakduration() {
        return breakduration;
    }

    public void setBreakduration(int breakduration) {
        this.breakduration = breakduration;
    }
}

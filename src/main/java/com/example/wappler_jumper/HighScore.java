package com.example.wappler_jumper;

import javafx.scene.control.TextField;

public class HighScore extends main_class {
    private double ingamescorecounter;
    private double highscorecounter = 0;
    private TextField ingamescore = new TextField();
    private TextField highscore = new TextField();

    public double getIngamescorecounter() {
        return ingamescorecounter;
    }

    public void setIngamescorecounter(double ingamescorecounter) {
        this.ingamescorecounter = ingamescorecounter;
    }

    public double getHighscorecounter() {
        return highscorecounter;
    }

    public void setHighscorecounter(double highscorecounter) {
        this.highscorecounter = highscorecounter;
    }

    public TextField getIngamescore() {
        return ingamescore;
    }

    public void setIngamescore(TextField ingamescore) {
        this.ingamescore = ingamescore;
    }

    public TextField getHighscore() {
        return highscore;
    }

    public void setHighscore(TextField highscore) {
        this.highscore = highscore;
    }

    public HighScore() {
    }
}

package com.example.wappler_jumper;

import javafx.scene.control.Slider;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Music {
    private Clip clip;
    private Slider slider = new Slider(0, 6.0206, 3);
    private FloatControl gainControl;

    public Music(){

    }

    public Music(Clip clip, Slider slider, FloatControl gainControl) {
        this.clip = clip;
        this.slider = slider;
        this.gainControl = gainControl;
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    public FloatControl getGainControl() {
        return gainControl;
    }

    public void setGainControl(FloatControl gainControl) {
        this.gainControl = gainControl;
    }

    public void playMusic(String file) {
        try {
            File musicpath = new File(file);
            if (musicpath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue((float) slider.getValue());

            } else {
                System.out.println("No music you stupid n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.wappler_jumper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Duration;

public class main_class extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Welcome to Wappler Jumper!");
        /*
        * TODO
        *  main-start Fenster
        *       * title "Wappler Jumper"
        *       * play Button
        * */

        //basic alignment
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(-200, 0, 0, 0));
        menu.setSpacing(200);

        //title + curve style
        Label title = new Label("Wappler Jumper");
        title.setStyle("-fx-text-fill: #000000; " +
                "-fx-font-size: 40px; " +
                "-fx-font-weight: bold; " +
                "-fx-font-family: 'Ink Free', Tahoma, Geneva, Verdana, sans-serif; " +
                "-fx-pref-width: 350px; " +
                "-fx-pref-height: 60px; " +
                "-fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 44));

        //play button + style
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-background-color: #000000; " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-size: 24px; " +
                "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " +
                "-fx-pref-width: 120px; " +
                "-fx-pref-height: 60px; " +
                "-fx-background-radius: 30; " +
                "-fx-border-radius: 30; " +
                "-fx-border-color: #FFFFFF; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand;");
        playButton.setOnAction(actionEvent -> {
            stage.close();
            startGame();
        });

        menu.getChildren().addAll(title, playButton);

        Scene mainMenu = new Scene(menu, 700, 700);
        stage.setScene(mainMenu);
        stage.show();
    }

    private void startGame() {
        Stage map = new Stage();
        map.setTitle("Wappler-Jumper");
        VBox box = new VBox();
        Scene playScene = new Scene(box, 700, 700);
        map.setScene(playScene);
        for (int i = 0; i < 10; i++) {

        }

        map.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
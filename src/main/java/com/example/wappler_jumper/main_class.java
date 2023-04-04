package com.example.wappler_jumper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class main_class extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Welcome to Wappler Jumper!");
        /*
        * TODO
        *   Plattformen haben von sich selber ein Min Abstand von 50 Pixel
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
        Pane pane = new Pane();
        Random randomPos = new Random();

        for (int i = 0; i < 50; i++) {
            int xPos = randomPos.nextInt(700);
            int yPos = randomPos.nextInt(700);

            Plattform plattform = new Plattform(xPos, yPos, 80, 20);
            pane.getChildren().add(plattform);
        }

        Scene playScene = new Scene(pane, 700, 700);
        map.setScene(playScene);
        map.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
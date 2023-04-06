package com.example.wappler_jumper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class main_class extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Welcome to Ninja Jumper!");
        /*
        * TODO
        *   spieler einfügen mit funktionen
        *   plattformen so programmieren, dass wenn man springt, man auf den plattformen stehen bleibt
        *   spezielle plattformen die von der Plattform klasse erben
        * */

        //basic alignment
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(-200, 0, 0, 0));
        menu.setSpacing(200);

        //title + curve style
        Label title = new Label("Ninja Jumper");
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
        map.setTitle("Ninja-Jumper");

        //creating random Number for the xPos of the platform
        Random randomPos = new Random();
        VBox allPlatforms = new VBox();
        VBox.setVgrow(allPlatforms, Priority.ALWAYS);
        //top part of vbox for the score and settings
        HBox score = new HBox();
        score.setPrefHeight(80);
        score.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        allPlatforms.getChildren().add(score);

        //creating random platforms with x and y positions
        for (int i = 0; i < 7; i++) {
            Pane container = new Pane();

            container.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

            container.setPrefHeight(100);
            container.setPrefWidth(700);
            allPlatforms.getChildren().add(container);
            container.setLayoutX(0);
            container.setLayoutY(0);
            int max = 610, min = 10;
            //creating 2 platforms on each pane
            for (int j = 0; j < 2; j++) {
                int xPos = randomPos.nextInt(max - min + 1) + min;
                int yPos = randomPos.nextInt(10, 70);
                Platform platform = new Platform(xPos, yPos, 80, 20);
                container.getChildren().add(platform);
                max = (xPos + 100 > 545 ? 388 : 610);
                min = (xPos + 100 < 498 ? 433 : 10);
            }

        }

        //create scene
        Scene playScene = new Scene(allPlatforms, 700, 700);
        map.setScene(playScene);
        map.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
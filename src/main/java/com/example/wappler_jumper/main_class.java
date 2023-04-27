package com.example.wappler_jumper;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main_class extends Application {
    //Güler
    Pane gamePane = new Pane();
    List<MovingPlatform> randomPlatforms = new ArrayList<>();
    List<Platform> fixPlatforms = new ArrayList<>();
    Scene playScene = new Scene(gamePane, 700, 700);
    //Bilder
    Image ninjaN = new Image("ninja_jumper_normal.png");
    Image ninjanL = new Image("ninja_jumper_links.png");
    Image ninjaR = new Image("ninja_jumper_rechts.png");
     ImageView ninjanormal = new ImageView(ninjaN);
    ImageView ninjalinks = new ImageView(ninjanL);
    ImageView ninjarechts = new ImageView(ninjaR);
    int x = 125;
    int y = 540;
    private  double xVel = 0;
    private  double yVel = 0;
    private  int jumpingpoint;
    private  boolean executed = false;
    private  double gravity = 0.5;
    private  int counter = 0;
    private  boolean jumping = true;
    private  boolean falling;
    private  boolean dead = false;
    private  boolean pause = false;
    //Güler

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
            //startGame(); --> first version with several panes
            play();
        });

        menu.getChildren().addAll(title, playButton);

        Scene mainMenu = new Scene(menu, 700, 700);
        stage.setScene(mainMenu);
        stage.show();
    }

    private void play() {
        //Güler
        Stage map = new Stage();
        map.setTitle("Ninja-Jumper");
        //ninjanormal.isVisible();
        ninjalinks.setVisible(false);
        ninjarechts.setVisible(false);
        boolean playerdead = false;

        //In-game Menü
        pause = false;
        Image imgeinstellungsmenu = new Image("Einstellungsmenu.png");
        ImageView einstellungsmenu = new ImageView(imgeinstellungsmenu);
        einstellungsmenu.relocate(630, 20);
        einstellungsmenu.setOnKeyPressed(e -> {

            if (e.getCode() == KeyCode.P) {
                ingamemenu();
                map.close();
                pause = true;
            }
        });

        einstellungsmenu.setOnMouseClicked(e -> {
            ingamemenu();
            map.close();
            pause = true;
        });
        //Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                //yVel += gravity;
                if (y > 700) {
                    dead = true;
                } else dead = false;
                if (dead) {
                    //gameover
                    System.out.println("Gameover");
                }
                x += xVel;
                y += yVel;
                ninjanormal.relocate(x, y);
            }
        };
        timer.start();
        //Güler

        //creating random Number for the xPos of the platform
        Random randomPos = new Random();
        int numPlatforms = 13;


        //top part of vbox for the score and settings
        HBox score = new HBox();
        score.setPrefHeight(80);
        score.setPrefWidth(700);
        score.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        score.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        gamePane.getChildren().addAll(score, ninjanormal, ninjalinks, ninjarechts, einstellungsmenu);

        //creating fix platforms
        Platform fixP1 = new Platform(150, 620, 80, 20);
        fixPlatforms.add(fixP1);
        Platform fixP2 = new Platform(310, 500, 80, 20);
        fixPlatforms.add(fixP2);
        Platform fixP3 = new Platform(450, 390, 80, 20);
        fixPlatforms.add(fixP3);
        Platform fixP4 = new Platform(136, 280, 80, 20);
        fixPlatforms.add(fixP4);
        Platform fixP5 = new Platform(330, 170, 80, 20);
        fixPlatforms.add(fixP5);

        //creating random platforms
        for (int i = 0; i < fixPlatforms.size(); i++) {
            //random x & y pos of the platform + lenght & height
            int xPosFix = fixPlatforms.get(i).getxPos();
            int xPos;
            if (xPosFix < 350) {
                xPos = randomPos.nextInt(621 - xPosFix - 100 + 1) + xPosFix + 100; //random xPos from xPosFix + 100 to 621
            } else {
                xPos = randomPos.nextInt(xPosFix - 50 - 100 + 1) + 50; //random xPos - 100 from 50 to xPosFix
            }
            int yPos = fixPlatforms.get(i).getyPos() - 30;
            int lenght = 80;
            int height = 20;
            //int dir = (i % 2 == 0 ? 1 : -1);
            MovingPlatform newPlatform = new MovingPlatform(xPos, yPos, lenght, height, 1);
            newPlatform.move();
            randomPlatforms.add(newPlatform);

        }

        //adding fix and random platforms in the pane
        gamePane.getChildren().addAll(fixPlatforms);
        gamePane.getChildren().addAll(randomPlatforms);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            // Update the platforms by calling the move method
            for (MovingPlatform movingPlatform : randomPlatforms) {
                movingPlatform.move();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // create scene
        map.setScene(playScene);
        map.show();

    }

    private void update() {
        playScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> {
                    /*ninjalinks.isVisible();
                    ninjanormal.setVisible(false);
                    ninjarechts.setVisible(false);*/
                        /*ninjalinks.relocate(x[0] - 10, y[0]);
                        ninjarechts.relocate(x[0] - 10, y[0]);
                        ninjanormal.relocate(x[0] - 10, y[0]);
                        x[0] = x[0] - 10;*/
                    xVel = -3.5;
                }
                case D -> {
                    /*ninjarechts.isVisible();
                    ninjanormal.setVisible(false);
                    ninjalinks.setVisible(false);*/
                        /*ninjarechts.relocate(x[0] + 10, y[0]);
                        ninjalinks.relocate(x[0] + 10, y[0]);
                        ninjanormal.relocate(x[0] + 10, y[0]);
                        x[0] = x[0] + 10;*/
                    xVel = 3.5;
                }
                case SPACE -> {
                    //if (!executed){
                    //    jumpingpoint = y[0];
                    //executed = true;
                    //}

                    //jump();
                    /*ninjanormal.relocate(x[0], y[0] - 10);
                    ninjalinks.relocate(x[0], y[0] - 10);
                    ninjarechts.relocate(x[0], y[0] - 10);

                    y[0] = y[0] - 10;*/
                    //yVel = -10;

                    /*if (jumping){
                        yVel = -10;
                    }*/

                    if (!executed) {
                        executed = true;
                        jumpingpoint = y;
                        yVel = -3.5;
                    }
                }
            }
        });
        playScene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> {
                    /*ninjalinks.isVisible();
                    ninjanormal.setVisible(false);
                    ninjarechts.setVisible(false);*/
                        /*ninjalinks.relocate(x[0] - 10, y[0]);
                        ninjarechts.relocate(x[0] - 10, y[0]);
                        ninjanormal.relocate(x[0] - 10, y[0]);
                        x[0] = x[0] - 10;*/
                    xVel = 0;
                }
                case D -> {
                    /*ninjarechts.isVisible();
                    ninjanormal.setVisible(false);
                    ninjalinks.setVisible(false);*/
                        /*ninjarechts.relocate(x[0] + 10, y[0]);
                        ninjalinks.relocate(x[0] + 10, y[0]);
                        ninjanormal.relocate(x[0] + 10, y[0]);
                        x[0] = x[0] + 10;*/
                    xVel = 0;
                }
                case SPACE -> {
                    //jump();
                    /*ninjanormal.relocate(x[0], y[0] - 10);
                    ninjalinks.relocate(x[0], y[0] - 10);
                    ninjarechts.relocate(x[0], y[0] - 10);

                    //if (!(jumpingpoint <= y[0])) {
                    //yVel = 10;
                    //} else {
                    //  yVel = 0;
                    //}

                    //canjump = false;
                     */

                    /*if (counter != 0){
                        counter--;
                        yVel = 10;
                    }else yVel = 0;*/

                    if (jumpingpoint <= y) {
                        yVel = 0;
                    } else {
                        yVel = 3.5;
                    }

                    //yVel = Math.min(jumpingpoint - y[0], 0);
                    executed = false;
                }
            }
        });
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

    private void ingamemenu() {
        Stage stage = new Stage();
        stage.setTitle("Einstellungsmenü");

        //basic alignment
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(-200, 0, 0, 0));
        menu.setSpacing(200);

        //main menu button + style + setOnMouseClicked
        Button retmainmenu = new Button("Main Menü");
        retmainmenu.setStyle("-fx-background-color: #000000; " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-font-size: 24px; " +
                "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " +
                "-fx-pref-width: 200px; " +
                "-fx-pref-height: 60px; " +
                "-fx-background-radius: 30; " +
                "-fx-border-radius: 30; " +
                "-fx-border-color: #FFFFFF; " +
                "-fx-border-width: 2px; " +
                "-fx-cursor: hand;");
        menu.getChildren().addAll(retmainmenu);
        retmainmenu.setOnMouseClicked(e->{
            Stage stage1 = new Stage();
            try {
                start(stage1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        //Resume Button + style + setOnMouseClicked
        Button resume = new Button("Resume");
        resume.setStyle("-fx-background-color: #000000; " +
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
        menu.getChildren().addAll(resume);
        resume.setOnMouseClicked(e->{
            play();
            stage.close();
        });

        Scene mainMenu = new Scene(menu, 700, 700);
        stage.setScene(mainMenu);
        stage.show();
    }
}
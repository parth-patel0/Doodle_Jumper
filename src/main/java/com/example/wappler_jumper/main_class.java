package com.example.wappler_jumper;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
/*import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;*/

public class main_class extends Application {
    //Güler
    Pane gamePane = new Pane();
    List<OPPlatform> randomPlatforms = new ArrayList<>();
    List<Platform> fixPlatforms = new ArrayList<>();

    Scene playScene = new Scene(gamePane, 700, 700);
    //Bilder
    Image ninjaN = new Image("ninja_jumper_normal.png");
    ImageView ninjanormal = new ImageView(ninjaN);
    int x = 125;
    int y = 540;
    private double xVel = 0;
    private double yVel = 0;
    private int jumpingpoint;
    private boolean executed = false;
    private double gravity = 1;
    private int counter = 0;
    private boolean jumping;
    private boolean falling;
    private boolean dead = false;
    private boolean pause = false;
    private Stage map = new Stage();
    private Label player = new Label();
    private Button einstellungsmenu = new Button("Pause");
    private HBox score = new HBox();

    //Güler

    @Override
    public void start(Stage stage) throws IOException {
        List<Platform> rects = gamePane.getChildren().stream().filter(p -> p instanceof Platform).map(p -> (Platform) p).toList();
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
        title.setStyle("-fx-text-fill: #000000; " + "-fx-font-size: 40px; " + "-fx-font-weight: bold; " + "-fx-font-family: 'Ink Free', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 350px; " + "-fx-pref-height: 60px; " + "-fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 44));

        //play button + style
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 120px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        playButton.setOnAction(actionEvent -> {
            stage.close();
            //startGame(); --> first version with several panes
            try {
                play();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        menu.getChildren().addAll(title, playButton);

        Scene mainMenu = new Scene(menu, 700, 700);
        stage.setScene(mainMenu);
        stage.show();
    }

    private void initGame() {
        map.setTitle("Ninja-Jumper");

        //Player
        player.setGraphic(ninjanormal);

        //In-game Menü
        //pause = false;
        einstellungsmenu.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 12px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 60px; " + "-fx-pref-height: 30px; " + "-fx-background-radius: 15; " + "-fx-border-radius: 15; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 1px; " + "-fx-cursor: hand;");
        einstellungsmenu.relocate(630, 20);

        //top part of vbox for the score and settings
        score.setPrefHeight(80);
        score.setPrefWidth(700);
        score.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        score.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

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

        //creating random Number for the xPos of the platform
        Random randomPos = new Random();
        int numPlatforms = 13;

        //creating random platforms
        for (Platform fixPlatform : fixPlatforms) {
            //random x & y pos of the platform + length & height
            int xPosFix = fixPlatform.getxPos();
            int xPos;
            if (xPosFix < 350) {
                xPos = randomPos.nextInt(621 - xPosFix - 100 + 1) + xPosFix + 100; //random xPos from xPosFix + 100 to 621
            } else {
                xPos = randomPos.nextInt(xPosFix - 50 - 100 + 1) + 50; //random xPos - 100 from 50 to xPosFix
            }
            int yPos = fixPlatform.getyPos() - 30;
            int length = 80;
            int height = 20;
            //int dir = (i % 2 == 0 ? 1 : -1);
            OPPlatform newPlatform = new OPPlatform(xPos, yPos, length, height, 1);
            newPlatform.move();
            randomPlatforms.add(newPlatform);
        }


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            // Update the platforms by calling the move method
            for (OPPlatform movingPlatform : randomPlatforms) {
                movingPlatform.move();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        gamePane.getChildren().addAll(score, player, einstellungsmenu);
        //adding fix and random platforms in the pane
        gamePane.getChildren().addAll(fixPlatforms);
        gamePane.getChildren().addAll(randomPlatforms);

        // create scene
        map.setScene(playScene);
    }

    private void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //Güler
        if (!pause) {
            initGame();
        }
        //In-game Menü
        einstellungsmenu.setOnMouseClicked(e -> {
            ingamemenu();
            map.close();
        });
        //Güler


        //Musik
        /*Media Sound = new Media(Paths.get("mine-diamonds-karaoke.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(Sound);
        mediaPlayer.play();*/

        //playMusic("mine-diamonds-karaoke.wav",0);
        /*AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\furka\\IdeaProjects\\Doodle_Jumper\\src\\main\\resources\\mine-diamonds-karaoke.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        //if (number == 0) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        //}
        clip.start();*/


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
                player.relocate(x, y);
                if (jumping) {
                    for (Platform fplatform : fixPlatforms) {
                        fplatform.setLayoutY((fplatform.getLayoutY() + 2.5));
                        if (fplatform.getyPos() + fplatform.getLayoutY() > playScene.getHeight()) {
                            //fplatform.setVisible(false);
                            //fplatform.setyPos(500);
                            fplatform.relocate(0, -200);
                        }
                    }
                    for (OPPlatform rplatform : randomPlatforms) {
                        rplatform.setLayoutY((rplatform.getLayoutY() + 2.5));
                        /*
                        * if (xPosFix < 350) {
                            xPos = randomPos.nextInt(621 - xPosFix - 100 + 1) + xPosFix + 100; //random xPos from xPosFix + 100 to 621
                          } else {
                              xPos = randomPos.nextInt(xPosFix - 50 - 100 + 1) + 50; //random xPos - 100 from 50 to xPosFix
                            }*/
                        if (rplatform.getyPos() + rplatform.getLayoutY() > playScene.getHeight()) {
                            //rplatform.setVisible(false);
                            //rplatform.setyPos(200);
                            /*int xPosFixForRandom = fixPlatforms.get(counter++).getxPos();
                            int xPosforRandom = 0;
                            if (xPosFixForRandom < 350) {
                                xPosforRandom = randomPos.nextInt(621 - xPosFixForRandom - 100 + 1) + xPosFixForRandom + 100; //random xPos from xPosFix + 100 to 621
                            } else {
                                xPosforRandom = randomPos.nextInt(xPosFixForRandom - 50 - 100 + 1) + 50; //random xPos - 100 from 50 to xPosFix
                            }*/
                            rplatform.relocate(0, -200);
                            //randomPlatforms.add(new OPPlatform((int) ninjanormal.getX() + 200, (int) (ninjanormal.getY() + 200), 80, 20, 1));
                        }
                    }
                }
            }
        };
        if (!pause) {
            timer.start();
        }
        map.show();
    }

    private void update() {
        checkCollisions();

        playScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> {
                    xVel = -2.5;
                }
                case D -> {
                    xVel = 2.5;
                }
                case W -> {
                    if (!executed) {
                        executed = true;
                        jumpingpoint = y;
                        yVel = -2.5;
                    }
                    jumping = true;
                }
            }
        });

        playScene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case A -> {
                    xVel = 0;
                }
                case D -> {
                    xVel = 0;
                }
                case W -> {
                    if (jumpingpoint <= y) {
                        yVel = 0;
                    } else {
                        yVel = 2.5;
                    }
                    //yVel = Math.min(jumpingpoint - y[0], 0);
                    executed = false;
                    jumping = false;
                }
            }
        });
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
        retmainmenu.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 200px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        menu.getChildren().addAll(retmainmenu);
        retmainmenu.setOnMouseClicked(e -> {
            Stage stage1 = new Stage();
            try {
                gamePane.getChildren().removeAll();
                pause = false;
                start(stage1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        //Resume Button + style + setOnMouseClicked
        Button resume = new Button("Resume");
        resume.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 120px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        menu.getChildren().addAll(resume);
        resume.setOnMouseClicked(e -> {
            try {
                pause = true;
                play();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        Scene mainMenu = new Scene(menu, 700, 700);
        stage.setScene(mainMenu);
        stage.show();
    }

    private void checkCollisions() {
        for (Platform platform : fixPlatforms) {
            if (collisionChecker(platform)) jumping = false;
            break;
        }
        for (Platform randomPlatform : randomPlatforms) {
            if (collisionChecker(randomPlatform)) jumping = false;
            break;
        }
        y += gravity;
    }

    private boolean collisionChecker(Platform platform) {
        if ((x > platform.getxPos() - 29 && x < platform.getxPos() + 80) && (y + 45 > platform.getyPos() && y + 45 < platform.getyPos() + 20)) {
            y = platform.getyPos() - 46;
            yVel = 0;
            return true;
        }
        return false;
    }

    public static void playMusic(String file, int number) {
        try {
            File musicPath = new File(file);

            if (musicPath.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                if (number == 0) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
                //musicClip = clip;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
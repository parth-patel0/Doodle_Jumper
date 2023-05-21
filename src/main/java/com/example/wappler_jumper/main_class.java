package com.example.wappler_jumper;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
/*import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;*/
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class main_class extends Application {
    //Güler
    Pane gamePane = new Pane();
    List<OPPlatform> randomPlatforms = new ArrayList<>();
    List<Platform> fixPlatforms = new ArrayList<>();
    Scene playScene = new Scene(gamePane, 700, 700);
    int x = 125;
    int y = 540;
    private double xVel = 0;
    private static double yVel = 0;
    private double gravity = 1;
    private boolean jumping;
    private boolean dead = false;
    private boolean pause = false;
    private final Stage map = new Stage();
    private final Label player = new Label();
    private final Button einstellungsmenu = new Button("Pause");
    private final HBox score = new HBox();
    private static final long jumpingtime = 250;
    private double ingamescorecounter;
    private double highscorecounter = 0;
    TextField ingamescore = new TextField();
    TextField highscore = new TextField();
    Clip clip;
    Slider slider = new Slider(-80, 6.0206, 4);
    FloatControl gainControl;
    boolean musicplaying;
    private boolean executed = false;
    private Circle blackhole;
    private int blackholecounter = 0;
    private Timeline timeline;
    private Label enemy = new Label();
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
        gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
        gamePane.getChildren().removeAll(fixPlatforms);
        gamePane.getChildren().removeAll(randomPlatforms);
        fixPlatforms.clear();
        randomPlatforms.clear();

        map.setTitle("Ninja-Jumper");

        //Player
        Image ninjaN = new Image("ninja_jumper_normal.png");
        ImageView ninjanormal = new ImageView(ninjaN);
        player.setGraphic(ninjanormal);

        //Gegner
        Image gegnerg = new Image("gegner.png");
        ImageView gegner = new ImageView(gegnerg);
        enemy.setGraphic(gegner);
        enemy.relocate(new Random().nextInt(0, 700), 500);

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
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            // Update the platforms by calling the move method
            for (OPPlatform movingPlatform : randomPlatforms) {
                movingPlatform.move();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //Hindernis (schwarzes loch)
        blackhole = new Circle(0, 400, 15, Color.BLACK);
        //blackhole.setCenterX(new Random().nextInt((int) (0 + blackhole.getRadius()), (int) (700 - blackhole.getRadius())) + 700 - blackhole.getRadius());
        //List<Circle> circles = gamePane.getChildren().stream().filter(p -> p instanceof Circle).map(p -> (Circle) p).toList();

        //score
        ingamescore.setText("Score: " + ingamescorecounter);
        ingamescore.relocate(10, 10);
        ingamescore.toFront();

        //highscore
        highscore.setText("Highscore: " + highscorecounter);
        highscore.relocate(200, 10);
        highscore.toFront();

        gamePane.getChildren().addAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
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
        if (!dead && !executed) {
            playMusic("src/main/resources/main-theme.aiff");
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue((float) slider.getValue());
            musicplaying = true;
        }
        slider.setOnMouseDragged(e -> {
            gainControl.setValue((float) slider.getValue());
        });
        slider.setOnMouseClicked(e -> {
            gainControl.setValue((float) slider.getValue());
        });
        //Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                //yVel += gravity;
                if (y > 700) {
                    dead = true;
                    y = 540;
                    x = 125;
                } else dead = false;
                if (dead) {
                    ingamescorecounter = 0;
                    gameover();
                    map.close();
                    this.stop();
                    dead = false;
                    gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
                    gamePane.getChildren().removeAll(fixPlatforms);
                    gamePane.getChildren().removeAll(randomPlatforms);
                }
                x += xVel;
                y += yVel;
                if (player.getBoundsInParent().intersects(blackhole.getBoundsInParent())) {
                    ingamescorecounter = 0;
                    gameover();
                    map.close();
                    this.stop();
                    dead = false;
                    gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
                    gamePane.getChildren().removeAll(fixPlatforms);
                    gamePane.getChildren().removeAll(randomPlatforms);
                }
                ingamescore.setText("Score: " + ingamescorecounter);
                if (ingamescorecounter > highscorecounter) {
                    highscorecounter = ingamescorecounter;
                    highscore.setText("Highscore: " + highscorecounter);
                }
                player.relocate(x, y);
                if (jumping) {
                    blackhole.setLayoutY(blackhole.getLayoutY() + Math.abs(yVel));
                    if (blackhole.getCenterY() + blackhole.getRadius() + blackhole.getLayoutY() > playScene.getHeight()) {
                        blackholecounter++;
                        if (blackholecounter >= jumpingtime * 2) {
                            blackholecounter = 0;
                            int random = new Random().nextInt(-225, 325);
                            if (!checkCollisionsplatforms(blackhole)) {
                                blackhole.relocate(random, -500);
                            } else
                                blackhole.relocate(random + 80 + 10/*witdh of the platform*/, -500 - 20 - 10/*height of the platform*/);
                        }
                    }
                    for (Platform fplatform : fixPlatforms) {
                        fplatform.setLayoutY((fplatform.getLayoutY() + Math.abs(yVel)));
                        if (fplatform.getyPos() + fplatform.getLayoutY() > playScene.getHeight()) {
                            int random = new Random().nextInt(-200, 100);
                            if (!checkCollisionsplatforms(fplatform)) {
                                fplatform.relocate(random, -500);
                            } else
                                fplatform.relocate(random + 80 + 10/*witdh of the platform*/, -500 - 20 - 10/*height of the platform*/);
                        }
                    }
                    for (OPPlatform rplatform : randomPlatforms) {
                        rplatform.setLayoutY((rplatform.getLayoutY() + Math.abs(yVel)));
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
                            int random = new Random().nextInt(-200, 0);
                            if (!checkCollisionsplatforms(rplatform)) {
                                rplatform.relocate(random, -500);
                            } else
                                rplatform.relocate(random + 80 + 10/*witdh of the platform*/, -500 - 20 - 10/*height of the platform*/);
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
                    xVel = -2;
                }
                case D -> {
                    xVel = 2;
                }
                case W -> {
                    new Thread(new thread()).start();
                    yVel = -2;
                    ingamescorecounter += Math.abs(yVel);
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
                    //yVel = 0;
                    yVel = 2;
                    jumping = false;
                }
            }
        });
    }

    public static class thread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(jumpingtime);
                yVel = 2;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void gameover() {
        Stage stage = new Stage();
        stage.setTitle("Gameover");
        //basic alignment
        VBox gameover = new VBox();
        gameover.setAlignment(Pos.CENTER);
        gameover.setPadding(new Insets(-200, 0, 0, 0));
        gameover.setSpacing(200);

        //main menu button + style + setOnMouseClicked
        Button retmainmenu = new Button("Main Menü");
        retmainmenu.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 200px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        gameover.getChildren().addAll(retmainmenu);
        retmainmenu.setOnMouseClicked(e -> {
            Stage stage1 = new Stage();
            try {
                clip.stop();
                ingamescorecounter = 0;
                gamePane.getChildren().removeAll(fixPlatforms);
                gamePane.getChildren().removeAll(randomPlatforms);
                gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
                pause = false;
                start(stage1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        //try again Button + style + setOnMouseClicked
        Button playAgain = new Button("Play again");
        playAgain.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 120px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        gameover.getChildren().addAll(playAgain);
        playAgain.setOnMouseClicked(e -> {
            try {
                ingamescorecounter = 0;
                gamePane.getChildren().removeAll(fixPlatforms);
                gamePane.getChildren().removeAll(randomPlatforms);
                gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
                pause = false;
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

        Scene gameoverMenu = new Scene(gameover, 700, 700);
        stage.setScene(gameoverMenu);
        stage.show();
    }

    private void ingamemenu() {
        Stage stage = new Stage();
        stage.setTitle("Einstellungsmenü");

        //basic alignment
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(-200, 0, 0, 0));
        menu.setSpacing(150);

        //main menu button + style + setOnMouseClicked
        Button retmainmenu = new Button("Main Menü");
        retmainmenu.setStyle("-fx-background-color: #000000; " + "-fx-text-fill: #FFFFFF; " + "-fx-font-size: 24px; " + "-fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " + "-fx-pref-width: 200px; " + "-fx-pref-height: 60px; " + "-fx-background-radius: 30; " + "-fx-border-radius: 30; " + "-fx-border-color: #FFFFFF; " + "-fx-border-width: 2px; " + "-fx-cursor: hand;");
        menu.getChildren().addAll(retmainmenu);
        retmainmenu.setOnMouseClicked(e -> {
            Stage stage1 = new Stage();
            try {
                clip.stop();
                ingamescorecounter = 0;
                gamePane.getChildren().removeAll(fixPlatforms);
                gamePane.getChildren().removeAll(randomPlatforms);
                gamePane.getChildren().removeAll(score, player, einstellungsmenu, ingamescore, highscore, blackhole, enemy);
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
                executed = true;
                pause = true;
                play();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });

        //Musikregler
        menu.getChildren().add(slider);

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

    public void playMusic(String file) {
        try {
            File musicpath = new File(file);
            if (musicpath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                //gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                //gainControl.setValue((float) slider.getValue());

            } else {
                System.out.println("No music you stupid n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCollisionsplatforms(Node node) {
        for (Platform platform : fixPlatforms) {
            if (platform.getBoundsInParent().intersects(node.getBoundsInParent())) {
                return true;
            }
        }
        for (Platform randomPlatform : randomPlatforms) {
            if (randomPlatform.getBoundsInParent().intersects(node.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }
    /*
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     */
}
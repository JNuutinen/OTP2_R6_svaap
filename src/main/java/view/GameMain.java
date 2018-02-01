package view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.util.ArrayList;

/*
    Inspoo:
    https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

    Testausta, sovellettu ylläolevast linkist. Ei käytä controlleria eikä GameGraphicsia.
 */
public class GameMain extends Application {
    private ArrayList<String> input;
    private GameGraphics gameGraphics;
    private GameLoop gameLoop = new GameLoop(this);
    public static Pane pane;
    public static ArrayList<Unit> units = new ArrayList<Unit>();


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Päävalikon luonti
        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.scene();

        // Peligrafiikoiden luonti(?)
        gameGraphics = new GameGraphics(primaryStage);
        gameGraphics.start();

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player();
        Image shipImage = new Image("/images/spaceship_small_cyan_placeholder.png");
        player.setImage(shipImage);
        player.setPosition(100, 300);
        GameLoop.updateables.add(player);


        pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, 1270, 720);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");
        pane.getChildren().addAll(player);
        if(scene != null && primaryStage != null){
            primaryStage.setScene(mainMenuScene);
        }
        primaryStage.show();

        // Start game painiketta painaessa mainMenuScene vaihdetaan pelin sceneen
        mainMenu.start.setOnAction((event) -> {
            primaryStage.setScene(scene);
        });

        // ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        // Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)){
                if(code.equals("W")){
                    player.setIsMoving(true);
                    input.add(code);
                    player.setDirection(90);
                }
                else if(code.equals("A")){
                    player.setIsMoving(true);
                    input.add(code);
                    player.setDirection(180);
                }
                else if(code.equals("S")){
                    player.setIsMoving(true);
                    input.add(code);
                    player.setDirection(270);
                }
                else if(code.equals("D")){
                    player.setIsMoving(true);
                    input.add(code);
                    player.setDirection(0);
                }

                else if(code.equals("V")){
                    System.exit(0);
                }
                else if (code.equals("O")){
                    input.add(code);
                    Projectile projectile = new Projectile(10, 0);
                    pane.getChildren().addAll(projectile);
                }

            }
        });


        // Kun näppäintä ei enää paineta, poista se arraylististä
        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (input.contains(code)) {
                input.remove(code);
            }

            if(input.size() == 0){
                player.setIsMoving(false);
            }
        });
        gameLoop.start();
    }
}


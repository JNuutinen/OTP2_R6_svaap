package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.util.ArrayList;
import java.util.Stack;

public class GameMain extends Application {
    public static ArrayList<Unit> units = new ArrayList<>();
    public static Pane pane;
    public static Stack<Enemy> enemySpawnStack = new Stack<>();

    private ArrayList<String> input;
    private GameLoop gameLoop = new GameLoop(this);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Päävalikon luonti
        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.scene();
        primaryStage.setScene(mainMenuScene);

        // Start game painiketta painaessa mainMenuScene vaihdetaan pelin sceneen ja peli käynnistyy
        mainMenu.start.setOnAction((event) -> startGame(primaryStage));

        primaryStage.show();
    }

    public static void addEnemy(Enemy enemy) {
        GameLoop.updateables.add(enemy);
        pane.getChildren().add(enemy);
    }

    private void startGame(Stage primaryStage) {
        // Peligrafiikoiden luonti(?)
        /*
        GameGraphics gameGraphics = new GameGraphics(primaryStage);
        gameGraphics.start();
        */

        pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, 1270, 720);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player();
        //Image shipImage = new Image("/images/spaceship_small_cyan_placeholder.png");
        Image shipImage = new Image("/images/player_ship_9000.png");
        player.setImage(shipImage);
        player.setPosition(100, 300);
        GameLoop.updateables.add(player);

        /*
        // Testienemy
        Image enemyImage = new Image("/images/enemy_ship_9000.png");
        Enemy testEnemy = new Enemy(enemyImage, Enemy.MOVE_STRAIGHT, 1100, 300);
        Enemy testEnemy2 =  new Enemy();
        testEnemy2.setImage(enemyImage);
        testEnemy2.setInitPosition(1100, 400);
        testEnemy2.setMovementPattern(Enemy.MOVE_SINE);
        GameLoop.updateables.add(testEnemy);
        GameLoop.updateables.add(testEnemy2);
        */

        // Unitit panee
        pane.getChildren().addAll(player);

        // ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        // Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)){
                switch (code) {
                    case "W":
                        player.setIsMoving(true);
                        input.add(code);
                        player.setDirection(90);
                        break;
                    case "A":
                        player.setIsMoving(true);
                        input.add(code);
                        player.setDirection(180);
                        break;
                    case "S":
                        player.setIsMoving(true);
                        input.add(code);
                        player.setDirection(270);
                        break;
                    case "D":
                        player.setIsMoving(true);
                        input.add(code);
                        player.setDirection(0);
                        break;
                    case "V":
                        System.exit(0);
                    case "O":
                        input.add(code);
                        Projectile projectile = new Projectile(10, 0);
                        pane.getChildren().addAll(projectile);
                        break;
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
        primaryStage.setScene(scene);
        gameLoop.start();
        new Level1().start();
    }
}


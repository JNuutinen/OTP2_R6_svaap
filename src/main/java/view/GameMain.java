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

public class GameMain extends Application {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static ArrayList<Unit> units = new ArrayList<>();
    public static ArrayList<String> input;

    public static Pane pane;

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
        GameLoop.queueUpdateable(enemy);
        pane.getChildren().add(enemy);
    }

    public static void addProjectile(Projectile projectile) {
        GameLoop.queueUpdateable(projectile);
        pane.getChildren().add(projectile);
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
        Scene scene = new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player();
        //Image shipImage = new Image("/images/spaceship_small_cyan_placeholder.png");
        Image shipImage = new Image("/images/player_ship_9000.png");
        player.setImage(shipImage);
        player.setPosition(100, 300);
        GameLoop.queueUpdateable(player);

        // Unitit panee
        pane.getChildren().addAll(player);

        // ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        // Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)) input.add(code);
        });

        // Kun näppäintä ei enää paineta, poista se arraylististä
        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });
        primaryStage.setScene(scene);
        gameLoop.start();
        new Level1().start();
    }
}


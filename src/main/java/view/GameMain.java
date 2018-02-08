package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

import java.util.ArrayList;

public class GameMain extends Application {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static ArrayList<Unit> units = new ArrayList<>();
    public static ArrayList<String> input;
    public static Label score;

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
    public static Pane getPane(){
        return pane;
    }

    public static void addEnemy(Enemy enemy) {
        GameLoop.queueUpdateable(enemy);
        pane.getChildren().add(enemy);
    }
    public static void removeSprite(Sprite sprite){
        //TODO removeSprite jättää spriten hitboxin paikoilleen, alla olevaa vain väliaikainen fixi
        sprite.setPosition(-50, -50);
        pane.getChildren().remove(sprite);
    }

    private void startGame(Stage primaryStage) {
        // Peligrafiikoiden luonti
        pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");

        score = new Label("Score: " + Player.getScore());
        score.setFont(new Font("Cambria", 32));
        pane.getChildren().add(score);

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player();
        player.setPosition(100, 300);
        GameLoop.queueUpdateable(player);
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
        gameLoop.startLoop();
        new Level1().start();
    }
}


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
import model.Player;
import model.Projectile;
import model.Unit;
import model.Updateable;

import java.util.ArrayList;

/*
    Inspoo:
    https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

    Testausta, sovellettu ylläolevast linkist. Ei käytä controlleria eikä GameGraphicsia.
 */
public class GameMain extends Application {
    //TODO backup sprite
    //TODO private GraphicsContext graphicsContext;
    private ArrayList<String> input;
    private GameGraphics gameGraphics;
    private ArrayList<Updateable> updateables = new ArrayList<Updateable>();
    public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public static ArrayList<Unit> units = new ArrayList<Unit>();
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        gameGraphics = new GameGraphics(primaryStage);
        gameGraphics.start();

        //pelaajan luonti ja lisays looppilistaan
        Player player = new Player();
        updateables.add(player);

        Pane pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        Scene scene = new Scene(vbox, 1270, 720);
        primaryStage.setTitle("svaap:development");
        vbox.setStyle("-fx-background-color: black");
        pane.getChildren().addAll(player);

        if(scene != null && primaryStage != null){
            primaryStage.setScene(scene);
        }






        // ArrayList pitää sisällään kyseisellä hetkellä painettujen näppäinten event-koodit
        input = new ArrayList<>();

        // Näppäintä painaessa, lisää se arraylistiin, ellei se jo ole siellä
        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)) input.add(code);

            //TODO sivusuuntaliike
            player.setIsMoving(true);
            if (input.contains("D")){
                player.setDirection(0);
            }
            if (input.contains("W")){
                player.setDirection(90);
            }
            if (input.contains("A")){
                player.setDirection(180);
            }
            if (input.contains("S")){
                player.setDirection(270);
            }
        });


        // Kun näppäintä ei enää paineta, poista se arraylististä
        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);

            if(input.size() == 0){
                player.setIsMoving(false);
            }
        });

        // canvasin graphicscontext
        //TODO graphicsContext = canvas.getGraphicsContext2D();


        // BACKUP SPRITEN ALUSTUS
        Image shipImage = new Image("/images/spaceship_small_cyan_placeholder.png");
        //playerShip = new SpriteBackup();
        player.setImage(shipImage);
        player.setPosition(100, 100);





        // Ensimmäinen ajanotto gamelooppia varten
        long startNanoTime = System.nanoTime();
        gameLoop(startNanoTime);

        // TODO: spriten luonti ja lisäys rootiin
        primaryStage.show();
    }

    private void gameLoop(long startNanoTime) {
        new AnimationTimer() {
            long lastNanoTime = startNanoTime;
            public void handle(long currentNanoTime) {
                // Lasketaan aika viime updatesta
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;

                for (Projectile projectile : projectiles) {
                    projectile.move();
                }
                //ajaa update metodin jokaisessa looppiluokassa
                for(Updateable updateable : updateables){
                    updateable.update();
                }

                /*
                // Renderöinti
                graphicsContext.clearRect(0, 0, 1280, 720);
                player.render(graphicsContext);
                */
            }
        }.start();
    }
}


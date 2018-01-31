package view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Player;

import java.util.ArrayList;

/*
    Inspoo:
    https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

    Testausta, sovellettu ylläolevast linkist. Ei käytä controlleria eikä GameGraphicsia.
 */
public class GameMain extends Application {
    //backup sprite
    private SpriteBackup playerShip;

    private ArrayList<String> input;
    private GraphicsContext graphicsContext;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("branch: gameloop-testi");
        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

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

        // canvasin graphicscontext
        graphicsContext = canvas.getGraphicsContext2D();

        /* BACKUP SPRITEN ALUSTUS
        Image shipImage = new Image("/images/spaceship_small_cyan_placeholder.png");
        playerShip = new SpriteBackup();
        playerShip.setImage(shipImage);
        playerShip.setPosition(100, 100);
        */


        // Ensimmäinen ajanotto gamelooppia varten
        long startNanoTime = System.nanoTime();
        annaSenLooppaa(startNanoTime);

        // TODO: spriten luonti ja lisäys rootiin
        primaryStage.show();
    }

    private void annaSenLooppaa(long startNanoTime) {
        new AnimationTimer() {
            long lastNanoTime = startNanoTime;
            public void handle(long currentNanoTime) {
                // Lasketaan aika viime updatesta
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;

                /* BACKUP SPRITEN LIIKUTTELU JA RENDERÖINTI
                // Aluksen liikuttelu
                playerShip.setVelocity(0,0);
                if (input.contains("A")) playerShip.addVelocity(-50, 0);
                if (input.contains("D")) playerShip.addVelocity(50, 0);
                if (input.contains("W")) playerShip.addVelocity(0, -50);
                if (input.contains("S")) playerShip.addVelocity(0, 50);
                playerShip.update(elapsedTime);

                // Renderöinti
                graphicsContext.clearRect(0, 0, 1280, 720);
                playerShip.render(graphicsContext);
                */
                // TODO: spriten liikuttelu
            }
        }.start();
    }
}


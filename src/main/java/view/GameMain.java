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

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        gameGraphics = new GameGraphics(primaryStage);
        gameGraphics.start();
        /*
        Group root = new Group();
        Scene scene = new Scene(root);
        //primaryStage.setScene(scene);
        primaryStage.setTitle("svaap:development");
        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);*/

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

            int toRight = 0; //-1 = vasemmalle
            int toUp = 0; //-1 = alas

            //jaoteltu kahteen eri 'osaan' luettavuuden vuoksi:
            if (input.contains("D")){
                if (!input.contains("A")){
                    toRight = 1;
                }
            }
            if (input.contains("A")){
                if (!input.contains("D")){
                    toRight = -1;
                }
            }
            if (input.contains("W")){
                if (!input.contains("S")){
                    toUp = 1;
                }
            }
            if (input.contains("S")){
                if (!input.contains("W")){
                    toUp = -1;
                }
            }

            player.setIsMoving(true);
            if(toRight == 1){
                if(toUp == 1){
                    player.setDirection(45); //oikea ylos
                }
                else if(toUp == -1){
                    player.setDirection(315); //oikea alas
                }
                else{
                    player.setDirection(270); //oikea
                }
            }
            else if(toRight == -1){
                if(toUp == 1){
                    player.setDirection(135); //vasen ylos
                }
                else if(toUp == -1){
                    player.setDirection(225); //vasen alas
                }
                else{
                    player.setDirection(180); //vasen
                }
            }
            else if(toUp == 1){
                player.setDirection(90); //ylos
            }
            else if(toUp == -1){
                player.setDirection(270); // alas
            }
            else{
                player.setIsMoving(false);
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


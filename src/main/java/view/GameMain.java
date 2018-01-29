package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        //pelin testaus | kumita primaryStage.setScene(scene); alhaalta jos et haluu testaa peliä.

        Pane pane = new Pane();

        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        vbox.setStyle("-fx-background-color: black");
        Scene scene = new Scene(vbox, 1270, 720);

        primaryStage.setScene(scene);
        primaryStage.show();

        // ============ Pelaajan ohjaukseen liittyvää koodia TODO: viistoliike, nopeampi reaktio
        Player player = new Player();
        player.luoTesteriAlus();//TODO tan rivin saa poistaa
        vbox.getChildren().addAll(player);//TODO tan rivin saa poistaa
        Rectangle playerSprite = new Rectangle(player.getxPosition(), player.getyPosition(), 20, 10);
        playerSprite.setFill(Color.CYAN);
        pane.getChildren().addAll(playerSprite);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch(e.getCode()) {
                case W://ylös
                    player.move(0);
                    break ;
                case A://vasen
                    player.move(6);
                    break;
                case S://alas
                    player.move(4);
                    break;
                case D://oikea
                    player.move(2);
                    break;
            }

            playerSprite.setX(player.getxPosition());
            playerSprite.setY(player.getyPosition());

        });
        // ============
    }

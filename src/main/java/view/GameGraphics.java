package view;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameGraphics {

    private Stage primaryStage;
    private Scene scene;

    GameGraphics(Stage primaryStage) {//konstruktori
        // grafiikka-alustukset
        // pane->vbox->scene->primaryStage
        Pane pane = new Pane();
        VBox.setVgrow(pane, Priority.NEVER);
        VBox vbox;
        vbox = new VBox(pane);
        scene = new Scene(vbox, 1270, 720);

        this.primaryStage = primaryStage;


        // pelaajan aluksen luonti, TODO tämä toiseeen luokkaan
        Rectangle player1Sprite = new Rectangle(100, 300, 20, 10);
        player1Sprite.setFill(Color.CYAN);
        pane.getChildren().addAll(player1Sprite);

        //taustan värjääminen
        vbox.setStyle("-fx-background-color: black");

    }

    public GameGraphics(){
        //parametriton konstruktori. ei käytössä
    }

    public void start(){
        if(scene != null && primaryStage != null){
            primaryStage.setScene(scene);
        }
    }
}

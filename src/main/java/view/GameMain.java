package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Player;

import static javafx.scene.input.KeyCode.*;

public class GameMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
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


        Player player = new Player(scene);


        pane.getChildren().addAll(player);//TODO tan rivin saa poistaa
    }
}


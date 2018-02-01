package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenu {
    private Scene mainMenu;
    private Group root = new Group();
    private Canvas menuCanvas;
    private BorderPane bpane;
    public Button start;
    public Button exit;
    public Button settings;

    MainMenu(){
        mainMenu = new Scene (root);
        menuCanvas = new Canvas(1280, 720);
        build();
        bpane = new BorderPane();
        bpane.setCenter(vbox());
        root.getChildren().addAll(bpane);
    }

    public Scene scene(){
        return mainMenu;
    }

    private void build(){
        start = new Button("Start Game");
        exit = new Button("Exit");
        settings = new Button("Settings");
    }

    public VBox vbox(){
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button[] options = new Button[3];
        options[0] = start;
        options[1] = settings;
        options[2] = exit;

        for (int i=0; i<3; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;

    }

}


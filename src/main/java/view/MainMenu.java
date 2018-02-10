package view;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenu {
    private Scene mainMenu;
    public Button start;
    private Button exit;
    private Button settings;

    MainMenu(){
        Group root = new Group();
        mainMenu = new Scene (root);
        Canvas menuCanvas = new Canvas(1280, 720);
        build();
        BorderPane bpane = new BorderPane();
        bpane.setCenter(vbox());
        root.getChildren().addAll(bpane);
    }

    public Scene scene(){
        return mainMenu;
    }

    private void build(){
        // Painikkeiden luonti
        start = new Button();
        exit = new Button("Exit");
        settings = new Button("Settings");

        //Painikkeiden kuvat
        Image startImg = new Image("/images/Start.png");
        start.setGraphic(new ImageView(startImg));

        start.setStyle("-fx-background-color: transparent");
    }

    private VBox vbox(){
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


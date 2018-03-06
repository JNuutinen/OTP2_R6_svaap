package view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static view.GameMain.*;

class MainMenu {
    public Button play;

    private Group mainMenuGroup;
    private Button exit;
    private Button settings;

    MainMenu(){
        mainMenuGroup = new Group();
        build();
        BorderPane bpane = new BorderPane();
        bpane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        bpane.setCenter(vbox());
        bpane.setStyle("-fx-background-color: black");
        exit.setOnMouseClicked(event -> System.exit(0));
        mainMenuGroup.getChildren().addAll(bpane);
    }

    Group getGroup(){
        return mainMenuGroup;
    }

    private void build() {
        // Painikkeiden luonti
        play = new Button("Play");
        exit = new Button("Exit");
        settings = new Button("Settings");
    }

    private VBox vbox() {
        VBox vbox = new VBox();
        vbox.setSpacing(8);

        play.setPrefWidth(Double.MAX_VALUE);
        settings.setPrefWidth(Double.MAX_VALUE);
        exit.setPrefWidth(Double.MAX_VALUE);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setMaxWidth(100);
        vbox.getChildren().addAll(play, settings, exit);
        return vbox;
    }
}


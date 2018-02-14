package view;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

class MainMenu {
    public Button start;

    private Scene mainMenu;
    private Text levelSelectText;
    public Spinner<Integer> levelSpinner;
    private int levels;
    private Button exit;
    private Button settings;

    MainMenu(int levels){
        this.levels = levels;
        Group root = new Group();
        mainMenu = new Scene (root);
        //Canvas menuCanvas = new Canvas(1280, 720);
        build();
        BorderPane bpane = new BorderPane();
        bpane.setCenter(vbox());
        exit.setOnMouseClicked(event -> System.exit(0));
        root.getChildren().addAll(bpane);
    }

    Scene scene(){
        return mainMenu;
    }

    private void build(){
        // Painikkeiden luonti
        start = new Button();
        levelSelectText = new Text("Select level:");
        Integer[] lol  = {1, 2, 3};
        levelSpinner = new Spinner<>(1, levels, 1);
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

        VBox.setMargin(levelSelectText, new Insets(0, 8, 0, 8));
        VBox.setMargin(levelSpinner, new Insets(0, 0, 0, 0));
        vbox.getChildren().addAll(levelSelectText, levelSpinner);
        for (int i=0; i<3; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;
    }

    int getSelectedLevel() {
        return levelSpinner.getValue();
    }
}


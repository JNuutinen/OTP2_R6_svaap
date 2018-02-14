package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static view.GameMain.MAIN_MENU_HEIGHT;
import static view.GameMain.MAIN_MENU_WIDTH;

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
        build();
        BorderPane bpane = new BorderPane();
        bpane.setPrefSize(MAIN_MENU_WIDTH, MAIN_MENU_HEIGHT);
        bpane.setCenter(vbox());
        bpane.setStyle("-fx-background-color: black");
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
        levelSelectText.setStyle("-fx-fill: white");
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
        levelSpinner.setPrefWidth(Double.MAX_VALUE);
        settings.setPrefWidth(Double.MAX_VALUE);
        exit.setPrefWidth(Double.MAX_VALUE);
        vbox.getChildren().addAll(start, levelSelectText, levelSpinner, settings, exit);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(100);

        return vbox;
    }

    int getSelectedLevel() {
        return levelSpinner.getValue();
    }
}


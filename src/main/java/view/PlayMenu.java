package view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static view.GameMain.*;

class PlayMenu {
    Button startButton;
    Button backButton;

    private Spinner<Integer> levelSpinner;
    private Group playMenuGroup;

    PlayMenu(int levels) {
        playMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        startButton = new Button();
        backButton = new Button("Main menu");
        backButton.setPrefWidth(Double.MAX_VALUE);
        startButton.setGraphic(new ImageView(new Image("/images/Start.png")));
        startButton.setStyle("-fx-background-color: transparent");
        Text levelSelectText = new Text("Select level:");
        levelSelectText.setStyle("-fx-fill: white");
        levelSpinner = new Spinner<>(1, levels, 1);
        levelSpinner.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(100);
        vBox.getChildren().addAll(startButton, levelSelectText, levelSpinner, backButton);

        borderPane.setCenter(vBox);

        playMenuGroup.getChildren().add(borderPane);
    }

    Group getGroup() {
        return playMenuGroup;
    }

    int getSelectedLevel() {
        return levelSpinner.getValue();
    }
}

package view.menus;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

import static view.GameMain.*;

public class SettingsMenu implements Menu {

    /**
     * Takaisinnappi.
     */
    public Button backButton;

    /**
     * Näppäin Suomilokaalille.
     */
    public Button fiFiButton;

    /**
     * Näppäin Uusi Seelanti -lokaalille.
     */
    public Button enNzButton;

    /**
     * Lokaaliotsikko.
     */
    private Text localeText;

    /**
     * Group, johon valikko rakennetaan.
     */
    private Group settingsMenuGroup;

    public SettingsMenu(ResourceBundle messages) {
        settingsMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        localeText = new Text(messages.getString("locale"));
        localeText.setStyle("-fx-fill: white");

        fiFiButton = new Button();
        fiFiButton.setGraphic(new ImageView(new Image("/images/fi.png")));

        enNzButton = new Button();
        enNzButton.setGraphic(new ImageView(new Image("/images/nz.png")));

        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);

        HBox localeBox = new HBox();
        localeBox.setSpacing(8);
        localeBox.setAlignment(Pos.TOP_CENTER);
        localeBox.getChildren().addAll(fiFiButton, enNzButton);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(localeText, localeBox, backButton);

        borderPane.setCenter(vBox);

        settingsMenuGroup.getChildren().add(borderPane);
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        localeText.setText(messages.getString("locale"));
        backButton.setText(messages.getString("back"));
    }

    @Override
    public Group getGroup() {
        return settingsMenuGroup;
    }
}

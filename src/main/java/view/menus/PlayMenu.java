package view.menus;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Luo pelaa -valikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PlayMenu extends Menu{

    /**
     * Pelin käynnistyspainike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button startButton;

    /**
     * Takaisin painike, joka vie päävalikkoon. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button backButton;

    /**
     * Muokkausvalikkoon siirtymisen painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button customizeButton;

    /**
     * Spinner tason valintaan.
     */
    private Spinner<Integer> levelSpinner;

    /**
     * Valikon Group.
     */
    private Group playMenuGroup;

    /**
     * Levelivalikon numeroiden määrä, täytyy olla sama kuin luotujen levelien määrä GameControllerissa.
     */
    private static final int NUMBER_OF_LEVELS = 1;

    /** TODO
     * Konstruktori, joka luo komponentit ja lisää Groupiin.
     * @param levels Tasojen määrä kokonaisuudessaan, jonka perusteella tasonvalitsin tehdään.
     */
    public PlayMenu(ResourceBundle messages) {
        playMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        startButton = new Button(messages.getString("start_game"));
        startButton.setPrefWidth(Double.MAX_VALUE);
        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);
        //startButton.setGraphic(new ImageView(new Image("/images/Start.png")));
        //startButton.setStyle("-fx-background-color: transparent");
        Text levelSelectText = new Text(messages.getString("select_level"));
        levelSelectText.setStyle("-fx-fill: white");
        levelSpinner = new Spinner<>(1, NUMBER_OF_LEVELS, 1);
        levelSpinner.setPrefWidth(Double.MAX_VALUE);

        customizeButton = new Button(messages.getString("select_weapons"));
        customizeButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(startButton, levelSelectText, levelSpinner, customizeButton, backButton);

        borderPane.setCenter(vBox);

        playMenuGroup.getChildren().add(borderPane);

        // Pelin aloituspainike click event
        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000), uiRoot);
            ft2.setFromValue(1.0);
            ft2.setToValue(0.0);
            ft2.play();
            ft2.setOnFinished(event1 -> {
                pane.getChildren().remove(uiRoot);
                startGame(primaryStage, playerController, customizeMenu.getSelectedPrimaryWeapon(), customizeMenu.getSelectedSecondaryWeapon());
            });
        });
    }

    /**
     * Palautaa valikon Groupin.
     * @return Valikon Group.
     */
    public Group getGroup() {
        return playMenuGroup;
    }

    /**
     * Palauttaa Spinnerissä valitun tason numeron.
     * @return Valitun tason numero.
     */
    public int getSelectedLevel() {
        return levelSpinner.getValue();
    }
}

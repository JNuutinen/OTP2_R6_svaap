package view.menus;

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

/**
 * Luo pelaa -valikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PlayMenu {

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
     * Konstruktori, joka luo komponentit ja lisää Groupiin.
     * @param levels Tasojen määrä kokonaisuudessaan, jonka perusteella tasonvalitsin tehdään.
     */
    public PlayMenu(int levels) {
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

        customizeButton = new Button("Select weapons");
        customizeButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(100);
        vBox.getChildren().addAll(startButton, levelSelectText, levelSpinner, customizeButton, backButton);

        borderPane.setCenter(vBox);

        playMenuGroup.getChildren().add(borderPane);
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

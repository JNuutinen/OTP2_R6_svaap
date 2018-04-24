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
import view.GameMain;

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
     * Tasonvaihto-otsikko.
     */
    private Text levelSelectText;

    /**
     * Spinner tason valintaan.
     */
    private Spinner<Integer> levelSpinner;

    /**
     * Levelivalikon numeroiden määrä, täytyy olla sama kuin luotujen levelien määrä GameControllerissa.
     */
    private static final int NUMBER_OF_LEVELS = 1;

    private MainMenu mainMenu;
    private CustomizeMenu customizeMenu;

    /** TODO
     * Konstruktori, joka luo komponentit ja lisää Groupiin.
     * @param messages Lokalisoidut resurssit.
     * @param levels Tasojen määrä kokonaisuudessaan, jonka perusteella tasonvalitsin tehdään.
     * @param messages TODO Tasojen määrä kokonaisuudessaan, jonka perusteella tasonvalitsin tehdään.
     */
    public PlayMenu(ResourceBundle messages, MenuSpace menuSpace, GameMain gameMain) {
        super(menuSpace);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        startButton = new Button(messages.getString("start_game"));
        startButton.setPrefWidth(Double.MAX_VALUE);
        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);
        //startButton.setGraphic(new ImageView(new Image("/images/Start.png")));
        //startButton.setStyle("-fx-background-color: transparent");
        levelSelectText = new Text(messages.getString("select_level"));
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

        getChildren().addAll(borderPane);

        //-- click eventit --//

    @Override
    public void changeLocale(ResourceBundle messages) {
        startButton.setText(messages.getString("start_game"));
        backButton.setText(messages.getString("back"));
        levelSelectText.setText(messages.getString("select_level"));
        customizeButton.setText(messages.getString("select_weapons"));
    }

    @Override
    public Group getGroup() {
        return playMenuGroup;
        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, mainMenu));
        customizeButton.setOnAction(event -> getMenuSpace().changeToNextMenu(this, customizeMenu));

        // Pelin aloituspainike click event
        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000), gameMain.getUiRoot());
            ft2.setFromValue(1.0);
            ft2.setToValue(0.0);
            ft2.play();
            ft2.setOnFinished(event1 -> {
                //pane.getChildren().remove(uiRoot);
                gameMain.startGame(customizeMenu.getSelectedPrimaryWeapon(), customizeMenu.getSelectedSecondaryWeapon());
            });
        });
    }

    /**
     * Palauttaa Spinnerissä valitun tason numeron.
     * @return Valitun tason numero.
     */
    public int getSelectedLevel() {
        return levelSpinner.getValue();
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void setCustomizeMenu(CustomizeMenu customizeMenu) {
        this.customizeMenu = customizeMenu;
    }
}

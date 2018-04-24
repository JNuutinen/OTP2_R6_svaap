package view.menus;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static view.GameMain.*;

public class SettingsMenu extends Menu {

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
     * MainMenu-menu
     */
    private MainMenu mainMenu;

    /**
     * Map jossa pelin eri lokaalit.
     */
    private Map<String, Locale> locales;

    /**
     * Lokalisoidut tekstit.
     */
    private ResourceBundle messages;

    /**
     * Konstruktori. Lisää Komponentit itseensä. Luo myös click eventit.
     * @param messages Lokalisoidut tekstit
     * @param menuSpace MenusSpace jossa tieto kaikista menuista.
     */
    public SettingsMenu(ResourceBundle messages, MenuSpace menuSpace) {
        super(menuSpace);
        this.messages = messages;

        initLocales();
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

        getChildren().add(borderPane);

                    //-- click eventit --//

        // Settings menun back button click event
        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, mainMenu));

        // Settings menun lokaalien click eventit
        fiFiButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("fi_FI")));
        enNzButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("en_NZ")));
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        localeText.setText(messages.getString("locale"));
        backButton.setText(messages.getString("back"));
    }

    /**
     * Setteri MainMenulle
     * @param mainMenu MainMenu
     */
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * Luo pelin valittavat lokaalit ja tallentaa ne locales Mappiin. Asettaa vakiolokaalin.
     */
    private void initLocales() {
        locales = new HashMap<>();
        locales.put("en_NZ", new Locale("en", "NZ"));
        locales.put("fi_FI", new Locale("fi", "FI"));
        messages = ResourceBundle.getBundle("MessagesBundle", locales.get("en_NZ"));
    }
}

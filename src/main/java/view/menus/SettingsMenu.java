package view.menus;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Asetusmenu.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class SettingsMenu extends Menu {

    /**
     * Takaisinnappi.
     */
    private Button backButton;

    /**
     * Lokaaliotsikko.
     */
    private Text localeText;

    /**
     * Otsikko menujen efektien valinnalle.
     */
    private Text menuEffectText;

    private RadioButton slideEffectButton;

    private RadioButton noEffectButton;

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
    SettingsMenu(ResourceBundle messages, Map<String, Locale> locales, MenuSpace menuSpace) {
        super(menuSpace);
        this.messages = messages;

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        localeText = new Text(messages.getString("locale"));
        localeText.setStyle("-fx-fill: white");

        Button fiFiButton = new Button();
        fiFiButton.setGraphic(new ImageView(new Image("/images/fi.png")));

        Button seSeButton = new Button();
        seSeButton.setGraphic(new ImageView(new Image("/images/se.png")));

        Button enNzButton = new Button();
        enNzButton.setGraphic(new ImageView(new Image("/images/nz.png")));

        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);

        HBox localeBox = new HBox();
        localeBox.setSpacing(8);
        localeBox.setAlignment(Pos.TOP_CENTER);
        localeBox.getChildren().addAll(fiFiButton, seSeButton, enNzButton);

        final ToggleGroup effectButtons = new ToggleGroup();
        noEffectButton = new RadioButton(messages.getString("menu_effect_none"));
        noEffectButton.setToggleGroup(effectButtons);
        slideEffectButton = new RadioButton(messages.getString("menu_effect_slide"));
        slideEffectButton.setToggleGroup(effectButtons);
        slideEffectButton.setSelected(true);

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
        seSeButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("se_SE")));
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
    void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
}

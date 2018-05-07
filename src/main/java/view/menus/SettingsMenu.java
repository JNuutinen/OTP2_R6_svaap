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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import view.menuScreenFX.MenuFX;
import view.menuScreenFX.PlainMenuTransition;
import view.menuScreenFX.SliderMenuTransition;

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

    /**
     * Radionappi menuefektien poistamiselle.
     */
    private RadioButton noEffectButton;

    /**
     * Radionappi menuefektin slide asetukselle.
     */
    private RadioButton slideEffectButton;

    /**
     * MenuSpace, menujen efektien vaihtamiseen.
     */
    private MenuSpace menuSpace;

    /**
     * MainMenu-menu
     */
    private MainMenu mainMenu;

    /**
     * Konstruktori. Lisää Komponentit itseensä. Luo myös click eventit.
     *
     * @param messages Lokalisoidut tekstit
     * @param menuSpace MenusSpace jossa tieto kaikista menuista.
     */
    SettingsMenu(ResourceBundle messages, Map<String, Locale> locales, MenuSpace menuSpace) {
        super(menuSpace);
        this.menuSpace = menuSpace;

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

        menuEffectText = new Text(messages.getString("menu_effect"));
        menuEffectText.setStyle("-fx-fill: white");
        final ToggleGroup effectButtons = new ToggleGroup();
        noEffectButton = new RadioButton(messages.getString("menu_effect_none"));
        noEffectButton.setTextFill(Color.WHITE);
        noEffectButton.setToggleGroup(effectButtons);
        noEffectButton.setUserData(PlainMenuTransition.getInstance());
        slideEffectButton = new RadioButton(messages.getString("menu_effect_slide"));
        slideEffectButton.setTextFill(Color.WHITE);
        slideEffectButton.setToggleGroup(effectButtons);
        slideEffectButton.setSelected(true);
        slideEffectButton.setUserData(SliderMenuTransition.getInstance());
        menuSpace.setMenuFX(SliderMenuTransition.getInstance());
        setRadioButtonCallback(effectButtons);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(localeText, localeBox, menuEffectText, slideEffectButton, noEffectButton, backButton);

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
        menuEffectText.setText(messages.getString("menu_effect"));
        localeText.setText(messages.getString("locale"));
        backButton.setText(messages.getString("back"));
        noEffectButton.setText(messages.getString("menu_effect_none"));
        slideEffectButton.setText(messages.getString("menu_effect_slide"));
    }

    /**
     * Setteri MainMenulle
     *
     * @param mainMenu MainMenu
     */
    void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * Apumetodi toggleGroupin listenerin asettamiseen.
     *
     * @param toggleGroup ToggleGroup, johon listener asetetaan.
     */
    private void setRadioButtonCallback(ToggleGroup toggleGroup) {
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                if (new_toggle.getUserData() instanceof MenuFX) {
                    menuSpace.setMenuFX((MenuFX) new_toggle.getUserData());
                }
            }
        });
    }
}

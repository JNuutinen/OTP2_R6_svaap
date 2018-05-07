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
import model.factory.*;
import view.View;
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
     * Otsikko taustan värin vaihtamiselle.
     */
    private Text backgroundColorText;

    /**
     * Radionappi normaalille taustalle.
     */
    private RadioButton normalBgButton;

    /**
     * Radionappi punaiselle taustalle.
     */
    private RadioButton redBgButton;

    /**
     * Radionappi vihreälle taustalle.
     */
    private RadioButton greenBgButton;

    /**
     * Radionappi lilalle taustalle.
     */
    private RadioButton purpleBgButton;

    /**
     * Radionappi vedenalaiselle taustalle.
     */
    private RadioButton underWaterBgButton;

    /**
     * MenuSpace, menujen efektien vaihtamiseen.
     */
    private MenuSpace menuSpace;

    /**
     * MainMenu-menu
     */
    private MainMenu mainMenu;

    /**
     * Pelin view.
     */
    private View gameMain;

    /**
     * Konstruktori. Lisää Komponentit itseensä. Luo myös click eventit.
     *
     * @param gameMain Pelin view.
     * @param messages Lokalisoidut tekstit
     * @param menuSpace MenusSpace jossa tieto kaikista menuista.
     */
    SettingsMenu(View gameMain, ResourceBundle messages, Map<String, Locale> locales, MenuSpace menuSpace) {
        super(menuSpace);
        this.gameMain = gameMain;
        this.menuSpace = menuSpace;

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);
        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, mainMenu));

        HBox localeBox = makeLocaleSettings(messages, locales);

        makeMenuFxSettings(messages);

        makeBackgroundSettings(messages);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(localeText, localeBox, menuEffectText, slideEffectButton, noEffectButton,
                backgroundColorText, normalBgButton, redBgButton, greenBgButton, purpleBgButton, underWaterBgButton,
                backButton);

        borderPane.setCenter(vBox);

        getChildren().add(borderPane);
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        menuEffectText.setText(messages.getString("menu_effect"));
        localeText.setText(messages.getString("locale"));
        backButton.setText(messages.getString("back"));
        noEffectButton.setText(messages.getString("menu_effect_none"));
        slideEffectButton.setText(messages.getString("menu_effect_slide"));
        backgroundColorText.setText(messages.getString("background"));
        normalBgButton.setText(messages.getString("normal"));
        redBgButton.setText(messages.getString("red"));
        greenBgButton.setText(messages.getString("green"));
        purpleBgButton.setText(messages.getString("purple"));
        underWaterBgButton.setText(messages.getString("water"));
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
     * @param toggleGroup ToggleGroup, johon listener asetetaan. Vaihtaa menuefektin.
     */
    private void setMenuFxRadioButtonCallback(ToggleGroup toggleGroup) {
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                if (new_toggle.getUserData() instanceof MenuFX) {
                    menuSpace.setMenuFX((MenuFX) new_toggle.getUserData());
                }
            }
        });
    }

    /**
     * Apumetodi toggleGroupin listenerin asettamiseen. Vaihtaa pelin teeman.
     *
     * @param toggleGroup ToggleGroup, johon listener asetetaan.
     */
    private void setBackgroundRadioButtonCallback(ToggleGroup toggleGroup) {
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                if (new_toggle.getUserData() instanceof Theme) {
                    gameMain.setTheme((Theme) new_toggle.getUserData());
                }
            }
        });
    }

    /**
     * Apumetodi joka rakentaa lokaalinvaihtoasetuksien osat.
     *
     * @param messages Lokalisoidut merkkijonot.
     * @param locales  Pelin lokaalit.
     * @return HBox, jossa lokaaliasetuksien elementit.
     */
    private HBox makeLocaleSettings(ResourceBundle messages, Map<String, Locale> locales) {
        localeText = new Text(messages.getString("locale"));
        localeText.setStyle("-fx-fill: white");

        Button fiFiButton = new Button();
        fiFiButton.setGraphic(new ImageView(new Image("/images/fi.png")));

        Button seSeButton = new Button();
        seSeButton.setGraphic(new ImageView(new Image("/images/se.png")));

        Button enNzButton = new Button();
        enNzButton.setGraphic(new ImageView(new Image("/images/nz.png")));

        HBox localeBox = new HBox();
        localeBox.setSpacing(8);
        localeBox.setAlignment(Pos.TOP_CENTER);
        localeBox.getChildren().addAll(fiFiButton, seSeButton, enNzButton);
        fiFiButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("fi_FI")));
        seSeButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("se_SE")));
        enNzButton.setOnAction(event -> getMenuSpace().changeLocales(locales.get("en_NZ")));
        return localeBox;
    }

    /**
     * Apumetodi, joka rakentaa menuefektien asetukset.
     *
     * @param messages Lokalisoidut merkkijonot.
     */
    private void makeMenuFxSettings(ResourceBundle messages) {
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
        setMenuFxRadioButtonCallback(effectButtons);
    }

    /**
     * Apumetodi, joka rakentaa taustateeman asetukset.
     *
     * @param messages Lokalisoidut merkkijonot.
     */
    private void makeBackgroundSettings(ResourceBundle messages) {
        backgroundColorText = new Text(messages.getString("background"));
        backgroundColorText.setStyle("-fx-fill: white");

        final ToggleGroup bgButtons = new ToggleGroup();

        normalBgButton = new RadioButton(messages.getString("normal"));
        normalBgButton.setTextFill(Color.WHITE);
        normalBgButton.setToggleGroup(bgButtons);
        normalBgButton.setSelected(true);
        normalBgButton.setUserData(NormalSpaceTheme.getInstance());

        redBgButton = new RadioButton(messages.getString("red"));
        redBgButton.setTextFill(Color.WHITE);
        redBgButton.setToggleGroup(bgButtons);
        redBgButton.setUserData(RedSpaceTheme.getInstance());

        greenBgButton = new RadioButton(messages.getString("green"));
        greenBgButton.setTextFill(Color.WHITE);
        greenBgButton.setToggleGroup(bgButtons);
        greenBgButton.setUserData(GreenSpaceTheme.getInstance());

        purpleBgButton = new RadioButton(messages.getString("purple"));
        purpleBgButton.setTextFill(Color.WHITE);
        purpleBgButton.setToggleGroup(bgButtons);
        purpleBgButton.setUserData(PurpleSpaceTheme.getInstance());

        underWaterBgButton = new RadioButton(messages.getString("water"));
        underWaterBgButton.setTextFill(Color.WHITE);
        underWaterBgButton.setToggleGroup(bgButtons);
        underWaterBgButton.setUserData(UnderwaterTheme.getInstance());

        setBackgroundRadioButtonCallback(bgButtons);
    }
}

package view.menus;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Rakentaa päävalikkonäkymän.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class MainMenu extends Menu {

    /**
     * Pelaa -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button play;

    /**
     * Asetukset-painike.
     */
    public Button settings;

    /**
     * Nettipelaa -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button netplay;


    /**
     * Pelin lopetuspainike.
     */
    private Button exit;

    /**
     * NetPlay-menu
     */
    private NetplayMenu netplayMenu;

    /**
     * PlayMenu-menu
     */
    private PlayMenu playMenu;

    /**
     * SettingsMenu-menu
     */
    private SettingsMenu settingsMenu;

    /**
     * Konstruktori, jossa luodaan komponentit ja lisätään Groupiin.
     * @param messages Lokalisoidut resurssit.
     */
    public MainMenu(ResourceBundle messages, MenuSpace menuSpace) {
        super(menuSpace);

        play = new Button(messages.getString("play"));
        settings = new Button(messages.getString("settings"));
        netplay = new Button("netplay"); // TODO resourceBundle tähän
        exit = new Button(messages.getString("exit"));

        BorderPane bpane = new BorderPane();
        bpane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - BANNER_HEIGHT);
        bpane.setCenter(vbox());
        bpane.setStyle("-fx-background-color: black");
        exit.setOnMouseClicked(event -> System.exit(0));
        getChildren().addAll(bpane);

        //-- click eventit --//

        play.setOnAction(event -> getMenuSpace().changeToNextMenu(this, playMenu));

        netplay.setOnAction(event -> getMenuSpace().changeToNextMenu(this, netplayMenu));

        settings.setOnAction(event -> getMenuSpace().changeToNextMenu(this, settingsMenu));

    }

    /**
     * Luo VBoxin, johon lisätään komponentit. VBox lisätään Groupiin.
     * @return Luotu VBox.
     */
    private VBox vbox() {
        VBox vbox = new VBox();
        vbox.setSpacing(8);

        play.setPrefWidth(Double.MAX_VALUE);
        netplay.setPrefWidth(Double.MAX_VALUE);
        exit.setPrefWidth(Double.MAX_VALUE);
        settings.setPrefWidth(Double.MAX_VALUE);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setMaxWidth(150);
        vbox.getChildren().addAll(play, settings, netplay, exit);
        return vbox;
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        play.setText(messages.getString("play"));
        settings.setText(messages.getString("settings"));
        exit.setText(messages.getString("exit"));
    }

    public void setNetplayMenu(NetplayMenu netplayMenu) {
        this.netplayMenu = netplayMenu;
    }

    public void setPlayMenu(PlayMenu playMenu) {
        this.playMenu = playMenu;
    }

    public void setSettingsMenu(view.menus.SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }
}


package view.menus;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.network.PlayerController;
import model.network.ServerController;

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
     * Nettipelaa -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button netplay;


    /**
     * Pelin lopetuspainike.
     */
    private Button exit;



    /**
     * Konstruktori, jossa luodaan komponentit ja lisätään Groupiin.
     */
    public MainMenu(ResourceBundle messages){
        play = new Button(messages.getString("play"));
        netplay = new Button("netplay"); // TODO resourceBundle tähän
        exit = new Button(messages.getString("exit"));

        BorderPane bpane = new BorderPane();
        bpane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        bpane.setCenter(vbox());
        bpane.setStyle("-fx-background-color: black");
        exit.setOnMouseClicked(event -> System.exit(0));
        getChildren().addAll(bpane);

        //      click eventit

        // Main menun play click event
        play.setOnAction(event -> changeToNextMenu(new PlayMenu(messages))); // TODO parametrit
        // Main menun netplay click event
        netplay.setOnAction(event -> changeToNextMenu(new NetplayMenu(messages)));
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
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setMaxWidth(150);
        vbox.getChildren().addAll(play, netplay, exit);
        return vbox;
    }
}


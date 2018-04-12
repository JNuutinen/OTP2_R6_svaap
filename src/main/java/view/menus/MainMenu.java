package view.menus;

import javafx.geometry.Pos;
import javafx.scene.Group;
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
public class MainMenu {

    /**
     * Pelaa -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button play;

    /**
     * Group, johon valikko rakennetaan.
     */
    private Group mainMenuGroup;

    /**
     * Pelin lopetuspainike.
     */
    private Button exit;

    /**
     * Konstruktori, jossa luodaan komponentit ja lisätään Groupiin.
     */
    public MainMenu(ResourceBundle messages){
        mainMenuGroup = new Group();
        play = new Button(messages.getString("play"));
        exit = new Button(messages.getString("exit"));
        BorderPane bpane = new BorderPane();
        bpane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        bpane.setCenter(vbox());
        bpane.setStyle("-fx-background-color: black");
        exit.setOnMouseClicked(event -> System.exit(0));
        mainMenuGroup.getChildren().addAll(bpane);
    }

    /**
     * Palauttaa luodun Groupin.
     * @return Group, jossa valikko.
     */
    public Group getGroup(){
        return mainMenuGroup;
    }

    /**
     * Luo VBoxin, johon lisätään komponentit. VBox lisätään Groupiin.
     * @return Luotu VBox.
     */
    private VBox vbox() {
        VBox vbox = new VBox();
        vbox.setSpacing(8);

        play.setPrefWidth(Double.MAX_VALUE);
        exit.setPrefWidth(Double.MAX_VALUE);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setMaxWidth(150);
        vbox.getChildren().addAll(play, exit);
        return vbox;
    }
}


package view.menus;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.network.PlayerController;
import model.network.ServerController;

import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Luo nettipelaus -valikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class NetplayMenu extends Menu{

    // TODO jdoc
    private Button netPlay;

    // TODO jdoc
    private Button connect;

    // TODO jdoc
    private Button host;

    public Button backButton;


    /**
     * Valikon Group.
     */
    private Group netplayMenuGroup;

    /**
     * TODO kaikki jdocit
     */
    public NetplayMenu(ResourceBundle messages) {

        connect = new Button("connect");
        host = new Button("host");
        backButton = new Button(messages.getString("back"));

        connect.setOnMouseClicked(event -> connectToHost());
        host.setOnMouseClicked(event -> hostGame());


        netplayMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");


        backButton.setPrefWidth(Double.MAX_VALUE);


        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(backButton, connect, host);

        borderPane.setCenter(vBox);

        netplayMenuGroup.getChildren().add(borderPane);
    }

    private void hostGame(){
        Thread serverController = new ServerController();
        serverController.start();
    }

    private void connectToHost(){
        PlayerController playerController = new PlayerController();
    }

    /**
     * Palautaa valikon Groupin.
     * @return Valikon Group.
     */
    public Group getGroup() {
        return netplayMenuGroup;
    }
}

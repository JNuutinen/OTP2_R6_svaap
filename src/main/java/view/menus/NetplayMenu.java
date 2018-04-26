package view.menus;

import Multiplayer.Client;
import Multiplayer.Server;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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


    private MainMenu mainMenu;

    /**
     * TODO kaikki jdocit
     */
    public NetplayMenu(ResourceBundle messages, MenuSpace menuSpace) {
        super(menuSpace);

        connect = new Button("connect"); //TODO locale
        host = new Button("host");  // TODO locale
        backButton = new Button(messages.getString("back"));

        connect.setOnMouseClicked(event -> connectToHost());
        host.setOnMouseClicked(event -> hostGame());
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");


        backButton.setPrefWidth(Double.MAX_VALUE);
        host.setPrefWidth(Double.MAX_VALUE);
        connect.setPrefWidth(Double.MAX_VALUE);


        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(connect, host, backButton);

        borderPane.setCenter(vBox);

        getChildren().addAll(borderPane);

                    //-- click eventit --//

        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, mainMenu));
    }

    /**
     * Ei toiminnassa
     */
    private void hostGame(){
        Server server = new Server();
        server.startServer();
        (new Thread(server)).start();
        //Thread serverController = new ServerController();
        //serverController.start();
    }

    /**
     * Ei toiminnassa
     */
    private void connectToHost(){
        Client.connect();
        //PlayerController playerController = new PlayerController();
    }

    /**
     * Setteri MainMenulle
     * @param mainMenu MainMenu
     */
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        backButton.setText(messages.getString("back"));
    }
}

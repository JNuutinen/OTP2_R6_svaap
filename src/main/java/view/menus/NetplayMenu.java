package view.menus;

import Multiplayer.Client;
import Multiplayer.Server;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.GameMain;

import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Luo nettipelaus -valikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */

// TODO     EI KÄYTÖSSÄ                                 EI KÄYTÖSSÄ
    // TODO POISTA LUOKKA JOS NETTIPELAUSTA EI TEHDÄ

public class NetplayMenu extends Menu{

    // TODO jdoc
    private Button netPlay;

    // TODO jdoc
    private Button connectButton;

    // TODO jdoc
    private Button hostButton;

    public Button backButton;


    private MainMenu mainMenu;

    private CustomizeMenu customizeMenu;

    /**
     * TODO kaikki jdocit
     */
    public NetplayMenu(ResourceBundle messages, MenuSpace menuSpace, GameMain gameMain) {
        super(menuSpace);

        connectButton = new Button("connect"); //TODO locale
        hostButton = new Button("host");  // TODO locale
        backButton = new Button(messages.getString("back"));

        connectButton.setOnMouseClicked(event -> connectToHost());
        hostButton.setOnMouseClicked(event -> hostGame());
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");


        backButton.setPrefWidth(Double.MAX_VALUE);
        hostButton.setPrefWidth(Double.MAX_VALUE);
        connectButton.setPrefWidth(Double.MAX_VALUE);


        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(connectButton, hostButton, backButton);

        borderPane.setCenter(vBox);

        getChildren().addAll(borderPane);

                    //-- click eventit --//

        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, mainMenu));

        connectButton.setOnAction(event -> {
            Client client = new Client();
            if (client.connect()) {
            connectButton.setDisable(true);
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000), gameMain.getUiRoot());
            ft2.setFromValue(1.0);
            ft2.setToValue(0.0);
            ft2.play();
            ft2.setOnFinished(event1 -> {
                //pane.getChildren().remove(uiRoot);
                gameMain.startGame(customizeMenu.getSelectedPrimaryWeapon(), customizeMenu.getSelectedSecondaryWeapon());
            });
        }});

        hostButton.setOnAction(event -> {
            Server server = new Server();
            if (server.startServer()) {
                hostButton.setDisable(true);
                FadeTransition ft2 = new FadeTransition(Duration.millis(1000), gameMain.getUiRoot());
                ft2.setFromValue(1.0);
                ft2.setToValue(0.0);
                ft2.play();
                ft2.setOnFinished(event1 -> {
                    //pane.getChildren().remove(uiRoot);
                    gameMain.startGame(customizeMenu.getSelectedPrimaryWeapon(), customizeMenu.getSelectedSecondaryWeapon());
                });
            }});
    }

    /**
     * Ei toiminnassa
     */
    private void hostGame(){
        Server server = new Server();
        if (server.startServer()) {
            //TODO: Start game
        }
    }

    /**
     * Ei toiminnassa
     */
    private void connectToHost(){
        Client client = new Client();
        if (client.connect()) {
            //TODO: start game
        }
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

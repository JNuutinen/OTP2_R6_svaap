package view.menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Rakentaa taukovalikkonäkymän.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PauseMenu implements Menu {

    /**
     * Jatka peliä -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button continueButton;

    /**
     * Palaa päävalikkoon -painike. OnClick asetetaan ulkopuolelta, siksi public.
     */
    public Button quitButton;

    /**
     * Taukovalikon Group.
     */
    private Group pauseMenuGroup;

    /**
     * Konstruktori, jossa luodaan komponentit ja lisätään Groupiin.
     * @param messages Lokalisoidut resurssit.
     */
    public PauseMenu(ResourceBundle messages) {
        continueButton = new Button(messages.getString("continue"));
        continueButton.setMinWidth(100);
        continueButton.setPrefWidth(Double.MAX_VALUE);

        quitButton = new Button(messages.getString("quit"));
        quitButton.setMinWidth(100);
        quitButton.setPrefWidth(Double.MAX_VALUE);

        pauseMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: black");
        vBox.getChildren().addAll(continueButton, quitButton);
        VBox.setMargin(continueButton, new Insets(50,50,0,50));
        VBox.setMargin(quitButton, new Insets(0,50,50,50));
        vBox.setMaxWidth(continueButton.getWidth() + VBox.getMargin(continueButton).getLeft()
                + VBox.getMargin(continueButton).getRight() + vBox.getSpacing());
        vBox.setMaxHeight(continueButton.getHeight() + VBox.getMargin(continueButton).getTop()
                + VBox.getMargin(continueButton).getBottom()
                + quitButton.getHeight() + VBox.getMargin(quitButton).getTop()
                + VBox.getMargin(quitButton).getBottom() + vBox.getSpacing());

        borderPane.setCenter(vBox);

        pauseMenuGroup.getChildren().add(borderPane);
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        continueButton.setText(messages.getString("continue"));
        quitButton.setText(messages.getString("quit"));
    }

    @Override
    public Group getGroup() {
        return pauseMenuGroup;
    }
}

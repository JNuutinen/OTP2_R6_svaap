package view.menus;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Component;
import model.weapons.Weapon;

import java.util.ArrayList;

import static view.GameMain.*;

/**
 * Luo aluksen muokkausvalikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class CustomizeMenu {

    /**
     * Takaisin -painike, joka vie pelaavalikkoon.
     */
    public Button backButton;

    /**
     * Valittavien aseiden listat, joiden perusteella ComboBoxit tehdään.
     */
    private ArrayList<Weapon> primaryWeapon, secondaryWeapon;

    /**
     * ComboBox pääaseen valintaan.
     */
    private ComboBox<String> primaryComboBox;

    /**
     * ComboBox sivuaseen valintaan.
     */
    private ComboBox<String> secondaryComboBox;

    /**
     * Group, johon valikko tehdään.
     */
    private Group customizeMenuGroup;

    /**
     * Konstruktori. Luo komponentit ja lisää Groupiin.
     * @param primaryWeapons Lista valittavista pääaseista.
     * @param secondaryWeapons Lista valittavista sivuaseista.
     */
    public CustomizeMenu(ArrayList<Weapon> primaryWeapons, ArrayList<Weapon> secondaryWeapons) {
        this.primaryWeapon = primaryWeapons;
        this.secondaryWeapon = secondaryWeapons;

        customizeMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        Text primary1Text = new Text("Primary weapon (\"o\")");
        primary1Text.setStyle("-fx-fill: white");

        Text primary2Text = new Text("Primary weapon 2 (\"o\")");
        primary2Text.setStyle("-fx-fill: white");

        Text secondaryText = new Text("Secondary weapon (\"p\")");
        secondaryText.setStyle("-fx-fill: white");

        ArrayList<String>primaryWeapon1Names = new ArrayList<>(primaryWeapons.size());
        for (Weapon w : primaryWeapons) {
            primaryWeapon1Names.add(((Component) w).getName());
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            secondaryWeaponNames.add(((Component) w).getName());
        }

        primaryComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeapon1Names));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeapon1Names.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

        primaryComboBox.setPrefWidth(Double.MAX_VALUE);
        secondaryComboBox.setPrefWidth(Double.MAX_VALUE);

        backButton = new Button("Back");
        backButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(100);
        vBox.getChildren().addAll(primary1Text, primaryComboBox, secondaryText, secondaryComboBox, backButton);

        borderPane.setCenter(vBox);

        customizeMenuGroup.getChildren().add(borderPane);
    }

    /**
     * Palauttaa valikon Groupin.
     * @return Valikon Group.
     */
    public Group getGroup() {
        return customizeMenuGroup;
    }

    /**
     * Palauttaa ComboBoxissa valitun pääaseen.
     * @return Valittu pääase.
     */
    public Weapon getSelectedPrimaryWeapon() {
        String selected = primaryComboBox.getValue();
        for (Weapon w : primaryWeapon) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }

    /**
     * Palauttaa ComboBoxissa valitun sivuaseen.
     * @return Valittu sivuase.
     */
    public Weapon getSelectedSecondaryWeapon() {
        String selected = secondaryComboBox.getValue();
        for (Weapon w : secondaryWeapon) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }
}

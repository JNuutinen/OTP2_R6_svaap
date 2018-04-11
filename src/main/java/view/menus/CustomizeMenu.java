package view.menus;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.weapons.*;
import view.GameMain;

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

        Text primary1Text = new Text(GameMain.messages.getString("primary_weapon"));
        primary1Text.setStyle("-fx-fill: white");

        Text secondaryText = new Text(GameMain.messages.getString("secondary_weapon"));
        secondaryText.setStyle("-fx-fill: white");

        ArrayList<String>primaryWeaponNames = new ArrayList<>(primaryWeapons.size());
        for (Weapon w : primaryWeapons) {
            if (w instanceof Blaster) primaryWeaponNames.add(GameMain.messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) primaryWeaponNames.add(GameMain.messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) primaryWeaponNames.add(GameMain.messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) primaryWeaponNames.add(GameMain.messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) primaryWeaponNames.add(GameMain.messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) primaryWeaponNames.add(GameMain.messages.getString("weapon_rocket_shotgun"));
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            if (w instanceof Blaster) secondaryWeaponNames.add(GameMain.messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) secondaryWeaponNames.add(GameMain.messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) secondaryWeaponNames.add(GameMain.messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) secondaryWeaponNames.add(GameMain.messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) secondaryWeaponNames.add(GameMain.messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) secondaryWeaponNames.add(GameMain.messages.getString("weapon_rocket_shotgun"));
        }

        primaryComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeaponNames));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeaponNames.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

        primaryComboBox.setPrefWidth(Double.MAX_VALUE);
        secondaryComboBox.setPrefWidth(Double.MAX_VALUE);

        backButton = new Button(GameMain.messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
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
        return primaryWeapon.get(primaryComboBox.getSelectionModel().getSelectedIndex());
    }

    /**
     * Palauttaa ComboBoxissa valitun sivuaseen.
     * @return Valittu sivuase.
     */
    public Weapon getSelectedSecondaryWeapon() {
        return secondaryWeapon.get(secondaryComboBox.getSelectionModel().getSelectedIndex());
    }
}

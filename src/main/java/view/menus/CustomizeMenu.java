package view.menus;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.weapons.*;

import java.util.ArrayList;
import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Luo aluksen muokkausvalikon.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class CustomizeMenu extends Menu {

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
     * Konstruktori. Luo komponentit ja lisää Groupiin. TODO
     */
    public CustomizeMenu(ResourceBundle messages) {

        // Valittavat aselistat
        secondaryWeapon = createPlayerSecondaries();
        primaryWeapon = createPlayerPrimaries();

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        Text primary1Text = new Text(messages.getString("primary_weapon"));
        primary1Text.setStyle("-fx-fill: white");

        Text secondaryText = new Text(messages.getString("secondary_weapon"));
        secondaryText.setStyle("-fx-fill: white");

        ArrayList<String>primaryWeaponNames = new ArrayList<>(primaryWeapon.size());
        for (Weapon w : primaryWeapon) {
            if (w instanceof Blaster) primaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) primaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) primaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) primaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) primaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) primaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapon.size());
        for (Weapon w : secondaryWeapon) {
            if (w instanceof Blaster) secondaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) secondaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) secondaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) secondaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) secondaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) secondaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
        }

        primaryComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeaponNames));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeaponNames.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

        primaryComboBox.setPrefWidth(Double.MAX_VALUE);
        secondaryComboBox.setPrefWidth(Double.MAX_VALUE);

        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(primary1Text, primaryComboBox, secondaryText, secondaryComboBox, backButton);

        borderPane.setCenter(vBox);

        getChildren().add(borderPane);

        //      click eventit

        // Main menun play click event
        backButton.setOnAction(event -> changeToNextMenu(new PlayMenu(messages)));
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

    /**
     * Luo listan valittavissa olevista pääaseista
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerPrimaries() {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon blaster = new Blaster(0, 45,  new Point2D(-15, 0), new Point2D(100, 0));

        Weapon rocketShotgun = new RocketShotgun(0, 0, 20,
                false, new Point2D(-15, 0), new Point2D(-15, 0));

        Weapon laserGun = new LaserGun(5, 0.5, new Point2D(-15, 0), new Point2D(80, 0));

        weapons.add(blaster);
        weapons.add(rocketShotgun);
        weapons.add(laserGun);
        return weapons;
    }

    /**
     * Luo listan valittavissa olevista sivuaseista
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerSecondaries() {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon rocketShotgun = new RocketShotgun(0, 0, 20,
                false, new Point2D(-15, 0), new Point2D(-15, 0));

        Weapon laserGun = new LaserGun(5, 0.5, new Point2D(-15, 0), new Point2D(80, 0));

        weapons.add(rocketShotgun);
        weapons.add(laserGun);
        return weapons;
    }
}

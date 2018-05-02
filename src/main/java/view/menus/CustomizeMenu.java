package view.menus;

import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.weapons.*;

import java.util.ArrayList;
import java.util.List;
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
     * Pääaseotsikko.
     */
    private Text primaryText;

    /**
     * Sivuaseotsikko.
     */
    private Text secondaryText;

    /**
     * Valittavien aseiden listat, joiden perusteella ComboBoxit tehdään.
     */
    private ArrayList<Weapon> primaryWeapons, secondaryWeapons;

    /**
     * ComboBox pääaseen valintaan.
     */
    private ComboBox<String> primaryComboBox;

    /**
     * ComboBox sivuaseen valintaan.
     */
    private ComboBox<String> secondaryComboBox;

    /**
     * Playmenu-menu
     */
    private PlayMenu playMenu;


    /**
     * Konstruktori. Luo komponentit ja lisää itseensä. Lisää myös click eventit.
     */
    public CustomizeMenu(ResourceBundle messages, MenuSpace menuSpace) {
        super(menuSpace);

        // Valittavat aselistat
        secondaryWeapons = createPlayerSecondaries();
        primaryWeapons = createPlayerPrimaries();

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT-BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        primaryText = new Text(messages.getString("primary_weapon"));
        primaryText.setStyle("-fx-fill: white");

        secondaryText = new Text(messages.getString("secondary_weapon"));
        secondaryText.setStyle("-fx-fill: white");

        ArrayList<String>primaryWeaponNames = new ArrayList<>(primaryWeapons.size());
        for (Weapon w : primaryWeapons) {
            if (w instanceof Blaster) primaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) primaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) primaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) primaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) primaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) primaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
            else if (w instanceof MachineGun) primaryWeaponNames.add(messages.getString("weapon_machine_gun"));
            else if (w instanceof PlasmaGun) primaryWeaponNames.add("PlasmaGun (yo taa pitää lisää lokaaleihin | CustomizeMenu.java)"); // TODO
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            if (w instanceof Blaster) secondaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) secondaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler) secondaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) secondaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) secondaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) secondaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
            else if (w instanceof MachineGun) secondaryWeaponNames.add(messages.getString("weapon_machine_gun"));
        }

        primaryComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeaponNames));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeaponNames.get(2));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

        primaryComboBox.setPrefWidth(Double.MAX_VALUE);
        secondaryComboBox.setPrefWidth(Double.MAX_VALUE);

        backButton = new Button(messages.getString("back"));
        backButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(150);
        vBox.getChildren().addAll(primaryText, primaryComboBox, secondaryText, secondaryComboBox, backButton);

        borderPane.setCenter(vBox);

        getChildren().add(borderPane);

                    //-- click eventit --//

        // Main menun play click event
        backButton.setOnAction(event -> getMenuSpace().changeToPreviousMenu(this, playMenu));
    }

    /**
     * Palauttaa ComboBoxissa valitun pääaseen.
     * @return Valittu pääase.
     */
    public Weapon getSelectedPrimaryWeapon() {
        return primaryWeapons.get(primaryComboBox.getSelectionModel().getSelectedIndex());
    }

    /**
     * Palauttaa ComboBoxissa valitun sivuaseen.
     * @return Valittu sivuase.
     */
    public Weapon getSelectedSecondaryWeapon() {
        return secondaryWeapons.get(secondaryComboBox.getSelectionModel().getSelectedIndex());
    }

    /**
     * Luo listan valittavissa olevista pääaseista
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerPrimaries() {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon blaster = new Blaster(0, 40, 0.5,  new Point2D(-15, 0), new Point2D(100, 0));

        Weapon rocketShotgun = new RocketShotgun(0, 0, 3, 20,
                false, new Point2D(-15, 0), new Point2D(-15, 0));

        Weapon laserGun = new LaserGun(5, 0.5, 0.2, new Point2D(-15, 0), new Point2D(80, 0));

        Weapon rocketLauncher = new RocketLauncher(5, 20, 4, false);

        // TODO: machine gun offsets
        Weapon machineGun = new MachineGun(0, 55, 0.05);

        Weapon plasmaGun = new PlasmaGun(5, 0.5, 0.2, new Point2D(-15, 0), new Point2D(80, 0));

        weapons.add(blaster);
        weapons.add(laserGun);
        weapons.add(machineGun);
        weapons.add(rocketLauncher);
        weapons.add(rocketShotgun);
        weapons.add(plasmaGun);
        return weapons;
    }

    /**
     * Luo listan valittavissa olevista sivuaseista
     * @return Lista, joka sisältää aseita
     */
    private ArrayList<Weapon> createPlayerSecondaries() {
        ArrayList<Weapon> weapons = new ArrayList<>();

        Weapon blaster = new Blaster(0, 40, 0.5,  new Point2D(-15, 0), new Point2D(100, 0));

        Weapon rocketShotgun = new RocketShotgun(0, 0, 3, 20,
                false, new Point2D(-15, 0), new Point2D(-15, 0));

        Weapon laserGun = new LaserGun(5, 0.5, 0.8, new Point2D(-15, 0), new Point2D(80, 0));

        Weapon blasterSprinkler = new BlasterSprinkler(2, 20, 2, 1);

        // TODO: machine gun offsets
        Weapon machineGun = new MachineGun(0, 55, 0.05);

        weapons.add(blaster);
        weapons.add(rocketShotgun);
        weapons.add(laserGun);
        weapons.add(blasterSprinkler);
        weapons.add(machineGun);
        return weapons;
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        setWeaponSpinners(messages);
        primaryText.setText(messages.getString("primary_weapon"));
        secondaryText.setText(messages.getString("secondary_weapon"));
        backButton.setText(messages.getString("back"));
    }

    public void setPlayMenu(PlayMenu playMenu) {
        this.playMenu = playMenu;
    }

    /**
     * Asettaa aseidenvalintaspinnereiden aseiden nimet lokaalin mukaan
     *
     * @param messages Lokalisoidut resurssit.
     */
    private void setWeaponSpinners(ResourceBundle messages) {
        List<String> primaryWeaponNames = new ArrayList<>(primaryWeapons.size());
        for (Weapon w : primaryWeapons) {
            if (w instanceof Blaster) primaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun) primaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler)
                primaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) primaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher) primaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) primaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
            else if (w instanceof MachineGun) primaryWeaponNames.add(messages.getString("weapon_machine_gun"));

        }

        List<String> secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            if (w instanceof Blaster) secondaryWeaponNames.add(messages.getString("weapon_blaster"));
            else if (w instanceof BlasterShotgun)
                secondaryWeaponNames.add(messages.getString("weapon_blaster_shotgun"));
            else if (w instanceof BlasterSprinkler)
                secondaryWeaponNames.add(messages.getString("weapon_blaster_sprinkler"));
            else if (w instanceof LaserGun) secondaryWeaponNames.add(messages.getString("weapon_laser_gun"));
            else if (w instanceof RocketLauncher)
                secondaryWeaponNames.add(messages.getString("weapon_rocket_launcher"));
            else if (w instanceof RocketShotgun) secondaryWeaponNames.add(messages.getString("weapon_rocket_shotgun"));
            else if (w instanceof MachineGun) secondaryWeaponNames.add(messages.getString("weapon_machine_gun"));
        }

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeaponNames));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeaponNames.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));
    }
}

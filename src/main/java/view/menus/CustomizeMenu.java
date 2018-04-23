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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static view.GameMain.*;

/**
 * Luo aluksen muokkausvalikon.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class CustomizeMenu implements Menu {

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
    private List<Weapon> primaryWeapons, secondaryWeapons;

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
     *
     * @param messages         Lokalisoidut resurssit.
     * @param primaryWeapons   Lista valittavista pääaseista.
     * @param secondaryWeapons Lista valittavista sivuaseista.
     */
    public CustomizeMenu(ResourceBundle messages, ArrayList<Weapon> primaryWeapons, ArrayList<Weapon> secondaryWeapons) {
        this.primaryWeapons = primaryWeapons;
        this.secondaryWeapons = secondaryWeapons;

        customizeMenuGroup = new Group();
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT - BANNER_HEIGHT);
        borderPane.setStyle("-fx-background-color: black");

        primaryText = new Text(messages.getString("primary_weapon"));
        primaryText.setStyle("-fx-fill: white");

        secondaryText = new Text(messages.getString("secondary_weapon"));
        secondaryText.setStyle("-fx-fill: white");

        primaryComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();
        setWeaponSpinners(messages);

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

        customizeMenuGroup.getChildren().add(borderPane);
    }

    @Override
    public void changeLocale(ResourceBundle messages) {
        setWeaponSpinners(messages);
        primaryText.setText(messages.getString("primary_weapon"));
        secondaryText.setText(messages.getString("secondary_weapon"));
        backButton.setText(messages.getString("back"));
    }

    @Override
    public Group getGroup() {
        return customizeMenuGroup;
    }

    /**
     * Palauttaa ComboBoxissa valitun pääaseen.
     *
     * @return Valittu pääase.
     */
    public Weapon getSelectedPrimaryWeapon() {
        return primaryWeapons.get(primaryComboBox.getSelectionModel().getSelectedIndex());
    }

    /**
     * Palauttaa ComboBoxissa valitun sivuaseen.
     *
     * @return Valittu sivuase.
     */
    public Weapon getSelectedSecondaryWeapon() {
        return secondaryWeapons.get(secondaryComboBox.getSelectionModel().getSelectedIndex());
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
        }

        primaryComboBox.setItems(FXCollections.observableArrayList(primaryWeaponNames));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primaryComboBox.setValue(primaryWeaponNames.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));
    }

}

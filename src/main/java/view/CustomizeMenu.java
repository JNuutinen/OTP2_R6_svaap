package view;

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

public class CustomizeMenu {
    Button backButton;

    private ArrayList<Weapon> primaryWeapon, secondaryWeapon;
    private ComboBox<String> primary1ComboBox;
    private ComboBox<String> primary2ComboBox;
    private ComboBox<String> secondaryComboBox;

    private Group customizeMenuGroup;

    CustomizeMenu(ArrayList<Weapon> primaryWeapons1, ArrayList<Weapon> secondaryWeapons) {
        this.primaryWeapon = primaryWeapons1;
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

        ArrayList<String>primaryWeapon1Names = new ArrayList<>(primaryWeapons1.size());
        for (Weapon w : primaryWeapons1) {
            primaryWeapon1Names.add(((Component) w).getName());
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            secondaryWeaponNames.add(((Component) w).getName());
        }

        primary1ComboBox = new ComboBox<>();
        primary2ComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primary1ComboBox.setItems(FXCollections.observableArrayList(primaryWeapon1Names));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primary1ComboBox.setValue(primaryWeapon1Names.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

        primary1ComboBox.setPrefWidth(Double.MAX_VALUE);
        primary2ComboBox.setPrefWidth(Double.MAX_VALUE);
        secondaryComboBox.setPrefWidth(Double.MAX_VALUE);

        backButton = new Button("Back");
        backButton.setPrefWidth(Double.MAX_VALUE);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxWidth(100);
        vBox.getChildren().addAll(primary1Text, primary1ComboBox, primary2Text, primary2ComboBox, secondaryText,
                secondaryComboBox, backButton);

        borderPane.setCenter(vBox);

        customizeMenuGroup.getChildren().add(borderPane);
    }

    Group getGroup() {
        return customizeMenuGroup;
    }

    Weapon getSelectedPrimaryWeapon1() {
        String selected = primary1ComboBox.getValue();
        for (Weapon w : primaryWeapon) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }

    Weapon getSelectedSecondaryWeapon() {
        String selected = secondaryComboBox.getValue();
        for (Weapon w : secondaryWeapon) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }
}

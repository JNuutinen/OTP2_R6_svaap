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

    private ArrayList<Weapon> p1, p2, s;
    private ComboBox<String> primary1ComboBox;
    private ComboBox<String> primary2ComboBox;
    private ComboBox<String> secondaryComboBox;

    private Group customizeMenuGroup;

    CustomizeMenu(ArrayList<Weapon> primaryWeapons1, ArrayList<Weapon> primaryWeapons2, ArrayList<Weapon> secondaryWeapons) {
        p1 = primaryWeapons1;
        p2 = primaryWeapons2;
        s = secondaryWeapons;

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

        ArrayList<String>primaryWeapon2Names = new ArrayList<>(primaryWeapons2.size());
        for (Weapon w : primaryWeapons2) {
            primaryWeapon2Names.add(((Component) w).getName());
        }

        ArrayList<String>secondaryWeaponNames = new ArrayList<>(secondaryWeapons.size());
        for (Weapon w : secondaryWeapons) {
            secondaryWeaponNames.add(((Component) w).getName());
        }

        primary1ComboBox = new ComboBox<>();
        primary2ComboBox = new ComboBox<>();
        secondaryComboBox = new ComboBox<>();

        primary1ComboBox.setItems(FXCollections.observableArrayList(primaryWeapon1Names));
        primary2ComboBox.setItems(FXCollections.observableArrayList(primaryWeapon2Names));
        secondaryComboBox.setItems(FXCollections.observableArrayList(secondaryWeaponNames));

        primary1ComboBox.setValue(primaryWeapon1Names.get(0));
        primary2ComboBox.setValue(primaryWeapon2Names.get(0));
        secondaryComboBox.setValue(secondaryWeaponNames.get(0));

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
        for (Weapon w : p1) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }

    Weapon getSelectedPrimaryWeapon2() {
        String selected = primary2ComboBox.getValue();
        for (Weapon w : p2) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }

    Weapon getSelectedSecondaryWeapon() {
        String selected = secondaryComboBox.getValue();
        for (Weapon w : s) {
            if (((Component) w).getName().equals(selected)) {
                return w;
            }
        }
        return null;
    }
}

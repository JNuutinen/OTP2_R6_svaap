package model;

import controller.Controller;
import controller.GameController;
import javafx.scene.paint.Color;
import model.units.Player;
import model.weapons.Blaster;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerFactory {
    static Controller controller = GameController.getInstance();

    public static Player getPlayer(Color color) {
        //      Pelaaja
        Player player = new Player(color);
        player.setPosition(200, 400);

        //      tieto controllerille pelaajasta
        controller.addPlayers(new ArrayList<>(Arrays.asList(player)));
        controller.setToScene(player);

        //      pelaajalle pyssyt
        player.addPrimaryWeapon(new Blaster(1,30 , 1));
        System.out.println("New player created");
        return player;
    }

    public static Player getPlayer(Color color, Weapon primary, Weapon secondary) {
        //      Pelaaja
        Player player = new Player(color);
        player.setPosition(200, 400);

        //      tieto controllerille pelaajasta
        controller.addPlayers(new ArrayList<>(Arrays.asList(player)));
        controller.addUpdateableAndSetToScene(player);

        //      pelaajalle pyssyt
        player.addPrimaryWeapon(primary);
        player.setSecondaryWeapon(secondary);
        System.out.println("New player created");
        return player;
    }

}

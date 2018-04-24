package model;

import controller.Controller;
import controller.GameController;
import javafx.scene.paint.Color;
import model.units.Player;
import model.weapons.Blaster;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerFactory {
    static Controller controller = GameController.getInstance();

    public static Player getPlayer(Color color) {
        //      Pelaaja
        Player player = new Player(Color.BLUE);
        player.setPosition(100, 300);

        //      tieto controllerille pelaajasta
        controller.addPlayers(new ArrayList<Player>(Arrays.asList(player)));
        controller.addUpdateableAndSetToScene(player);
        controller.addHitboxObject(player);

        //      pelaajalle pyssyt
        player.addPrimaryWeapon(new Blaster(1,10));
        System.out.println("New player created");
        return player;
    }
}

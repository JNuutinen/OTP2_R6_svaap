package Multiplayer;

import javafx.scene.paint.Color;
import model.units.Player;
import model.units.Unit;

import java.util.HashMap;

public class Multiplayer {
    static HashMap<Integer, Unit> players = new HashMap<>();


    /*
    public void shoot(Unit unit) {
        Data data = new ShootData(unit);
        Host.streamOut(data);
    }
    */

    public static Unit getPlayerById(int id) {
            return players.get(id);
    }

    public static void connect(Player player) {
        players.put(player.getPlayerId(), player);
        Host.connect();
        Host.streamOut(new ConnectionData(player.getPlayerId()));
    }
    /*
    public static void addPlayer(int playerId) {
        Player player = PlayerFactory.getPlayer(Color.WHITE);
        players.put(playerId, player);
    }
    */
}

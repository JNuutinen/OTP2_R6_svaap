package Multiplayer;
import model.Player;
import model.Unit;

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
        players.put(player.getPlayerId(), (Unit)player);
    }
}

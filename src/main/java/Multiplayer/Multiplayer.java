package Multiplayer;

import javafx.scene.paint.Color;
import model.PlayerFactory;
import model.units.Player;
import model.units.Unit;
import view.GameMain;

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

    public static void connect() { //Liitytään muiden peliin
        Player player = GameMain.player;
        players.put(player.getPlayerId(), player); //lisää oman instanssinsa playerin, listaan
        Host.connect();  // Yhdistää aukinaiseen sockettiin
        Host.streamOut(new ConnectionData(player.getPlayerId()));  // lähettää tiedon itsestään eteenpäin
    }

    public static void addPlayer(int playerId) {
        Player player = PlayerFactory.getPlayer(Color.WHITE); //Ulkoinen pelaaja liittyy omaan peliin
        players.put(playerId, player);
    }
}

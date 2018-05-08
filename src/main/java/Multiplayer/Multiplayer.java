package Multiplayer;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.PlayerFactory;
import model.units.Player;
import model.units.Unit;
import model.weapons.Blaster;
import model.weapons.MachineGun;
import model.weapons.RocketShotgun;
import view.GameMain;

import java.util.HashMap;

public class Multiplayer {
    static HashMap<Integer, Unit> players = new HashMap<>();
    static boolean connected;


    //TODO: connected stateks
    public static void shootSecondary() {
        if (connected) {
            Sender.streamOut(new ShootSecondaryData(GameMain.player.getPlayerId()));
        }
    }

    public static void shootPrimary() {
        if (connected) {
            Sender.streamOut(new ShootPrimaryData(GameMain.player.getPlayerId()));
        }
    }
    public static void move(double x, double y) {
        if (connected) {
            Sender.streamOut(new MoveData(GameMain.player.getPlayerId(), x, y));
        }
    }

    public static Unit getPlayerById(int id) {
        return players.get(id);
    }

    public static void connect() { //Liitytään muiden peliin
        Player player = GameMain.player;
        players.put(player.getPlayerId(), player); //lisää oman instanssinsa playerin, listaan
       Sender.streamOut(new ConnectionData(player.getPlayerId()));  // lähettää tiedon itsestään eteenpäin
        connected = true;
        System.out.println("connection packet sent...");
    }

    public static void addPlayer(int playerId) {

        Player player = PlayerFactory.getPlayer(Color.WHITE, new Blaster(0, 40, 0.5,  new Point2D(-15, 0), new Point2D(100, 0)),new RocketShotgun(0, 0, 3, 20,
                false, new Point2D(-15, 0), new Point2D(-15, 0))); //Ulkoinen pelaaja liittyy omaan peliin
        players.put(playerId, player);
    }
}

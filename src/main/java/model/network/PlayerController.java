package model.network;

import javafx.scene.input.KeyCode;
import model.units.Player;
import model.weapons.Weapon;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static view.GameMain.PLAYER_X_LIMIT;
import static view.GameMain.WINDOW_HEIGHT;

public class PlayerController {

    /**
     * Lista, joka sisältää tietyllä hetkellä painetut näppäimet.
     */
    private List<String> input = new ArrayList<>();

    private Socket connectionToTheServer;

    private Player player;

    public PlayerController(){

        /*
        try {
            connectionToTheServer = new Socket("localhost", 8888);
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream out = null;
        try {
            out = connectionToTheServer.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
        ps.println("tää viesti tuli clientiltä!");
        // Now, you don't have to flush it, because of the auto-flush flag we turned on.*/
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public List<String> getInput() {
        return input;
    }

    public void addInput(String input){
        if(player != null){
            player.addInput(input);
        }
    }
}

package Multiplayer;

import controller.GameController;
import javafx.scene.paint.Color;
import model.PlayerFactory;
import model.units.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Host {
    static String ip = "10.112.195.107";
    static int port = 1111;
    static DataInputStream in;
    static ObjectOutputStream out;

    public Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Host (){}

    public static void connect() {
        try {
            Socket s = new Socket(ip, port);
            out = new ObjectOutputStream(s.getOutputStream());

            Player player2 = PlayerFactory.getPlayer(Color.WHITE);

            System.out.println("Connected");
        } catch (IOException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Not connected");
        }
    }

    public static void streamOut(Data data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

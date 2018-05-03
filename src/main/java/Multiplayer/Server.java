package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server  {

    public Server() {}

    public boolean startServer() {
        System.out.println("Waiting for players...");
        try {
            ServerSocket serv = new ServerSocket(1111);
            Socket s = serv.accept();
            System.out.println("Client connected...");


            Reciever r = new Reciever();
            r.init(new ObjectInputStream(s.getInputStream()));
            (new Thread(r)).start();

            Sender.init(new ObjectOutputStream(s.getOutputStream()));

            System.out.println("Connected");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection failed");
            return false;
        }
    }

    public void startMultiplayer() {
        Multiplayer.connect();
    }
}

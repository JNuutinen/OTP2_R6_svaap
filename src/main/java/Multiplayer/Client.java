package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client  {
    String ip = "192.168.43.224";
    int port = 1111;


    public Client(){}

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean connect() {
        System.out.println("Connecting to server...");
        try {

            Socket s = new Socket(ip, port);

            Sender.init(new ObjectOutputStream(s.getOutputStream()));

            Reciever r = new Reciever();
            r.init(new ObjectInputStream(s.getInputStream()));
            (new Thread(r)).start();

            Multiplayer.connect();

            System.out.println("Connected");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection failed");
            return false;
        }
    }
}

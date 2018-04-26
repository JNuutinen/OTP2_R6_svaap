package Multiplayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {
    static String ip = "192.168.43.224";
    static int port = 1111;
    static ObjectInputStream in;
    static ObjectOutputStream out;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Client(){}

    public static void connect() {
        System.out.println("Connecting to server...");
        try {

            Socket s = new Socket(ip, port);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            Multiplayer.connect();

            System.out.println("Connected");
        } catch (IOException ex) {
            Logger.getLogger(Multiplayer.Client.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Not connected");
        }
    }

    public static void streamOut(Data data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Multiplayer.Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Boolean stop = false;
        System.out.println("Server running...");
        while (!stop) {
            try {

                /*
                Data[] data = (Data[])in.readObject();
                for (Data d : data) {
                    d.action();
                }
                */

                Data data = (Data)in.readObject();
                data.action();
                //stop = true;

            } catch (IOException ex) {
                Logger.getLogger(Multiplayer.Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

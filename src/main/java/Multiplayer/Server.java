package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


//Tää pitää käynnistää ennen Hostia.

public class Server implements Runnable {
    ObjectInputStream in;

    public Server() {}

    public void startServer() {
        System.out.println("Waiting for players...");
        try {
            ServerSocket serv = new ServerSocket(1111);
            Socket s = serv.accept();
            System.out.println("Client connected...");
            in = new ObjectInputStream(s.getInputStream());


        } catch (IOException ex) {
            Logger.getLogger(Multiplayer.Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection failed");
        }
    }



    @Override
    public void run() {
        Boolean stop = false;
        System.out.println("Listening for packets...");
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

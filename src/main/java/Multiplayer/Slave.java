package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


//Tää pitää käynnistää ennen Hostia.

public class Slave implements Runnable {
    ObjectInputStream in;

    public Slave() {}

    public void connect() {
        System.out.println("Slaving...");
        try {
            ServerSocket serv = new ServerSocket(1111);
            Socket s = serv.accept();
            System.out.println("Slave connected...");
            in = new ObjectInputStream(s.getInputStream());


        } catch (IOException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection failed");
        }
    }



    @Override
    public void run() {
        Boolean stop = false;
        System.out.println("Slave running...");
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
                Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

package Multiplayer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


//Tää pitää käynnistää ennen Hostia.

public class Slave implements Runnable {
    //DataInputStream in;
    ObjectInputStream in;

    public Slave() {}

    public void connect() {
        try {
            ServerSocket serv = new ServerSocket(1111);
            Socket s = serv.accept();
            //in = new DataInputStream(s.getInputStream());

           in = new ObjectInputStream(s.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    @Override
    public void run() {
        Boolean stop = false;
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

            } catch (IOException ex) {
                Logger.getLogger(Slave.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

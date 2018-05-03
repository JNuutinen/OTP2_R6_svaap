package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reciever implements Runnable {
    ObjectInputStream in;

    public void init(ObjectInputStream in){
        this.in = in;
    }
    @Override
    public void run() {
        Boolean stop = false;
        System.out.println("Listening for packets...");
        while (!stop) {
            try {

                Data data = (Data)in.readObject();
                data.action();
                //stop = true;

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

package Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    static ObjectOutputStream out;

    public static void init(ObjectOutputStream o) {
        out = o;
    }

    public static void streamOut(Data data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

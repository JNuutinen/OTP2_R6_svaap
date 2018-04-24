package Multiplayer;

import java.io.Serializable;

public abstract class Data implements Serializable {
    int playerId;

    public void action(){
        System.out.println("Abstract data");
    }
}

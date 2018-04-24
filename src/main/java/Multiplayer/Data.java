package Multiplayer;

import java.io.Serializable;

public abstract class Data implements Serializable {
    int PlayerId;

    public void action(){
        System.out.println("Abstract data");
    }
}

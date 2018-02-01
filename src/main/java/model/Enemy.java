package model;

import view.GameMain;

public class Enemy extends Unit implements Updateable {

    public Enemy() {
        GameMain.units.add(this);
    }


    public void update(){

    }
}

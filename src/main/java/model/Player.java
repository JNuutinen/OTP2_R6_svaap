package model;

import view.GameMain;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Player extends Unit implements Updateable {

    public Player (){
        GameMain.units.add(this);
    }

    @Override
    public void update(double deltaTime){
        moveStep(deltaTime);
    }


}

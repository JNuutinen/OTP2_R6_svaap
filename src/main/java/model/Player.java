package model;


import view.GameMain;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/*
    TODO: tänne jotain pelaajaan liittyvää
 */
public class Player extends Unit implements Updateable {


    public Player (){
        GameMain.units.add(this);
    }

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < 100
                || getYPosition() > WINDOW_HEIGHT+100) {
            GameLoop.removeUpdateable(this);
        } else {
            moveStep(deltaTime);
        }
    }


}

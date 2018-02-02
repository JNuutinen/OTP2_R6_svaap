package model;


import view.GameMain;

/*
    TODO: tänne jotain pelaajaan liittyvää
 */
public class Player extends Unit implements Updateable {


    public Player (){
        GameMain.units.add(this);
    }

    @Override
    public void update(double deltaTime){
        moveStep(deltaTime);

    }


}

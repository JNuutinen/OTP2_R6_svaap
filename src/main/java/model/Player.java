package model;


import view.GameMain;

/*
    TODO: tänne jotain pelaajaan liittyvää
 */
public class Player extends Unit implements Updateable {


    public Player (){
        GameMain.units.add(this);
    }

    public void update(){
        moveStep();

    }

    public void testeri(){
        //
    }


}

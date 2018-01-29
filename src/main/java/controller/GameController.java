package controller;

import model.Enemy;
import model.Player;
import model.Unit;
import view.GameMain;

public class GameController {

    private GameMain gameMain;
    private Enemy enemy;
    private Player player1;
    private Unit unit;

    public GameController(GameMain gameMain){//konstruktori
        this.gameMain = gameMain;
    }

    public GameController(){//tyhjä konstruktori
    }

    public void initOthers(Enemy enemy, Player player1, Unit unit){
        this.enemy = enemy;
        this.player1 = player1;
        this.unit = unit;
    }


}

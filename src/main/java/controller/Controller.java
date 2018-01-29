package controller;

import model.Enemy;
import model.Player;
import model.Unit;

public interface Controller {
    void initOthers(Enemy enemy, Player player1, Unit unit);
}

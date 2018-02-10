package controller;

import model.Player;
import model.Unit;
import model.Updateable;

import java.util.ArrayList;

public interface Controller {
    void addPlayer(Player player);
    void addScore(int score);
    void addUpdateable(Updateable updateable);
    void addUnitToCollisionList(Unit unit);
    int getScore();
    ArrayList<Unit> getCollisionList();
    void startLevel(int levelNumber);
    void startLoop();
    void setFps(double fps);
    void removeUpdateable(Updateable updateable);
}

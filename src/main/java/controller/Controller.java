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
    void removeFromCollisionList(Unit unit);
    int getScore();
    ArrayList<Unit> getCollisionList();
    ArrayList<Updateable> getUpdateables();
    void startLevel(int levelNumber);
    void startLoop();
    void setFps(double fps);
    void setCurrentFps(double fps);
    void removeUpdateable(Updateable updateable);
    void returnToMain();
    void setHealthbar(int hp);
    void pauseGame();
    void continueGame();
}

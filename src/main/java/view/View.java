package view;

import model.Unit;

import java.util.ArrayList;

public interface View {
    void addUnitToCollisionList(Unit unit);
    void removeFromCollisionList(Unit unit);
    ArrayList<Unit> getCollisionList();
    void setFps(double fps);
    void setCurrentFps(double fps);
    void setScore(int score);
    void returnToMain();
    void setHealthbar(int hp);
}

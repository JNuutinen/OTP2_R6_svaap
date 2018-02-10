package view;

import model.Sprite;
import model.Unit;

import java.util.ArrayList;

public interface View {
    void addSprite(Sprite sprite);
    void addUnitToCollisionList(Unit unit);
    ArrayList<Unit> getCollisionList();
    void removeSprite(Sprite sprite);
    void setFps(double fps);
    void setScore(int score);
}

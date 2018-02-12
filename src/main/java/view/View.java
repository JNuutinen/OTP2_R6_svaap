package view;

import javafx.stage.Stage;
import model.Sprite;
import model.Unit;

import java.util.ArrayList;

public interface View {
    void addSprite(Sprite sprite);
    void addUnitToCollisionList(Unit unit);
    void removeFromCollisionList(Unit unit);
    ArrayList<Unit> getCollisionList();
    void removeSprite(Sprite sprite);
    void setFps(double fps);
    void setScore(int score);
    void returnToMain();

}

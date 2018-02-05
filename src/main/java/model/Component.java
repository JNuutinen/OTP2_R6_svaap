package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import model.Sprite;

public class Component extends Sprite {
    double width = 60;
    double height = 60;
    Point2D startingPosition;
    private double xVelocity;
    private double yVelocity;
    public String imagePath;

    //UnitX & UnitY: Spawnataan komponentti samaan paikkaan isännänsä kanssa
    public Component(String imagePath) {
        this.imagePath = imagePath;
    }

    public void addVelocity(double x, double y) {
        xVelocity += x;
        yVelocity += y;
    }

    public void resetVelocity() {
        xVelocity = 0;
        yVelocity = 0;
    }


}

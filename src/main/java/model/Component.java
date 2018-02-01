package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import model.Sprite;

public class Component extends Sprite{
    double width;
    double height;
    Point2D startingPosition;

    public Component(int unitX, int unitY, double width, double height, String imagePath) {
        spawn(unitX, unitY, width, height, imagePath);
    }

    public void spawn(int unitX, int unitY, double width, double height, String imagePath) {
        startingPosition = new Point2D(unitX, unitY);
        this.setImage(new Image(getClass().getResourceAsStream(imagePath), width, height, true, true));
        this.setPosition(startingPosition.getX(), startingPosition.getY());
    }
}

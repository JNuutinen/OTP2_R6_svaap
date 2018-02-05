package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import model.Sprite;

public class Component extends Sprite{
    double width;
    double height;
    Point2D startingPosition;

    //UnitX & UnitY: Spawnataan komponentti samaan paikkaan isännänsä kanssa
    public Component(double unitX, double unitY) {
        spawn(unitX, unitY, 50, 50);
    }

    public void spawn(double unitX, double unitY, double width, double height) {

        String imagePath = "/images/player_ship_9000.png";
        startingPosition = new Point2D(unitX + 10, unitY + 10);
        this.setImage(new Image(getClass().getResourceAsStream(imagePath), width, height, true, true));
        this.setPosition(startingPosition.getX(), startingPosition.getY());
    }
}

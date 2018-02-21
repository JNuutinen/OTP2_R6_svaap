package model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public interface Updateable {
    void update(double deltaTime);

    Shape getHitboxShape();

    void collides(Updateable collidingUpdateable);

    String getTag();

    void setTag(String tag);

    Point2D getPosition();
}

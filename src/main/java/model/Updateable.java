package model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public interface Updateable {
    public void update(double deltaTime);

    public Shape getHitboxShape();

    public void collides(Updateable collidingUpdateable);

    public Updateable getUpdateable();

    public String getTag();

    public void setTag(String tag);

    public Point2D getPosition();
}

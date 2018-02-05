package model;

import javafx.scene.shape.Shape;

public interface Updateable {
    public void update(double deltaTime);

    public Shape getSpriteShape();

    public void collides(Updateable collidingUpdateable);

    public Updateable getUpdateable();

    public String getTag();

    public void setTag(String tag);
}

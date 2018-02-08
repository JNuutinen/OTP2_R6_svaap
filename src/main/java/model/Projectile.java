package model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;
import view.GameMain;
import model.Sprite;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Projectile extends Sprite implements Updateable{
    private int damage;
    private boolean hit = false;
    private int speed = 300;

    public Projectile(Point2D startingLocation, double direction, int damage, String tag){
        this.setPosition(startingLocation.getX(), startingLocation.getY());
        this.setDirection(direction);
        this.setTag(tag);
        this.damage = damage;
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        this.setImage(projectileImage);

        setVelocity(speed);
        setIsMoving(true);
        GameLoop.queueUpdateable(this);
    }

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            move(deltaTime);
        }
    }

    //kutsutaan aina kun osuu johonkin. tehty niin etta tietyt luokat esim. vihun ammus ja vihu ei voi osua toisiinsa.
    public void collides(Updateable collidingUpdateable){

        destroyThis();
        for (Unit unit : GameMain.units) {
            if (unit == collidingUpdateable) {
                unit.takeDamage(damage);
            }
        }
    }

    public Updateable getUpdateable(){
        return this;
    }


    public void move(double deltaTime) {
        if (!hit) {
            moveStep(deltaTime);
        }
    }

    public void destroyThis(){
        GameLoop.removeUpdateable(this);
        GameMain.removeSprite(this);
    }
}

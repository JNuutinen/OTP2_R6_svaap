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

    /*
    public Projectile(int damage, Unit shooter, String tag){
        this.setTag(tag);
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        this.setImage(projectileImage);
        this.damage = damage;
        double xPos;
        if (shooter instanceof Player) {
            xPos = shooter.getXPosition() + 50;
            setPosition(xPos, shooter.getYPosition());
            setDirection(0);
        } else if (shooter instanceof Enemy) {
            xPos = shooter.getXPosition() - 50;
            setPosition(xPos, shooter.getYPosition());
            setDirection(180);
        }
        setVelocity(speed);
        setIsMoving(true);
        GameLoop.queueUpdateable(this);
    }*/

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            GameLoop.removeUpdateable(this);
            destroyThis();
        } else {
            move(deltaTime);
        }
    }

    //kutsutaan aina kun osuu johonkin. tehty niin etta tietyt luokat esim. vihun ammus ja vihu ei voi osua toisiinsa.
    public void collides(Updateable collidingUpdateable){

        /*if(collidingUpdateable.getTag().equals("player")){
            //
        }*/
        GameLoop.removeUpdateable(this);
        delete(this);
        for(Unit unit : GameMain.units){
            if (unit == collidingUpdateable) {
                System.out.println("ammus osui tahan: " + collidingUpdateable.getTag());
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

package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Projectile extends Sprite implements Updateable {
    private Controller controller;
    private int damage;
    private boolean hit = false;
    private int speed = 35;

    public Projectile(Controller controller, Point2D startingLocation, double direction, int damage, String tag){
        this.controller = controller;
        this.setPosition(startingLocation.getX(), startingLocation.getY());
        this.setDirection(direction);
        this.setTag(tag);
        this.damage = damage;
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        this.setImage(projectileImage);

        setVelocity(speed);
        setIsMoving(true);
    }

    public Projectile(Controller controller, Point2D startingLocation, double direction, int damage, String tag, Unit shooter){
        this.controller = controller;
        this.setDirection(direction);

        //Projektile lähtee aluksen kärjestä. Viholliset ja pelaaja erikseen
        if (direction <= 90 || direction >= 270) { //Jos ampuja on pelaaja (kulkee vasemmalta oikealle)
            this.setPosition(startingLocation.getX() + shooter.getWidth(), startingLocation.getY());
        } else { //Jos ampuja on joku muu (kulkee oikealta vasemmalle)
            this.setPosition(startingLocation.getX() - shooter.getWidth(), startingLocation.getY());
        }
        this.setTag(tag);
        this.damage = damage;
        Image projectileImage = new Image("/images/projectile_ball_small_cyan.png");
        //this.setImage(projectileImage);

        setVelocity(speed);
        setIsMoving(true);

        this.setHitbox(10);
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
            move(deltaTime * getVelocity());
        }
    }

    //kutsutaan aina kun osuu johonkin. tehty niin etta tietyt luokat esim. vihun ammus ja vihu ei voi osua toisiinsa.
    public void collides(Updateable collidingUpdateable){

        destroyThis();
        for (Unit unit : controller.getCollisionList()) {
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
        controller.removeUpdateable(this);
    }
}

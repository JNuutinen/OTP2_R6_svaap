package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Missile extends Sprite implements Updateable {
    private Controller controller;
    private int damage;
    private boolean hit = false;
    private int speed = 20;

    private double rotatingSpeed = 9;
    private Updateable target;


    public Missile(Controller controller, Point2D startingLocation, double speed, double direction, int damage, String tag, Unit shooter, Color color){
        this.controller = controller;
        this.rotate(direction);
        this.setTag(tag);
        this.damage = damage;
        setVelocity(speed);
        setIsMoving(true);
        this.setHitbox(10);
        findAndSetTarget();

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(-6.0, 0.0,
                0.0, -3.0,
                speed*0.6+1.0, 0.0, // ammuksen hanta skaalautuu nopeuden mukaan, mutta on ainakin 1.0
                0.0, 3.0);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        triangle.setEffect(blur);
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStroke(color);
        triangle.setStrokeWidth(5.0);
        triangle.getTransforms().add(new Rotate(180, 0, 0));
        this.getChildren().add(triangle);

        //Projektile lähtee aluksen kärjestä. Viholliset ja pelaaja erikseen
        if (direction <= 90 || direction >= 270) { //Jos ampuja on pelaaja (kulkee vasemmalta oikealle)
            this.setPosition(startingLocation.getX() + shooter.getWidth(), startingLocation.getY());
        } else { //Jos ampuja on joku muu (kulkee oikealta vasemmalle)
            this.setPosition(startingLocation.getX() - shooter.getWidth(), startingLocation.getY());
        }
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


        double angleToTarget = 180;



        if(target != null) {
            angleToTarget = getAngleFromTarget(target.getPosition()) - getDirection();
            // taa vaa pitaa asteet -180 & 180 valissa
            while (angleToTarget >= 180.0) {
                angleToTarget -= 360.0;
            }
            while (angleToTarget < -180) {
                angleToTarget += 360.0;
            }
            rotate(angleToTarget * rotatingSpeed * deltaTime);
        }
        else{
            findAndSetTarget();
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

    public void findAndSetTarget(){
        for(Updateable updateable : controller.getUpdateables()){
            if(updateable.getTag().equals("enemy")) {
                target = updateable;
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



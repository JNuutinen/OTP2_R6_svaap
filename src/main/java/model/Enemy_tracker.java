package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Enemy_tracker extends Unit implements Updateable {
    public static final int MOVE_NONE = -1;
    public static final int MOVE_STRAIGHT = 0;
    public static final int MOVE_SINE = 1;

    private Controller controller;
    private double initialX;
    private double initialY;
    private int movementPattern;
    private double direction = 180;
    private Weapon weapon1;
    private Weapon weapon2;

    private Polygon triangle;

    // Ampumisen kovakoodit
    private int fireRate = 100;
    private int fireRateCounter = 100;

    public Enemy_tracker(Controller controller) {
        super(controller);
        this.controller = controller;
        controller.addUnitToCollisionList(this);
        setDirection(180);
        this.setTag("enemy");
    }

    public Enemy_tracker(Controller controller, Point2D[] path, double initialX, double initialY,
                         String tag) {
        super(controller);
        this.controller = controller;
        this.setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        setDirection(180);
        setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;

        triangle = new Polygon();
        triangle.getPoints().addAll(45.0, -25.0,
                -45.0, 0.0,
                45.0, 25.0);
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStroke(Color.RED);
        triangle.setStrokeWidth(2.0);
        this.getChildren().add(triangle);
        this.setHitbox(25);
    }

    @Override
    public void update(double deltaTime){
        if (fireRateCounter <= fireRate) fireRateCounter++;
        if (fireRateCounter >= fireRate) {
            fireRateCounter = 0;
            //spawnProjectile();
        }
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            //this.getTransforms().add(new Rotate(2, 45, 25, 0, Rotate.Z_AXIS));
            moveStep(deltaTime);
        }
    }


    public void getToPoint(Point2D destination){

    }

    public void rotate(double degrees){

    }

    public Updateable getUpdateable(){
        return this;
    }


    public void collides(Updateable collidingUpdateable){
        // tagin saa: collidingUpdateable.getTag()
    }

    public void spawnProjectile(){
        Projectile projectile = new Projectile(controller, this.getPosition(), 180, 10,
                "projectile_enemy", this);
        controller.addUpdateable(projectile);
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
        controller.removeFromCollisionList(this);;
    }
}

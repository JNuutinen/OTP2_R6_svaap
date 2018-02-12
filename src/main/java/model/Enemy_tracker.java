package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

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

    public Enemy_tracker(Controller controller, Image image, Point2D[] path, double initialX, double initialY,
                         String tag) {
        super(controller);
        this.controller = controller;
        this.setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        setDirection(180);
        setImage(image);
        setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;
    }


    public Updateable getUpdateable(){
        return this;
    }


    public int getMovementPattern() {
        return movementPattern;
    }

    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
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
            moveStep(deltaTime);
        }
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

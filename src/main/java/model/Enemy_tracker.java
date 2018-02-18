package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


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
    private double direction = 0;
    private Weapon weapon1;
    private Weapon weapon2;
    private Point2D[] path;
    private Point2D currentDestination = null;

    // Ampumisen kovakoodit
    private int fireRate = 300;
    private int fireRateCounter = 100;


    public Enemy_tracker(Controller controller, int movementPattern, double initialX, double initialY, Point2D[] path,
                 String tag) {
        super(controller);
        this.path = path;
        currentDestination = this.path[0];
        this.controller = controller;
        this.setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);



        rotate(180);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;

        Component c = new Component("triangle", 3, 0, Color.WHITE, 100, 0);
        components.add(c);
        equipComponents(components);
        this.setHitbox(50);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(-60.0, -30.0,
                60.0, 00.0,
                -60.0, 30.0);
        drawShip(triangle);

    }

    @Override
    public void update(double deltaTime){
        if (fireRateCounter <= fireRate) fireRateCounter++;
        if (fireRateCounter >= fireRate) {
            fireRateCounter = 0;
            spawnProjectile();
        }
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            //setPosition(getXPosition(), (((Math.sin(getXPosition() / 70) * 60)) * movementPattern) + initialY);
            moveStep(deltaTime);
        }

        if(getDistanceFromTarget(currentDestination) < 10){
            currentDestination = path[1];
        }
    }

    public double getDistanceFromTarget(Point2D target){
        return Math.sqrt(Math.pow(target.getX() - this.getXPosition(), 2) + Math.pow(target.getY() - this.getYPosition(), 2));
    }

    public Updateable getUpdateable(){
        return this;
    }

    public void setMovementPattern(int movementPattern) {
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
    }

    public int getMovementPattern() {
        return movementPattern;
    }

    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public void collides(Updateable collidingUpdateable){
        // tagin saa: collidingUpdateable.getTag()
    }

    public void spawnProjectile(){
        Projectile projectile = new Projectile(controller, this.getPosition(), 28,  180, 10,
                "projectile_enemy", this, Color.ORANGERED);
        controller.addUpdateable(projectile);
    }

    public void destroyThis(){
        controller.removeUpdateable(this);
        controller.removeFromCollisionList(this);;
    }
}

package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Enemy extends Unit implements Updateable {
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


    public Enemy(Controller controller, int movementPattern, double initialX, double initialY,
                 String tag) {
        super(controller);
        this.controller = controller;
        this.setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        setDirection(180);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;

        Component c = new Component("triangle", 3, 0, Color.PURPLE, 30, 40);
        components.add(c);
        Component c2 = new Component("triangle", 3, 0, Color.PURPLE, 0, -20);
        components.add(c2);
        Component c3 = new Component("triangle", 3, 0, Color.PURPLE, 20, 10);
        components.add(c3);
        Component c4 = new Component("triangle", 3, 0, Color.PURPLE, 20, -10);
        components.add(c4);
        equipComponents(components);
        this.setHitbox(40, 120);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(60.0, -30.0,
                -60.0, 00.0,
                60.0, 30.0);
        drawShip(triangle);

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
            setPosition(getXPosition(), (((Math.sin(getXPosition() / 70) * 60)) * movementPattern) + initialY);
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

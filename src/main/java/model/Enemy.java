package model;

import controller.Controller;
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
    private double direction = 0;


    // tulinopeus sekunneissa, ja sen apumuuttuja
    private double fireRate = 3;
    private double fireRateCounter = 2;
    private double damagedTimeCounter = 0;
    private boolean tookDamage2 = false;


    public Enemy(Controller controller, Color shipColor, int movementPattern, double initialX, double initialY, int tag) {
        super(controller, shipColor);
        this.controller = controller;
        setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        rotate(180);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;


        this.setHitbox(60);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(-60.0, -30.0,
                60.0, 00.0,
                -60.0, 30.0);
        drawShip(triangle);

        Component c = new Component("triangle", 3, 0, Color.PURPLE, 30, 40);
        components.add(c);
        Component c2 = new Component("triangle", 3, 0, Color.PURPLE, 0, -20);
        components.add(c2);
        Component c3 = new Component("triangle", 3, 0, Color.PURPLE, 20, 10);
        addComponent(c3);
        Component c4 = new Component("triangle", 3, 0, Color.PURPLE, 20, -10);
        components.add(c4);

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
        if(getTookDamage()){
            tookDamage2 = true;
            damagedTimeCounter = 0;
            setTookDamage(false);
        }
        if(tookDamage2 && damagedTimeCounter > 0.1){
            tookDamage2 = false;
            setOriginalColor();
            damagedTimeCounter = 0;
        }
        else if(tookDamage2){
            damagedTimeCounter += deltaTime;
        }

        if (fireRateCounter <= fireRate) {
            fireRateCounter += deltaTime;
        }
        if (fireRateCounter >= fireRate) {
            fireRateCounter = 0;
            shootPrimary();
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
}

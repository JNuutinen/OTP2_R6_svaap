package model;

import javafx.scene.image.Image;
import view.GameMain;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Enemy extends Unit implements Updateable {
    public static final int MOVE_NONE = -1;
    public static final int MOVE_STRAIGHT = 0;
    public static final int MOVE_SINE = 1;

    private double initialX;
    private double initialY;
    private int movementPattern;
    private double direction = 180;
    private Weapon weapon1;
    private Weapon weapon2;

    public Enemy() {
        GameMain.units.add(this);
        setDirection(180);
    }

    public Enemy(Image image, int movementPattern, double initialX, double initialY) {
        GameMain.units.add(this);
        setPosition(initialX, initialY);
        setDirection(180);
        setImage(image);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public void setMovementPattern(int movementPattern) {
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
    }

    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
    }

    @Override
    public void update(double deltaTime){
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < 100
                || getYPosition() > WINDOW_HEIGHT+100) {
            GameLoop.removeUpdateable(this);
        } else {
            setPosition(getXPosition(), (((Math.sin(getXPosition() / 70) * 60)) * movementPattern) + initialY);
            moveStep(deltaTime);
        }
    }
}

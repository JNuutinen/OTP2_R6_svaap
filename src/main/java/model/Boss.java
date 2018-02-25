package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.projectiles.SmallProjectile;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class Boss extends Unit implements Updateable {

    public static final int MOVE_NONE = -1;
    public static final int MOVE_STRAIGHT = 0;
    public static final int MOVE_SINE = 1;

    private Controller controller;
    private double initialX;
    private double initialY;
    private int movementPattern;
    private double direction = 180;
    private int movementCounter = 0;
    private boolean up = false;
    private int originalhp;

    // Ampumisen kovakoodit
    private int fireRate = 50;
    private int fireRateCounter = 50;

    public Boss(Controller controller) {
        super(controller);
        this.controller = controller;
        controller.addUnitToCollisionList(this);
        rotate(180);
        this.setTag("enemy");
    }

    public Boss(Controller controller, int hp, Image image, int movementPattern, double initialX, double initialY,
                 String tag) {
        super(controller);
        this.controller = controller;
        setHp(hp);
        originalhp = hp;
        this.setTag(tag);
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        rotate(180);
        setImage(image);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;
        this.setHitbox(128,256);
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
            spawnProjectile(0);
            spawnProjectile(5);
            spawnProjectile(-5);
        }
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            setPosition(getXPosition(), upOrDown());
            moveBoss(deltaTime);
        }
        if(hpPercentage() > 0 && hpPercentage() <= 10) {
            controller.setHealthbar(hpPercentage());
        }
    }

    // TODO: ei käytä asetta
    public void spawnProjectile(int direction){
        SmallProjectile projectile = new SmallProjectile(controller, this, 28,30, direction);
        controller.addUpdateable(projectile);
    }

    public double upOrDown(){
        if (!up){
            if(movementCounter >= 500){ up = true;}
            return initialY + movementCounter++;
        }else{
            if(movementCounter <= 200){ up = false;}
            return initialY + movementCounter--;
            }
        }
    public int hpPercentage(){
        int tenthHp = originalhp / 10;
        return getHp() / tenthHp;
    }
}

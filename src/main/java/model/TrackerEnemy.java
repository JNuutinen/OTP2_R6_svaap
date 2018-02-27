package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class TrackerEnemy extends Unit implements Updateable {
    public static final int MOVE_NONE = -1;
    public static final int MOVE_STRAIGHT = 0;
    public static final int MOVE_SINE = 1;

    private GraphicsContext gc;
    private Controller controller;
    private double initialX;
    private double initialY;
    private int movementPattern;
    private Point2D[] path;
    private int currentDestinationIndex = 0;
    private int lastDestinationIndex = 0;
    private double rotatingSpeed = 4;
    private boolean shootingTarget = false;
    private Updateable target = null;
    private final double initialVelocity = 300;

    // Ampumisen kovakoodit
    private int fireRate = 99999999;
    private int fireRateCounter = 100;


    public TrackerEnemy(GraphicsContext gc, Controller controller, int movementPattern, double initialX,
                        double initialY, Point2D[] path, String tag) {
        super(gc, controller);
        this.path = path;
        this.gc = gc;
        this.controller = controller;
        this.setTag(tag);
        this.lastDestinationIndex = path.length-1;
        setPosition(initialX, initialY);
        setVelocity(initialVelocity);
        findAndSetTarget();



        rotate(-160);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;

        Component c = new Component(gc, "triangle", 3, 0, Color.LIMEGREEN, 100, 0);
        components.add(c);
        equipComponents(components);
        this.setHitbox(50);

        Polygon triangle = new Polygon(); //Tämä tekee kolmion mikä esittää vihollisen alusta
        triangle.getPoints().addAll(-30.0, -30.0,
                40.0, 00.0,
                -30.0, 30.0);
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

        //laskee oman kulman ja kohteeseen katsottavan kulman erotuksen ja pitaa asteet -180 ja 180 valilla

        double angleToTarget = 180;
        if(!shootingTarget) {
            // kun paasee tarpeeksi lahelle maaranpaata, vaiha maaranpaa seuraavaan
            if(getDistanceFromTarget(path[currentDestinationIndex]) < 15){
                if(path[currentDestinationIndex] == path[lastDestinationIndex]){
                    shootingTarget = true;
                    lockDirection(180);
                    fireRateCounter = 100;
                    fireRate = 300;
                    setVelocity(100);
                }
                else{
                    currentDestinationIndex++;
                }
            }

            // taa vaa pitaa asteet -180 & 180 valissa
            angleToTarget = getAngleFromTarget(path[currentDestinationIndex]) - getDirection();
            while (angleToTarget >= 180.0) {
                angleToTarget -= 360.0;
            }
            while (angleToTarget < -180) {
                angleToTarget += 360.0;
            }

            /*if (angleToTarget < rotatingSpeed - 3) {     // TODO HEGE POISTAA
                rotate(angleToTarget * 0.05);
            } else if (angleToTarget > rotatingSpeed + 3) {*/
            rotate(angleToTarget * rotatingSpeed * deltaTime);
            //}

        }
        else{
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
        }
    }

    //etsii pelaajan updateable listasta ja asettaa sen kohteeksi
    public void findAndSetTarget(){
        for(Updateable updateable : controller.getUpdateables()){
            if(updateable.getTag().equals("player")) {
                target = updateable;
            }
        }
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

    // TODO: ei käytä asetta
    public void spawnProjectile(){
       // SmallProjectile projectile = new SmallProjectile(controller, this, 22,10, Color.LIMEGREEN);
      //  controller.addUpdateable(projectile);
    }
}

package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.weapons.Blaster;
import model.weapons.Weapon;

import static view.GameMain.*;

/**
 * TrackerEnemy vihollinen seuraa "katsellaan" ja ampuu pelaajaa kohti.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class TrackerEnemy extends Unit implements Updateable {

    /**
     * Jos vihollinen ei liiku, se saa arvon -1
     */
    public static final int MOVE_NONE = -1;

    /**
     * Jos vihollinen liikkuu suoraan -x suunnassa, se saa 0.
     */
    public static final int MOVE_STRAIGHT = 0;

    /**
     * Jos vihollinen liikkuu siniaallon tavoin, se saa arvon 1.
     */
    public static final int MOVE_SINE = 1;

    /**
     * Pelin kontrolleri
     */
    private Controller controller;

    /**
     * Alkuposition x-koordinaatti.
     */
    private double initialX;

    /**
     * Alkuposition y-koordinaatti.
     */
    private double initialY;

    /**
     * Aluksen liikkumatapa.
     */
    private int movementPattern;
    private Point2D[] path;
    private int currentDestinationIndex = 0;
    private int lastDestinationIndex = 0;

    /**
     * Aluksen kääntymisnopeus
     */
    private double rotatingSpeed = 4;
    private boolean shootingTarget = false;
    private Updateable target = null;
    private final double initialVelocity = 300;

    /**
     * Tulinopeus
     */
    private double fireRate = 99999;

    /**
     * Tulinopeuden apulaskuri
     */
    private double fireRateCounter = 0;

    /**
     * Osuma-efektin aikalaskuri
     */
    private double damagedTimeCounter = 0;

    /**
     * Osuma-efketin totuusarvo.
     */
    private boolean tookDamage2 = false;


    /**
     * TrackerEnemyn konstruktori. Luo kolmion muotoisen aluksen ja lisää sen CollisionListiin.
     * @param controller Pelin kontrolleri
     * @param shipColor Aluksen väri
     * @param movementPattern Aluksen liikkumatyyli
     * @param initialX Alkuposition x-koordinaatti
     * @param initialY Alkuposition y-koordinaatti
     * @param path Aluksen reitti
     * @param tag Vihollisen tunniste. Käytä tagia "enemy" jos kyseessä perusvihollinen.
     */
    public TrackerEnemy(Controller controller, Color shipColor, int movementPattern, double initialX, double initialY, Point2D[] path,
                        int tag) {
        super(controller, shipColor);
        this.path = path;
        this.controller = controller;
        setTag(tag);
        lastDestinationIndex = path.length-1;
        controller.addUnitToCollisionList(this);
        setPosition(initialX, initialY);
        setVelocity(initialVelocity);
        findAndSetTarget();



        rotate(-160);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.initialX = initialX;
        this.initialY = initialY;

        Component c = new Component("triangle", 3, 0, Color.YELLOW, 50, 0);
        components.add(c);
        //equipComponents();
        setHitbox(80);

        Polygon shape = new Polygon();
        // aluksen muoto
        shape.getPoints().addAll(30.0, 10.0,
                60.0, 10.0,
                50.0, 20.0,
                10.0, 30.0,
                0.0, 20.0,
                -10.0, 40.0,
                -40.0, 30.0,
                -50.0, 20.0,
                -30.0, 20.0,
                -30.0, -20.0,
                -50.0, -20.0,
                -40.0, -30.0,
                -10.0, -40.0,
                0.0, -20.0,
                10.0, -30.0,
                50.0, -20.0,
                60.0, -10.0,
                30.0, -10.0);
        drawShip(shape);

    }

    /**
     * Aluksen liikkumisen, ampumisen ja kääntymisen päivittäminen.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
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

        if (fireRateCounter <= fireRate){
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
            //setPosition(getXPosition(), (((Math.sin(getXPosition() / 70) * 60)) * movementPattern) + initialY);
            moveStep(deltaTime);
        }


        //laskee oman kulman ja kohteeseen katsottavan kulman erotuksen ja pitaa asteet -180 ja 180 valilla

        double angleToTarget;
        if(!shootingTarget) {
            // kun paasee tarpeeksi lahelle maaranpaata, vaiha maaranpaa seuraavaan
            if(getDistanceFromTarget(path[currentDestinationIndex]) < 18){
                if(path[currentDestinationIndex] == path[lastDestinationIndex]){
                    shootingTarget = true;
                    lockDirection(180);
                    fireRateCounter = 1;
                    fireRate = 3;
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

    /**
     * Etsii pelaajan updateable listasta ja asettaa sen kohteeksi
     */
    public void findAndSetTarget(){
        for(Updateable updateable : controller.getUpdateables()){
            if(updateable.getTag() == PLAYER_SHIP_TAG) {
                target = updateable;
            }
        }
    }
}

package model.units;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import model.HitboxCircle;
import model.Tag;

import java.util.List;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * TrackerEnemy vihollinen liikkuu aluksi määritellun reitin (path) ja kun on kulkenut sen loppuun, niin
 * alkaa valumaan vasemmalle päin samalla kun katsoo ja ampuu pelaajaa kohti.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class TrackerEnemy extends Unit {

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
     * Lista Point2D-koordinaateista, joka kertoo aluksen kulkupolun.
     */
    private Point2D[] path;

    /**
     * Kertoo nykyisen sijainnin indeksin, apumuuttuja kulkupolkuun.
     */
    private int currentDestinationIndex = 0;

    /**
     * TODO JOku
     */
    private int lastDestinationIndex = 0;

    /**
     * Aluksen kääntymisnopeus
     */
    private double rotatingSpeed = 4;

    /**
     * Kertoo, ampuuko alus kohdettaan.
     */
    private boolean shootingTarget = false;

    /**
     * Aluksen kohde, jota se seuraa "katseellaan".
     */
    private HitboxCircle target = null;

    /**
     * Aluksen alkunopeus.
     */
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
    private boolean tookDamage = false;

    /**
     * TrackerEnemyn konstruktori. Luo aluksen ja lisää sen peliin.
     * @param primaries Lista, jossa aluksen aseet tageina ilmoitettuna.
     * @param initialPosition Aloitussijainnin koordinaatit.
     * @param path Lista, jossa aluksen kulkeman polun koordinaatit.
     */
    public TrackerEnemy(List<Tag> primaries, Point2D initialPosition, Point2D[] path) {
        super(Color.DEEPSKYBLUE, 5, 20);
        this.path = path;
        controller = GameController.getInstance();
        setTag(Tag.SHIP_ENEMY);
        lastDestinationIndex = path.length-1;
        initialX = initialPosition.getX();
        initialY = initialPosition.getY();
        setPosition(initialX, initialY);
        setVelocity(initialVelocity);
        findAndSetTarget();
        rotate(180);
        setIsMoving(true);
        setHitbox(80);
        setHp(40);

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
        makePrimaryWeapons(primaries);

        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
    }

    @Override
    public void update(double deltaTime){
        if(getTookDamage()){
            tookDamage = true;
            damagedTimeCounter = 0;
            setTookDamage(false);
        }
        if (tookDamage && damagedTimeCounter > 0.1) {
            tookDamage = false;
            setOriginalColor();
            damagedTimeCounter = 0;
        } else if (tookDamage) {
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
            moveStep(deltaTime);
        }

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
            rotate(angleToTarget * rotatingSpeed * deltaTime);
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
     * Etsii lähimmän pelaajan playerHitboxObjects listasta ja asettaa sen kohteeksi
     */
    public void findAndSetTarget(){
        double shortestDistance = Double.MAX_VALUE;
        HitboxCircle closestPlayer = null;
        for (HitboxCircle hitboxCircle : controller.getPlayerHitboxObjects()) {
            double distance = getDistanceFromTarget(hitboxCircle.getPosition());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestPlayer = hitboxCircle;
            }
        }
        target = closestPlayer;
    }
}

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
 * BomberEnemy TODO
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class BomberEnemy extends Unit {

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
     * path-listan indeksi eli piste johon alus kulkee sen jälkeen kun on saavuttanut path-listan
     * viimeisen pisteen. Jos arvo on -1, niin alus tuhoaa itsensä sen jälkeen kun on saavuttanut viimeisen pisteen.
     */
    private int connectingPoint;

    /**
     * Aluksen kääntymisnopeus
     */
    private double rotatingSpeed = 4;


    /**
     * Aluksen kohde, jota se seuraa "katseellaan".
     */
    private HitboxCircle target = null;

    /**
     * Aluksen alkunopeus.
     */
    private final double initialVelocity = 270;

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
     * BomberEnemyn konstruktori. Luo aluksen ja lisää sen peliin.
     * @param primaries Lista, jossa aluksen aseet tageina ilmoitettuna.
     * @param initialPosition Aloitussijainnin koordinaatit.
     * @param path Lista, jossa aluksen kulkeman polun koordinaatit.
     * @param connectingPoint path-listan indeksi eli piste johon alus kulkee sen jälkeen kun on saavuttanut path-listan
     *                       viimeisen pisteen. Jos arvo on -1, niin alus tuhoaa itsensä sen jälkeen kun on saavuttanut viimeisen pisteen.
     */
    public BomberEnemy(List<Tag> primaries, Point2D initialPosition, Point2D[] path, int connectingPoint) {
        super(Color.DEEPPINK, 5, 20);
        this.path = path;
        this.connectingPoint = connectingPoint;
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
            moveStep(deltaTime);
        }

        double angleToTarget;
        // kun paasee tarpeeksi lahelle maaranpaata, vaiha maaranpaa seuraavaan
        if(getDistanceFromTarget(path[currentDestinationIndex]) < 18){
            if(path[currentDestinationIndex] == path[lastDestinationIndex]){
                // jos liittymispiste on määritelty -1, niin tuhoa alus reitin loppussa.
                if(connectingPoint == -1){
                    destroyThis();
                }
                // tai jos liittymispiste on jokin reitin pisteistä paitsi viimeinen piste, niin aseta seuraava piste kyseiseen reitin pisteeseen.
                else if(connectingPoint < path.length - 1 && connectingPoint >= 0){
                    currentDestinationIndex = connectingPoint;
                }
            }
            else{
                currentDestinationIndex++;
            }
        }

        // tää vaa pitaa asteet -180 & 180 valissa
        angleToTarget = getAngleFromTarget(path[currentDestinationIndex]) - getDirection();
        while (angleToTarget >= 180.0) {
            angleToTarget -= 360.0;
        }
        while (angleToTarget < -180) {
            angleToTarget += 360.0;
        }
        rotate(angleToTarget * rotatingSpeed * deltaTime);

        /*
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

        }*/
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

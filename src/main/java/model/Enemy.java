package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

import static view.GameMain.ENEMY_SHIP_TAG;
import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Vihollisen pääluokka. Perii unitin ja implementoi Updateable rajapinnan.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Enemy extends Unit {

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
     * Vihollisen aloituspaikan x-koordinaatti.
     */
    private double initialX;

    /**
     * Vihollisen aloituspaikan y-koordinaatti.
     */
    private double initialY;

    /**
     * Vihollisen liikkumatapa, ei liiku, suoraan ja siniaalto.
     */
    private int movementPattern;

    /**
     * Tulinopeus
     */
    private double fireRate = 3;

    /**
     * Tulinopeuden laskuri, tätä kasvatetaan kunnes se saavuttaa tulinopeuden, jolloin ammutaan.
     * Tämän jälkeen laskuri nollataan.
     */
    private double fireRateCounter = 2;

    /**
     * Laskuri, joka kertoo kuinka kauan osuman graafinen efekti kestää.
     */
    private double damagedTimeCounter = 0;

    /**
     * Osuma-efektin boolean arvo.
     */
    private boolean tookDamage2 = false;

    /**
     * Vihollisen konstruktori. Luo vihollisen graafisen esityksen, eli kolmion, jonka värin voi valita. Lisää vihollisen CollisionListiin, asettaa
     * tagin, asettaa alkuposition x- ja y-koordinaatit ja liikkumatavan.
     * @param controller Pelin kontrolleri
     * @param shipColor Vihollisaluksen väri
     * @param movementPattern Liikkumatyyli, -1 = MOVE_NONE, 0 MOVE_STRAIGHT, 1 = MOVE_SINE.
     * @param initialPosition Aloitussijainti.
     */
    public Enemy(Controller controller, Color shipColor, ArrayList<Integer> primaries, int movementPattern, Point2D initialPosition) {
        super(controller, shipColor, 5, 20);

        this.controller = controller;
        setTag(ENEMY_SHIP_TAG);
        controller.addUnitToCollisionList(this);
        this.initialX = initialPosition.getX();
        this.initialY = initialPosition.getY();
        setPosition(initialX, initialY);
        rotate(180);
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
        this.setHitbox(80);
        setHp((int) (30 * controller.getLevel().getEnemyHealthModifier()));
        Polygon shape = new Polygon();
        // aluksen muoto
        shape.getPoints().addAll(50.0, 0.0,
                30.0, -20.0,
                10.0, -20.0,
                -10.0, -40.0,
                -30.0, -40.0,
                -20.0, -20.0,
                -50.0, 00.0,
                -20.0, 20.0,
                -30.0, 40.0,
                -10.0, 40.0,
                10.0, 20.0,
                30.0, 20.0);
        drawShip(shape);

        makePrimaryWeapons(primaries);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
    }

    /**
     * Asettaa vihollisen liikkumatyylin.
     * @param movementPattern Liikkumatyyli, -1 = MOVE_NONE, 0 MOVE_STRAIGHT, 1 MOVE_SINE.
     */
    public void setMovementPattern(int movementPattern) {
        this.movementPattern = movementPattern;
        if (movementPattern == MOVE_NONE) setIsMoving(false);
        else setIsMoving(true);
    }

    /**
     * Palauttaa vihollisen liikkumatyylin
     * @return Arvo -1, 0 tai 1.
     */
    public int getMovementPattern() {
        return movementPattern;
    }


    /**
     * Vihollisen päivitys-funktio, kutsutaan gameLoopista jokaisella iteraatiolla.
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

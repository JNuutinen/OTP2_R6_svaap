package model;

import controller.Controller;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Vihollisen pääluokka. Perii unitin ja implementoi Updateable rajapinnan.
 */
public class Enemy extends Unit implements Updateable {

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
     * Suunta johon vihollinen liikkuu.
     */
    private double direction = 0;


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
     * @param initialX Aloituspaikan x-koordinaatti.
     * @param initialY Aloituspaikan y-koordinaatti.
     * @param tag Vihollisen tunniste. Käytä "enemy" perusviholliselle.
     */
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


        this.setHitbox(80);

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
     * Asettaa alkuposition
     * @param initialX Alkuposition x-koordinaatti.
     * @param initialY Alkuposition y-koordinaatti.
     */
    public void setInitPosition(double initialX, double initialY) {
        this.initialX = initialX;
        this.initialY = initialY;
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
                || getXPosition() > WINDOW_WIDTH+200) {
            destroyThis();
        } else {
            //setPosition(getXPosition(), (((Math.sin(getXPosition() / 70) * 60)) * movementPattern) + initialY);

            setPosition(getXPosition(), getYPosition() + yVelocity * deltaTime + (((Math.sin(getXPosition() / 70) * 60)) * movementPattern));
            moveStep(deltaTime);
        }
    }
}

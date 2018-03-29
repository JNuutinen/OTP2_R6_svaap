package model.projectiles;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Unit;

/**
 * Viiveellä hakeutuva ohjusammus.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */

public class LazyMissile extends Missile {

    /**
     * Viive millisekunteina, jonka jälkeen ohjus alkaa hakeutua kohteisiin.
     */
    private static final long HOMING_DELAY = 400;

    /**
     * Ammuksen myöhempi kääntymisnopeus.
     */
    private double latterRotatingSpeed = 0;

    /**
     * Ammuksen alkukääntymisnopeus.
     */
    private double initialRotatingSpeed = 0;

    /**
     * Aikalaskuri, käytetään ammuksen kääntönopeuden määrittämisessä.
     */
    private double timeCounter = 0;

    /**
     * Apumuuttuja, käytetään ammuksen kääntönopeuden määrittämisessä.
     */
    boolean doOnce = true;

    /**
     * Pitää kirjaa ajasta, jonka ammus ollut luotu. Käytetään HOMING_DELAY:n kanssa.
     */
    private long aliveTime = 0;

    /**
     * Konstruktori.
     * @param controller Kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param direction Ammuksen suunta.
     * @param initialRotatingSpeed Ammuksen alkukääntymisnopeus.
     * @param latterRotatingSpeed Ammuksen myöhempi kääntymisnopeus.
     * @param tag Ammuksen tagi.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double initialRotatingSpeed, double latterRotatingSpeed, int tag) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, initialRotatingSpeed, tag);
        this.latterRotatingSpeed = latterRotatingSpeed;
        this.initialRotatingSpeed = initialRotatingSpeed;
        // Asetetaan projectilen suunta
        rotate(direction);
    }

    /**
     * Konstruktori jossa lisäksi canLoseTarget
     * @param controller Kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param direction Ammuksen suunta.
     * @param initialRotatingSpeed Ammuksen alkukääntymisnopeus.
     * @param latterRotatingSpeed Ammuksen myöhempi kääntymisnopeus.
     * @param tag Ammuksen tagi.
     *
     * @param canLoseTarget pystyykö ohjus kadottaa kohteen jos menee liian kauas kohteesta
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double initialRotatingSpeed, double latterRotatingSpeed, int tag, boolean canLoseTarget) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, initialRotatingSpeed, tag, canLoseTarget);
        this.latterRotatingSpeed = latterRotatingSpeed;
        this.initialRotatingSpeed = initialRotatingSpeed;
        // Asetetaan projectilen suunta
        rotate(direction);
    }

    @Override
    public void update(double deltaTime) {
        moveStep(deltaTime * getVelocity());
        // Sen jälkeen missilelle ominaiset updatet, kun on kulunut luonnista HOMING_DELAY verran aikaa.
        if (aliveTime > HOMING_DELAY) {
            super.update(deltaTime);
        } else {
            aliveTime += deltaTime * 1000;
            if (aliveTime > HOMING_DELAY) {
                setVelocity(25);
            }
        }

        // 0.8 sec jälkeen vaihda kääntymisnopeutta. Tarkoitus lentoradan näyttää paremmalata ampumisen jällkeen.
        if(timeCounter > 0.8){
            if(doOnce){
                this.setRotatingSpeed(latterRotatingSpeed);
                doOnce = false;
            }
            this.setRotatingSpeed(getRotatingSpeed() + 4 *deltaTime);
        }
        timeCounter += deltaTime;
    }
}

package model.projectiles;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Unit;

/**
 * Viiveellä hakeutuva ohjusammus.
 */

public class LazyMissile extends Missile {

    /**
     * Viive millisekunteina, jonka jälkeen ohjus alkaa hakeutua kohteisiin.
     */
    private static final long HOMING_DELAY = 400;
    private double latterRotatingSpeed = 0;
    private double timeCounter = 0;

    /**
     * Pitää kirjaa ajasta, jonka ammus ollut luotu. Käytetään HOMING_DELAY:n kanssa.
     */
    private long aliveTime = 0;

    /**
     * Konstruktori vakiovärillä, aloitussuunnalla ja kääntymisnopeudella.
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param direction Projectilen väri.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double initialRotatingSpeed, double latterRotatingSpeed, Component component) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, initialRotatingSpeed, component);
        this.latterRotatingSpeed = latterRotatingSpeed;

        // Asetetaan projectilen suunta
        rotate(direction);


    }

    /**
     * Konstruktori värin valinnalla, aloitussuunnalla ja kääntymisnopeuden .
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param color Projectilen väri.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double rotatingSpeed, double latterRotatingSpeed, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, direction, component);
        this.latterRotatingSpeed = latterRotatingSpeed;


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
                setVelocity(30);
            }
        }

        if(timeCounter > 0.8){
            this.setRotatingSpeed(latterRotatingSpeed);
        }

        timeCounter += deltaTime;
    }
}

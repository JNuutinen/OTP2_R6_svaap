package model.projectiles;

import controller.Controller;
import javafx.scene.canvas.GraphicsContext;
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
    private double initialRotatingSpeed = 0;
    private double timeCounter = 0;
    boolean doOnce = true;

    /**
     * Pitää kirjaa ajasta, jonka ammus ollut luotu. Käytetään HOMING_DELAY:n kanssa.
     */
    private long aliveTime = 0;

    /**
     * Konstruktori vakiovärillä, aloitussuunnalla ja kääntymisnopeudella.
     * @param gc GraphicsContext, johon ammus piirretään.
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param direction Projectilen väri.
     */
    public LazyMissile(GraphicsContext gc, Controller controller, Unit shooter, double speed, int damage,
                       double direction, double initialRotatingSpeed, double latterRotatingSpeed, Component component) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(gc, controller, shooter, speed, damage, initialRotatingSpeed, component);
        this.latterRotatingSpeed = latterRotatingSpeed;
        this.initialRotatingSpeed = initialRotatingSpeed;

        // Asetetaan projectilen suunta
        rotate(direction);


    }

    /**
     * Konstruktori värin valinnalla, aloitussuunnalla ja kääntymisnopeuden.
     * @param gc GraphicsContext, johon ammus piirretään.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param color Projectilen väri.
     */
    public LazyMissile(GraphicsContext gc, Controller controller, Unit shooter, double speed, int damage,
                       double direction, double rotatingSpeed, double latterRotatingSpeed, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(gc, controller, shooter, speed, damage, direction, component);
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
                setVelocity(25);
            }
        }



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

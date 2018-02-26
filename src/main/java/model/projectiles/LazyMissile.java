package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Component;
import model.Unit;
import model.Updateable;

/**
 * Viiveellä hakeutuva ohjusammus.
 */

public class LazyMissile extends Missile {

    /**
     * Viive millisekunteina, jonka jälkeen ohjus alkaa hakeutua kohteisiin.
     */
    private static final long HOMING_DELAY = 400;

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
                       double rotatingSpeed, Component component) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, rotatingSpeed, component);

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
                       double rotatingSpeed, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, direction, component);

        // Asetetaan projectilen suunta
        rotate(direction);
    }

    @Override
    public void update(double deltaTime) {
        // Kutsutaan ensiksi BaseProjectilen perus updatea Missilen kautta.
        callBaseUpdate(deltaTime);

        // Sen jälkeen missilelle ominaiset updatet, kun on kulunut luonnista HOMING_DELAY verran aikaa.
        if (aliveTime > HOMING_DELAY) {
            super.update(deltaTime);
        } else {
            aliveTime += deltaTime * 1000;
            if (aliveTime > HOMING_DELAY) {
                setVelocity(30);
            }
        }
    }
}

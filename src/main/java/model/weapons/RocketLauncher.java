package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.projectiles.Missile;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class RocketLauncher extends Component implements Weapon {

    /**
     * Raketinheittimen ammuksien nopeus.
     */
    private static final int SPEED = 31;

    /**
     * Ammuksien kääntymisnopeus.
     */
    private double rotatingSpeed = 9.0;

    /**
     * Raketinheittimen ammuksien vahinko.
     */
    private static final int DAMAGE = 30;

    /**
     * Raketinheittimen tulinopeus.
     */
    private static final double FIRE_RATE = 1;

    /**
     * Raketinheittimen väri.
     */
    private static final Color COLOR = Color.BLUE;

    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;


    /**
     * Apumuuttuja joka määrittelee voiko ohjus hävittää kohteen jos menee liian kauas kohteesta
     */
    private boolean missileCanLoseTarget = true;

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param missileCanLoseTarget Kertoo voiko ohjus kadottaa kohteen jos etäisyys kasvaa liikaa kohteesta.
     */
    public RocketLauncher(int orientation, double rotatingSpeed, boolean missileCanLoseTarget) {
        super("circle", 4, orientation, COLOR);
        this.controller = GameController.getInstance();
        this.rotatingSpeed = rotatingSpeed;
        this.missileCanLoseTarget = missileCanLoseTarget;
    }

    /**
     * TODO
     * @param orientation Aseen orientation.
     * @param componentOffset Aseen x-offset.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param missileCanLoseTarget Kertoo voiko ohjus kadottaa kohteen jos etäisyys kasvaa liikaa kohteesta.
     */
    public RocketLauncher(int orientation, double rotatingSpeed, boolean missileCanLoseTarget,
                          Point2D componentOffset, Point2D prjoectileOffset) {
        this(orientation, rotatingSpeed, missileCanLoseTarget);
        setComponentOffset(componentOffset);
        setProjectileOffset(prjoectileOffset);
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if (getParentUnit() != null) {
            Missile missile = new Missile(getParentUnit(), SPEED, DAMAGE, rotatingSpeed, getTag(), missileCanLoseTarget);
            controller.addUpdateableAndSetToScene(missile);
            controller.addHitboxObject(missile);
        }
    }
}

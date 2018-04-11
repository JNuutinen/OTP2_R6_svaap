package model.weapons;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.projectiles.LazyMissile;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class RocketShotgun extends Component implements Weapon {

    /**
     * Rakettihaulukon ammuksien nopeus.
     */
    private static final int SPEED = 15;

    /**
     * Rakettihaulukon ammuksien vahinko.
     */
    private static final int DAMAGE = 7;

    /**
     * Rakettihaulukon tulinopeus.
     */
    private static final double FIRE_RATE = 0;

    /**
     * Ammusten käääntymisnopeus aluksi.
     */
    private double initialMissileRotatingSpeed = 11;

    /**
     * Ammusten kääntymisnopeus hetken jälkeen.
     */
    private double latterMissileRotatingSpeed = 11;

    /**
     * Ammuttavien ammusten suunnat. Alkioiden lukumäärä on ammuttavien ammusten lukumäärä.
     */
    //private static final int[] PROJECTILE_DIRECTIONS = {-40, -20, 0, 20, 40};
    //private static final int[] PROJECTILE_DIRECTIONS = {-30, -15, 0, 15, 30};
    //private static final int[] PROJECTILE_DIRECTIONS = {-20, -10, 0, 10, 20};
    private static final int[] PROJECTILE_DIRECTIONS = {-10, -5, 0, 5, 10};

    /**
     * Rakettihaulukon väri.
     */
    private static final Color COLOR = Color.TURQUOISE;

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
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     */
    public RocketShotgun(Controller controller, int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed) {
        super("circle", 4, orientation, COLOR);
        this.controller = controller;
        this.initialMissileRotatingSpeed = initialMissileRotatingSpeed;
        this.latterMissileRotatingSpeed = latterMissileRotatingSpeed;
    }

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     */
    public RocketShotgun(Controller controller, int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed,
                         Point2D componentOffset, Point2D projectileOffset) {
        this(controller, orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed);
        setComponentOffset(componentOffset);
        setProjectileOffset(projectileOffset);
    }

    /**
     * TODO TODO
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     * @param missileCanLoseTarget boolean kertoo voiko ohjus kadottaa kohteensa jos menee liian kauas kohteesta
     */

    public RocketShotgun(Controller controller, int orientation, double initialMissileRotatingSpeed,
                         double latterMissileRotatingSpeed, boolean missileCanLoseTarget) {
        this(controller, orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed);
        this.missileCanLoseTarget = missileCanLoseTarget;
    }


    public RocketShotgun(Controller controller, int orientation, double initialMissileRotatingSpeed,
                         double latterMissileRotatingSpeed, boolean missileCanLoseTarget, Point2D componentOffset, Point2D projectileOffset) {
        this(controller, orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed, missileCanLoseTarget);
        setComponentOffset(componentOffset);
        setProjectileOffset(projectileOffset);
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null) {
            for (int i = 0; i < PROJECTILE_DIRECTIONS.length; i++) {
                LazyMissile lazyMissile = new LazyMissile(controller, getParentUnit(), SPEED, DAMAGE, PROJECTILE_DIRECTIONS[i],
                        initialMissileRotatingSpeed, latterMissileRotatingSpeed, getTag(), missileCanLoseTarget);
                controller.addUpdateableAndSetToScene(lazyMissile, lazyMissile);
            }
        }
    }
}

package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.projectiles.LazyMissile;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class RocketShotgun extends Weapon {

    /**
     * Rakettihaulukon ammuksien nopeus.
     */
    private static final int SPEED = 15;

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
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     */
    public RocketShotgun(int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed) {
        super("circle", 4, orientation, COLOR, 7, 3);
        controller = GameController.getInstance();
        this.initialMissileRotatingSpeed = initialMissileRotatingSpeed;
        this.latterMissileRotatingSpeed = latterMissileRotatingSpeed;
    }

    /**
     * Konstruktori aseen ja ammuksien poikkeamalla.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     */
    public RocketShotgun(int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed,
                         Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed);
        setComponentOffset(componentOffset);
        setProjectileOffset(projectileOffset);
    }

    /**
     * Konstruktori tiedolla, voiko ohjus menettää kohteensa.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     * @param missileCanLoseTarget boolean kertoo voiko ohjus kadottaa kohteensa jos menee liian kauas kohteesta
     */
    public RocketShotgun(int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed,
                         boolean missileCanLoseTarget) {
        this(orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed);
        this.missileCanLoseTarget = missileCanLoseTarget;
    }

    /**
     * Konstruktori aseen ja ammuksen poikkeamalla, sekä tiedolla voiko ohjus menettää kohteensa.
     * @param orientation Aseen orientation.
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     * @param missileCanLoseTarget boolean kertoo voiko ohjus kadottaa kohteensa jos menee liian kauas kohteesta
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     */
    public RocketShotgun(int orientation, double initialMissileRotatingSpeed, double latterMissileRotatingSpeed,
                         boolean missileCanLoseTarget, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, initialMissileRotatingSpeed, latterMissileRotatingSpeed, missileCanLoseTarget);
        setComponentOffset(componentOffset);
        setProjectileOffset(projectileOffset);
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null) {
            if (getFireRateCounter() >= getFirerate()) {
                setFireRateCounter(0);

                for (int i = 0; i < PROJECTILE_DIRECTIONS.length; i++) {
                    LazyMissile lazyMissile = new LazyMissile(getParentUnit(), SPEED, (int) (getDamage() * getDamageMultiplier()), PROJECTILE_DIRECTIONS[i],
                            initialMissileRotatingSpeed, latterMissileRotatingSpeed, getWeaponProjectileTag(), missileCanLoseTarget);
                    controller.addUpdateableAndSetToScene(lazyMissile);
                    controller.addHitboxObject(lazyMissile);
                }
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        if (getFireRateCounter() <= getFirerate()) {
            setFireRateCounter(getFireRateCounter() + deltaTime);
        }
    }
}

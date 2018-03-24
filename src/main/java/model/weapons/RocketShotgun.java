package model.weapons;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.Player;
import model.Unit;
import model.projectiles.LazyMissile;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_PROJECTILE_TAG;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
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
    private static final double FIRE_RATE = 4;

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
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    /**
     * Ammuksen tagi.
     */
    private int tag;

    /**
     * Apumuuttuja joka määrittelee voiko ohjus hävittää kohteen jos menee liian kauas kohteesta
     */
    private boolean missileCanLoseTarget = true;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param componentOffset TODO
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     */
    public RocketShotgun(Controller controller, int orientation, Point2D componentOffset, double initialMissileRotatingSpeed,
                         double latterMissileRotatingSpeed) {
        super("circle", 4, orientation, COLOR, componentOffset);
        this.controller = controller;
        this.initialMissileRotatingSpeed = initialMissileRotatingSpeed;
        this.latterMissileRotatingSpeed = latterMissileRotatingSpeed;
    }

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param componentOffset TODO
     * @param initialMissileRotatingSpeed Ammuksen kääntymisnopeus aluksi.
     * @param latterMissileRotatingSpeed Ammuksen kääntymisnopeus hetken kuluttua.
     * @param missileCanLoseTarget boolean kertoo voiko ohjus kadottaa kohteensa jos menee liian kauas kohteesta
     */

    public RocketShotgun(Controller controller, int orientation, Point2D componentOffset, double initialMissileRotatingSpeed,
                         double latterMissileRotatingSpeed, boolean missileCanLoseTarget) {
        this(controller, orientation, componentOffset, initialMissileRotatingSpeed, latterMissileRotatingSpeed);
        this.missileCanLoseTarget = missileCanLoseTarget;

    }

    public void setShooter(Unit shooter){
        this.shooter = shooter;
        if (shooter instanceof Player){
            this.tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            this.tag = ENEMY_PROJECTILE_TAG;
        }
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if(shooter != null) {
            for (int i = 0; i < PROJECTILE_DIRECTIONS.length; i++) {
                controller.addUpdateable(new LazyMissile(controller, shooter, SPEED, DAMAGE, PROJECTILE_DIRECTIONS[i],
                        initialMissileRotatingSpeed, latterMissileRotatingSpeed, tag, missileCanLoseTarget));
            }
        }
    }
}

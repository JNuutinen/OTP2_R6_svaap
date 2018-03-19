package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Player;
import model.Unit;
import model.projectiles.Missile;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_PROJECTILE_TAG;

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
    private static final int SPEED = 35;

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
     * @param shooter Ammuksen ampuja.
     * @param orientation Aseen orientation.
     * @param xOffset Aseen x-offset.
     * @param yOffset Aseen y-offset.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     */
    public RocketLauncher(Controller controller, Unit shooter, int orientation, double xOffset,
                          double yOffset, double rotatingSpeed) {
        super("circle", 4, orientation, COLOR, xOffset, yOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.rotatingSpeed = rotatingSpeed;
        if (shooter instanceof Player){
            this.tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            this.tag = ENEMY_PROJECTILE_TAG;
        }
    }

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param orientation Aseen orientation.
     * @param xOffset Aseen x-offset.
     * @param yOffset Aseen y-offset.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param missileCanLoseTarget boolean kertoo voiko ohjus kadottaa kohteensa jos menee liian kauas kohteesta
     */
    public RocketLauncher(Controller controller, Unit shooter, int orientation, double xOffset,
                          double yOffset, double rotatingSpeed, boolean missileCanLoseTarget) {
        this(controller, shooter, orientation, xOffset, yOffset, rotatingSpeed);
        this.missileCanLoseTarget = missileCanLoseTarget;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        controller.addUpdateable(new Missile(controller, shooter, SPEED, DAMAGE, rotatingSpeed, tag, missileCanLoseTarget));
    }
}

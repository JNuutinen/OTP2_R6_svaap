package model.weapons;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.projectiles.SmallProjectile;

/**
 * Blasterhaulikkoase.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class BlasterShotgun extends Component implements Weapon {

    /**
     * Aseen ammuksien nopeus
     */
    private static final int SPEED = 25;

    /**
     * Aseen ammuksien vahinko.
     */
    private static final int DAMAGE = 5;

    /**
     * Aseen tulinopeus.
     */
    private static final double FIRE_RATE = 0.2;

    /**
     * Aseen väri.
     */
    private static final Color COLOR = Color.ORANGERED;

    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;

    /**
     * Ammusten vahinkomääräkerroin.
     */
    private double damageMultiplier = 1;


    /**
     * ammuksen nopeus
     */
    private double projectileSpeed = 0;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param projectileSpeed TODO
     */
    public BlasterShotgun(Controller controller, int orientation, double projectileSpeed) {
        super("rectangle", 4, orientation, COLOR);
        this.projectileSpeed = projectileSpeed;
        this.controller = controller;
    }

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param projectileSpeed TODO
     * @param componentOffset TODO
     * @param projectileOffset TODO
     */
    public BlasterShotgun(Controller controller, int orientation, double projectileSpeed, Point2D componentOffset,
                          Point2D projectileOffset) {
        this(controller, orientation, projectileSpeed);
        this.controller = controller;
        setProjectileOffset(projectileOffset);
        setComponentOffset(componentOffset);
    }

    /**
     * @return tulinopeus
     */
    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null) {
            for (int i = -1; i < 2; i++) {
                // TODO luo smallProjectile custom nopeuden kanssa @param projectileSpeed
                SmallProjectile smallProjectile = new SmallProjectile(controller, getParentUnit(), SPEED, (int)(DAMAGE * damageMultiplier),
                        getProjectileOffset(), getParentUnitColor(), i * 7, getTag());
                controller.addUpdateableAndSetToScene(smallProjectile);
                controller.addHitboxObject(smallProjectile);
            }
        }
    }
}

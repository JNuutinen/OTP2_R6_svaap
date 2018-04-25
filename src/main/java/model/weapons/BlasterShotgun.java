package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.projectiles.SmallProjectile;

/**
 * Blasterhaulikkoase.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class BlasterShotgun extends Weapon {

    /**
     * Aseen ammuksien nopeus
     */
    private final int SPEED = 25;

    /**
     * Aseen väri.
     */
    private static final Color COLOR = Color.ORANGERED;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * ammuksen nopeus
     */
    private double projectileSpeed = 0;

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammusten nopeus.
     */
    public BlasterShotgun(int orientation, double projectileSpeed) {
        super("rectangle", 4, orientation, COLOR, 10, 1.5);
        this.projectileSpeed = projectileSpeed;
        controller = GameController.getInstance();
    }

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammusten nopeus
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     */
    public BlasterShotgun(int orientation, double projectileSpeed, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed);
        setProjectileOffset(projectileOffset);
        setComponentOffset(componentOffset);
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null) {
            if (getFireRateCounter() >= getFirerate()) {
                setFireRateCounter(0);
                for (int i = -1; i < 2; i++) {
                    // TODO luo smallProjectile custom nopeuden kanssa @param projectileSpeed
                    SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), SPEED, (int) (getDamage() * getDamageMultiplier()),
                            getProjectileOffset(), getParentUnitColor(), i * 7, getWeaponProjectileTag());
                    controller.addUpdateableAndSetToScene(smallProjectile);
                    controller.addHitboxObject(smallProjectile);
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

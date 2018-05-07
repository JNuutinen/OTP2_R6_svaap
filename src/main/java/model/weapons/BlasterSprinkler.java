package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Updateable;
import model.projectiles.SmallProjectile;

/**
 * Blasterruiskuttelija.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class BlasterSprinkler extends Weapon implements Updateable {

    /**
     * Aseen vakioväri.
     */
    private static final Color COLOR = Color.RED;

    /**
     * Ammuksen nopeus.
     */
    private double projectileSpeed;

    /**
     * Tieto siitä, ammutaanko parhaillaan.
     */
    private boolean isShooting = false;

    /**
     * Fireraten laskuri.
     */
    private double firerateCounter = 0;

    /**
     * Ampumisen pituus.
     */
    private double shootingTime;

    /**
     * Ampumisajan laskuri.
     */
    private double shootingTimeCounter = 0;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param shootingTime Ampumisen kesto.
     * @param firerate tulinopeus
     */
    public BlasterSprinkler(int orientation, double projectileSpeed, double shootingTime, double firerate) {
        super("rectangle", 4, orientation, COLOR, 10, firerate);
        this.shootingTime = shootingTime;
        this.controller = GameController.getInstance();
        this.projectileSpeed = projectileSpeed;

    }

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param firerate tulinopeus
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     * @param shootingTime Ampumisruiskautuksen kesto.
     */
    public BlasterSprinkler(int orientation, double projectileSpeed, double shootingTime, double firerate, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed, shootingTime, firerate);
        setProjectileOffset(projectileOffset);
        setComponentOffset(componentOffset);
    }

    @Override
    public void shoot() {
        if (getFireRateCounter() >= getFirerate() && !isShooting) {
            setFireRateCounter(0);
            isShooting = true;
        }
    }

    @Override
    public void update(double deltaTime) {
        if (getFireRateCounter() <= getFirerate()) {
            setFireRateCounter(getFireRateCounter() + deltaTime);
        }

        if(getParentUnit() != null) {
            if (!getParentUnit().isDestroyed()) {
                if (isShooting) {
                    firerateCounter += deltaTime;
                    shootingTimeCounter += deltaTime;
                    if (firerateCounter >= 0.07) {
                        SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), projectileSpeed, (int)(getDamage() * getDamageMultiplier()),
                                getProjectileOffset(), getParentUnitColor(), Math.random() * 140 - 70, getWeaponProjectileTag());
                        controller.addUpdateableAndSetToScene(smallProjectile);
                        controller.addHitboxObject(smallProjectile);
                        firerateCounter = 0;
                        if (shootingTimeCounter > shootingTime) {
                            isShooting = false;
                            shootingTimeCounter = 0;
                        }
                    }
                }
            } else {
                controller.removeUpdateable(this);
            }
        }
    }
}

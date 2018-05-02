package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.projectiles.LaserBeam;
import model.projectiles.Plasma;
import model.projectiles.SmallProjectile;

/**
 * Blaster pyssy.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class PlasmaGun extends Weapon {

    /**
     * Blasterin ammuksien nopeus.
     */
    private static final int SPEED = 40;

    /**
     * Blasterin komponentin väri.
     */
    private static final Color COLOR = Color.DEEPSKYBLUE;

    /**
     * Ammuksien nopeus.
     */
    private double projectileSpeed = SPEED;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     */
    public PlasmaGun(int orientation, double projectileSpeed, double firerate) {
        super("triangle", 4, orientation, COLOR, 10, firerate);
        this.controller = GameController.getInstance();
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Konstruktori aseen ja ammuksen aloituspaikan custom poikkeamalla aluksesta
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     */
    public PlasmaGun(int orientation, double projectileSpeed, double firerate, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed, firerate);
        this.setProjectileOffset(projectileOffset);
        this.setComponentOffset(componentOffset);
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null){
            if (getFireRateCounter() >= getFirerate()) {
                setFireRateCounter(0);
                Plasma plasma = new Plasma(getParentUnit(), SPEED, (int) (getDamage() * getDamageMultiplier()), getParentUnitColor(), getWeaponProjectileTag(),
                        new Point2D(getProjectileOffset().getX(), getProjectileOffset().getY()));
                controller.addUpdateableAndSetToScene(plasma);
                controller.addHitboxObject(plasma);
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        setFireRateCounter(getFireRateCounter() + deltaTime);
    }
}

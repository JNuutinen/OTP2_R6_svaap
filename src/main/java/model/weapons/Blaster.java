package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.projectiles.SmallProjectile;

/**
 * Blaster pyssy.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Blaster extends Weapon {

    /**
     * Blasterin ammuksien nopeus.
     */
    private static final int SPEED = 30;

    /**
     * Blasterin komponentin väri.
     */
    private static final Color COLOR = Color.ORANGE;

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
    public Blaster(int orientation, double projectileSpeed, double firerate) {
        super("rectangle", 4, orientation, COLOR, 10, firerate);
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
    public Blaster(int orientation, double projectileSpeed, double firerate, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed, firerate);
        this.setProjectileOffset(projectileOffset);
        this.setComponentOffset(componentOffset);
    }

    @Override
    public void shoot() {
        if(getParentUnit() != null){
            if (getFireRateCounter() >= getFirerate()) {
                setFireRateCounter(0);
                SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), projectileSpeed, (int) (getDamage() * getDamageMultiplier()),
                        getProjectileOffset(), getParentUnitColor(), getWeaponProjectileTag());
                controller.addUpdateableAndSetToScene(smallProjectile);
                controller.addHitboxObject(smallProjectile);
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        setFireRateCounter(getFireRateCounter() + deltaTime);
    }
}

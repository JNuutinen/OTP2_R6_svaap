package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.projectiles.BulletProjectile;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Konekivääriase.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class MachineGun extends Weapon {

    /**
     * Aseen ammuksien nopeus.
     */
    private static final int SPEED = 55;

    /**
     * Aseen komponentin väri.
     */
    private static final Color COLOR = Color.RED;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Ammuksien nopeus.
     */
    private double projectileSpeed = SPEED;

    /**
     * Konstruktori
     *
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param firerate Tulinopeus.
     */
    public MachineGun(int orientation, double projectileSpeed, double firerate) {
        super("rectangle", 4, orientation, COLOR, 3, firerate);
        this.controller = GameController.getInstance();
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Konstruktori aseen ja ammuksen aloituspaikan custom poikkeamalla aluksesta
     *
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param firerate Tulinopeus.
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta (x = eteenpäin, y = vasempaan päin; aluksesta)
     */
    public MachineGun(int orientation, double projectileSpeed, double firerate, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed, firerate);
        this.setProjectileOffset(projectileOffset);
        this.setComponentOffset(componentOffset);
    }

    /**
     * Ampuu BulletProjectileja hajonnalla. Hajonnan suuntakulmaa voidaan säätää ThreadLocalRandom.current().nextInt()
     * -kutsussa.
     */
    @Override
    public void shoot() {
        if(getParentUnit() != null){
            if (getFireRateCounter() >= getFirerate()) {
                setFireRateCounter(0);
                BulletProjectile bulletProjectile = new BulletProjectile(getParentUnit(), projectileSpeed, (int) (getDamage() * getDamageMultiplier()),
                        getProjectileOffset(), getParentUnitColor(), ThreadLocalRandom.current().nextInt(-2, 2 + 1), getWeaponProjectileTag());
                controller.addUpdateableAndSetToScene(bulletProjectile);
                controller.addHitboxObject(bulletProjectile);
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        setFireRateCounter(getFireRateCounter() + deltaTime);
    }
}

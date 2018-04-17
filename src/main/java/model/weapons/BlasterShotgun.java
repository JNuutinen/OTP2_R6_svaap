package model.weapons;

import controller.Controller;
import controller.GameController;
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
     * ammuksen nopeus
     */
    private double projectileSpeed = 0;

    /**
     * Ammuksen tagi.
     */
    private int tag;

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed TODO
     */
    public BlasterShotgun(int orientation, double projectileSpeed) {
        super("rectangle", 4, orientation, COLOR);
        this.projectileSpeed = projectileSpeed;
        controller = GameController.getInstance();
    }

    /**
     * Konstruktori.
     * @param orientation Aseen orientation.
     * @param projectileSpeed TODO
     * @param componentOffset TODO
     * @param projectileOffset TODO
     */
    public BlasterShotgun(int orientation, double projectileSpeed, Point2D componentOffset,
                          Point2D projectileOffset) {
        this(orientation, projectileSpeed);
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
    public void shoot() {
        if(getParentUnit() != null) {
            for (int i = -1; i < 2; i++) {
                // TODO luo smallProjectile custom nopeuden kanssa @param projectileSpeed
                SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), SPEED, DAMAGE,
                        getProjectileOffset(), getParentUnitColor(), i * 7, tag);
                controller.addUpdateableAndSetToScene(smallProjectile);
                controller.addHitboxObject(smallProjectile);
            }
        }
    }
}

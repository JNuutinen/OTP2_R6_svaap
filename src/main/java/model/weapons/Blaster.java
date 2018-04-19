package model.weapons;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.projectiles.SmallProjectile;

/**
 * Blaster pyssy.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Blaster extends Component implements Weapon {

    /**
     * Blasterin ammuksien nopeus.
     */
    private static final int SPEED = 30;

    /**
     * Blasterin ammuksien vahinko.
     */
    private static final int DAMAGE = 10;

    /**
     * Blasterin tulinopeus.
     */
    private static final double FIRE_RATE = 0.2;

    /**
     * Blasterin komponentin väri.
     */
    private static final Color COLOR = Color.ORANGE;

    /**
     * Ammuksien nopeus.
     */
    private double projectileSpeed = SPEED;

    /**
     * Ammusten vahinkomääräkerroin.
     */
    private double damageMultiplier = 1;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     */
    public Blaster(int orientation, double projectileSpeed) {
        super("rectangle", 4, orientation, COLOR);
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
    public Blaster(int orientation, double projectileSpeed, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, projectileSpeed);
        this.setProjectileOffset(projectileOffset);
        this.setComponentOffset(componentOffset);
    }

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
        if(getParentUnit() != null){
            SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), projectileSpeed, (int)(DAMAGE * damageMultiplier),
                    getProjectileOffset(), getParentUnitColor(), getTag());
            controller.addUpdateableAndSetToScene(smallProjectile);
            controller.addHitboxObject(smallProjectile);
        }
    }
}

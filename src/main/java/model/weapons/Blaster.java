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

    /** Blasterin ammuksien nopeus. */
    private static final int SPEED = 30;

    /** Blasterin ammuksien vahinko. */
    private static final int DAMAGE = 5;

    /** Blasterin tulinopeus. */
    private static final double FIRE_RATE = 0.2;

    /** Blasterin komponentin väri. */
    private static final Color COLOR = Color.ORANGE;

    /** Ammuksien nopeus. */
    private double projectileSpeed = SPEED;


    /** Pelin kontrolleri. */
    private Controller controller;

    /**
     * TODO
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     */
    public Blaster(int orientation, double projectileSpeed) {
        super("rectangle", 4, orientation, COLOR);
        this.controller = GameController.getInstance();
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * TODO
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param componentOffset TODO
     * @param projectileOffset TODO
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
    public void shoot() {
        if(getParentUnit() != null){
            SmallProjectile smallProjectile = new SmallProjectile(getParentUnit(), projectileSpeed, DAMAGE,
                    getProjectileOffset(), getParentUnitColor(), getTag());
            controller.addUpdateableAndSetToScene(smallProjectile, smallProjectile);
        }
    }
}

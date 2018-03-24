package model.weapons;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Component;
import model.Player;
import model.Unit;
import model.projectiles.SmallProjectile;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_PROJECTILE_TAG;

/**
 * Blaster pyssy.
 */
public class Blaster extends Component implements Weapon {

    /** Blasterin ammuksien nopeus. */
    private static final int SPEED = 30;

    /** Blasterin ammuksien vahinko. */
    private static final int DAMAGE = 5;

    /** Blasterin tulinopeus. */
    private static final double FIRE_RATE = 0.2;

    /** Blasterin komponentin väri. */
    private static final Color COLOR = Color.WHITE;

    /** Ammuksien väri. */
    private Color projectileColor = Color.WHITE;

    /** Ammuksien nopeus. */
    private double projectileSpeed = SPEED;

    /** Ammuksien tagi. */
    private int tag;

    /** Pelin kontrolleri. */
    private Controller controller;

    /** Unit, jolla ase on käytössä. */
    private Unit shooter;

    /**
     * Konstruktori ammuksen ampuvan aluksen värillä.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param projectileSpeed Ammuksen nopeus.
     * @param componentOffset TODO.
     * @param projectileOffset TODO
     */
    public Blaster(Controller controller, int orientation, double projectileSpeed, Point2D componentOffset, Point2D projectileOffset) {
        super("rectangle", 4, orientation, COLOR, componentOffset, projectileOffset);
        this.controller = controller;
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Konstruktori valittavalla ammuksen värillä.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param projectileColor Ammuksen väri.
     * @param projectileSpeed Ammuksen nopeus.
     * @param componentOffset TODO
     * @param projectileOffset TODO
     */
    public Blaster(Controller controller, int orientation, Color projectileColor, double projectileSpeed,
                   Point2D componentOffset, Point2D projectileOffset) {
        this(controller, orientation, projectileSpeed, componentOffset, projectileOffset);
        this.projectileColor = projectileColor;
    }

    public void setShooter(Unit shooter){
        this.shooter = shooter;
        if (shooter instanceof Player){
            this.tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            this.tag = ENEMY_PROJECTILE_TAG;
        }
        this.projectileColor = shooter.getUnitColor();
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if(shooter != null){
            controller.addUpdateable(new SmallProjectile(controller, shooter, projectileSpeed, DAMAGE,
                    getProjectileOffset(), projectileColor, tag));
        }
    }
}

package model.weapons;

import controller.Controller;
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

    /**
     * Blasterin ammuksien nopeus
     */
    private static final int SPEED = 30;

    /**
     * Blasterin ammuksien vahinko.
     */
    private static final int DAMAGE = 5;

    /**
     * Blasterin tulinopeus.
     */
    private static final double FIRE_RATE = 0.05;

    /**
     * Blasterin väri.
     */
    private static final Color COLOR = Color.RED;

    private Color projectileColor = Color.LIGHTBLUE;

    private double projectileSpeed;

    private int tag;



    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;

    /**
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    /**
     * Konstruktori. Kutsuu yliluokan (Component) konstruktoria jonka jälkeen asettaa kontrollerin ja ampujan.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jolla ase on käytössä.
     * @param shape Blasterin muoto merkkijonona.
     * @param size Blasterin koko.
     * @param orientation Blasterin suunta (kulma).
     * @param xOffset Blasterin sijainnin heitto unitista x-suunnassa.
     * @param yOffset Blasterin sijainnin heitto unitista y-suunnassa.
     */
    public Blaster(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                   double yOffset, Color projectileColor, double projectileSpeed, double projectileFrontOffset, double projectileLeftOffset) {
        super(shape, size, orientation, projectileColor, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.controller = controller;
        if (shooter instanceof Player){
            this.tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            this.tag = ENEMY_PROJECTILE_TAG;
        }
        this.shooter = shooter;
        this.projectileSpeed = projectileSpeed;

    }
    /*
    public Blaster(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                   double yOffset, Color componentColor, double projectileSpeed, double projectileFrontOffset, double projectileLeftOffset) {
        super(shape, size, orientation, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.controller = controller;
        if (shooter instanceof Player){
            this.tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            this.tag = ENEMY_PROJECTILE_TAG;
        }
        this.shooter = shooter;
        this.projectileColor = projectileColor;
        this.projectileSpeed = projectileSpeed;

    }
    */

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        controller.addUpdateable(new SmallProjectile(controller, shooter, projectileSpeed, DAMAGE, this,
                getProjectileFrontOffset(), getProjectileLeftOffset(), projectileColor, tag));
    }
}

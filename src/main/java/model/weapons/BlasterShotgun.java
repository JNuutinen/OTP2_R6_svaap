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
 * Blasterhaulikkoase.
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
    private static final Color COLOR = Color.YELLOW;

    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;

    /**
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    /**
     * Ammuksen väri.
     */
    private Color projectileColor;

    /**
     * Ammuksen tagi.
     */
    private int tag;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param componentOffset TODO
     * @param projectileOffset TODO
     */
    public BlasterShotgun(Controller controller, int orientation, Point2D componentOffset,
                          Point2D projectileOffset) {
        super("rectangle", 4, orientation, COLOR, componentOffset, projectileOffset);
        this.controller = controller;
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

    /**
     * @return tulinopeus
     */
    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if(shooter != null) {
            for (int i = -1; i < 2; i++) {
                controller.addUpdateableAndSetToScene(new SmallProjectile(controller, shooter, SPEED, DAMAGE,
                        getProjectileOffset(), projectileColor, i * 7, tag));
            }
        }
    }
}

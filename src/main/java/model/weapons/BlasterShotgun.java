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
     * @param shooter Ammuksen ampuja.
     * @param orientation Aseen orientation.
     * @param projectileColor Ammuksen väri.
     * @param xOffset Aseen x-offset.
     * @param yOffset Aseen y-offset.
     * @param projectileFrontOffset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     * @param projectileLeftOffset Ammuksen aloituspaikan poikkeus aluksen vasempaan suuntaan.
     */
    public BlasterShotgun(Controller controller, Unit shooter, int orientation, Color projectileColor, double xOffset,
                   double yOffset, double projectileFrontOffset, double projectileLeftOffset) {
        super("rectangle", 4, orientation, COLOR, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.projectileColor = projectileColor;
        if (shooter instanceof Player){
            tag = PLAYER_PROJECTILE_TAG;
        }
        else{
            tag = ENEMY_PROJECTILE_TAG;
        }
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
        for(int i = -1; i < 2; i++) {
            controller.addUpdateable(new SmallProjectile(controller, shooter, SPEED, DAMAGE, this,
                    getProjectileFrontOffset(), getProjectileLeftOffset(), projectileColor, i * 7, tag));
        }
    }
}

package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Unit;
import model.projectiles.SmallProjectile;

public class BlasterShotgun extends Component implements Weapon {

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
    private static final double FIRE_RATE = 0.2;

    /**
     * Blasterin väri.
     */
    private static final Color COLOR = Color.HOTPINK;

    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;

    /**
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    private Color projectileColor;

    /**
     * Konstruktori. Kutsuu yliluokan (Component) konstruktoria jonka jälkeen asettaa kontrollerin ja ampujan.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jolla ase on käytössä.
     * @param shape Blasterin muoto merkkijonona.
     * @param size Blasterin koko.
     * @param orientation Blasterin suunta (kulma).
     * @param xOffset Blasterin sijainnin heitto unitista x-suunnassa.
     * @param yOffset Blasterin sijainnin heitto unitista y-suunnassa.
     * @param projectileFrontOffset ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     * @param projectileLeftOffset ammuksen aloituspaikan poikkeama aluksn vasempaan suuntaan
     */
    public BlasterShotgun(Controller controller, Unit shooter, String shape, int size, int orientation, Color projectileColor, double xOffset,
                   double yOffset, double projectileFrontOffset, double projectileLeftOffset) {
        super(shape, size, orientation, COLOR, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.projectileColor = projectileColor;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        for(int i = -1; i < 2; i++) {
            controller.addUpdateable(new SmallProjectile(controller, shooter, SPEED, DAMAGE, this,
                    getProjectileFrontOffset(), getProjectileLeftOffset(), projectileColor, i * 5));
        }
    }
}

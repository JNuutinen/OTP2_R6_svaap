package model.weapons;

import controller.Controller;
import javafx.scene.canvas.GraphicsContext;
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
     * GraphicsContext, johon piirretään.
     */
    private GraphicsContext gc;

    /**
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    /**
     * Konstruktori. Kutsuu yliluokan (Component) konstruktoria jonka jälkeen asettaa kontrollerin ja ampujan.
     * @param gc GraphicsContext, johon piirretään.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jolla ase on käytössä.
     * @param shape Blasterin muoto merkkijonona.
     * @param size Blasterin koko.
     * @param orientation Blasterin suunta (kulma).
     * @param xOffset Blasterin sijainnin heitto unitista x-suunnassa.
     * @param yOffset Blasterin sijainnin heitto unitista y-suunnassa.
     */
    public BlasterShotgun(GraphicsContext gc, Controller controller, Unit shooter, String shape, int size, int orientation,
                          double xOffset, double yOffset) {
        super(gc, shape, size, orientation, COLOR, xOffset, yOffset);
        this.gc = gc;
        this.controller = controller;
        this.shooter = shooter;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        for(int i = -1; i < 2; i++) {
            controller.addUpdateable(new SmallProjectile(gc, controller, shooter, SPEED, DAMAGE, i * 5, this));
        }
    }
}

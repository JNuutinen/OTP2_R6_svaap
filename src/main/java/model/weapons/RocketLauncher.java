package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Unit;
import model.projectiles.Missile;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
 */
public class RocketLauncher extends Component implements Weapon {

    /**
     * Raketinheittimen ammuksien nopeus.
     */
    private static final int SPEED = 40;

    private double rotatingSpeed = 9.0;

    /**
     * Raketinheittimen ammuksien vahinko.
     */
    private static final int DAMAGE = 30;

    /**
     * Raketinheittimen tulinopeus.
     */
    private static final double FIRE_RATE = 1;

    /**
     * Raketinheittimen väri.
     */
    private static final Color COLOR = Color.PINK;

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
     * @param shape Raketinheittimen muoto merkkijonona.
     * @param size Raketinheittimen koko.
     * @param orientation Raketinheittimen suunta (kulma).
     * @param xOffset Raketinheittimen sijainnin heitto unitista x-suunnassa.
     * @param yOffset Raketinheittimen sijainnin heitto unitista y-suunnassa.
     */
    public RocketLauncher(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                          double yOffset, double rotatingSpeed) {
        super(shape, size, orientation, COLOR, xOffset, yOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.rotatingSpeed = rotatingSpeed;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        controller.addUpdateable(new Missile(controller, shooter, SPEED, DAMAGE, rotatingSpeed, this));
    }
}

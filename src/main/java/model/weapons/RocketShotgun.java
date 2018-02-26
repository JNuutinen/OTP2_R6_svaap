package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Unit;
import model.projectiles.LazyMissile;

/**
 * Raketinheitin. Paitsi ampuu ohjuksia.
 */
public class RocketShotgun extends Component implements Weapon {

    /**
     * Rakettihaulukon ammuksien nopeus.
     */
    private static final int SPEED = 25;

    /**
     * Rakettihaulukon ammuksien vahinko.
     */
    private static final int DAMAGE = 7;

    /**
     * Rakettihaulukon tulinopeus.
     */
    private static final double FIRE_RATE = 2.5;

    /**
     * Rakettihaulikon ampumien ammuksien aloitussuunnat, kulmien lukumäärä on ammuttujen ammusten määrä.
     */
    private static final int[] PROJECTILE_DIRECTIONS = {-40, -20, 0, 20, 40};

    /**
     * Rakettihaulukon väri.
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
    public RocketShotgun(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                          double yOffset) {
        super(shape, size, orientation, COLOR, xOffset, yOffset);
        this.controller = controller;
        this.shooter = shooter;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        for (int i = 0; i < PROJECTILE_DIRECTIONS.length; i++) {
            controller.addUpdateable(new LazyMissile(controller, shooter, SPEED, DAMAGE, PROJECTILE_DIRECTIONS[i], this));
        }
    }
}

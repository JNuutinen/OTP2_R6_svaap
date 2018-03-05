package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Player;
import model.Unit;
import model.projectiles.LaserBeam;

import static view.GameMain.ENEMY_TRACE_TAG;
import static view.GameMain.PLAYER_TRACE_TAG;

public class LaserGun extends Component implements Weapon {

    /**
     * Blasterin ammuksien nopeus
     */
    private static final int SPEED = 30;

    /**
     * Blasterin ammuksien vahinko.
     */
    private static final int DAMAGE = 15;

    /**
     * Blasterin tulinopeus.
     */
    private static final double FIRE_RATE = 1.0;

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

    private Color laserColor;

    private int tag;

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
    public LaserGun(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                    double yOffset, Color laserColor) {
        super(shape, size, orientation, COLOR, xOffset, yOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.laserColor = laserColor;
        if (shooter instanceof Player){
            this.tag = PLAYER_TRACE_TAG;
        }
        else{
            this.tag = ENEMY_TRACE_TAG;
        }
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        controller.addUpdateable(new LaserBeam(controller, shooter, SPEED, DAMAGE, laserColor, tag));
    }
}


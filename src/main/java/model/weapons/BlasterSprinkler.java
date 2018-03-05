package model.weapons;

import controller.Controller;
import javafx.scene.paint.Color;
import model.Component;
import model.Player;
import model.Unit;
import model.Updateable;
import model.projectiles.SmallProjectile;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_PROJECTILE_TAG;

/**
 * Blaster pyssy.
 */
public class BlasterSprinkler extends Component implements Weapon, Updateable {

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
    private double firerate = 0.1;

    /**
     * Blasterin väri.
     */
    private static final Color COLOR = Color.HOTPINK;

    private Color projectileColor;

    private double projectileSpeed;

    private int tag;

    private boolean isShooting = false;

    private double firerateCounter = 0;

    private double shootingTime = 0;

    private double shootingTimeCounter = 0;


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
    public BlasterSprinkler(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset, double yOffset,
                            Color projectileColor, double projectileSpeed, double projectileFrontOffset, double projectileLeftOffset, double shootingTime) {
        super(shape, size, orientation, COLOR, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.shootingTime = shootingTime;
        this.controller = controller;
        controller.addUpdateable(this);
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

    @Override
    public double getFireRate() {
        return firerate;
    }

    @Override
    public void shoot() {
        isShooting = true;
    }

    @Override
    public void update(double deltaTime) {
        if(isShooting){
            firerateCounter += deltaTime;
            shootingTimeCounter += deltaTime;
            if(firerateCounter >= firerate){
                controller.addUpdateable(new SmallProjectile(controller, shooter, projectileSpeed, DAMAGE, this,
                        getProjectileFrontOffset(), getProjectileLeftOffset(), projectileColor, Math.random()*160-80, tag));
                firerateCounter = 0;
                if(shootingTimeCounter > shootingTime){
                    isShooting = false;
                    shootingTimeCounter = 0;
                }
            }
        }
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}

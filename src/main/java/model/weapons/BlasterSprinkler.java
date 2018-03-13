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
 * Blasterruiskuttelija.
 */
public class BlasterSprinkler extends Component implements Weapon, Updateable {

    /**
     * Aseen ammuksien nopeus
     */
    private static final int SPEED = 30;

    /**
     * Aseen ammuksien vahinko.
     */
    private static final int DAMAGE = 5;

    /**
     * Aseen tulinopeus.
     */
    private double firerate = 0.07;

    /**
     * Aseen vakioväri.
     */
    private static final Color COLOR = Color.RED;

    /**
     * Ammuksen väri.
     */
    private Color projectileColor;

    /**
     * Ammuksen nopeus.
     */
    private double projectileSpeed;

    /**
     * Ammuksen tagi.
     */
    private int tag;

    /**
     * Tieto siitä, ammutaanko parhaillaan.
     */
    private boolean isShooting = false;

    /**
     * Fireraten laskuri.
     */
    private double firerateCounter = 0;

    /**
     * Ampumisen pituus.
     */
    private double shootingTime = 0;

    /**
     * Ampumisajan laskuri.
     */
    private double shootingTimeCounter = 0;


    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Unit, jolla ase on käytössä.
     */
    private Unit shooter;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param orientation Aseen orientation.
     * @param xOffset Aseen x-offset.
     * @param yOffset Aseen y-offset.
     * @param projectileColor Ammuksen väri.
     * @param projectileSpeed Ammuksen nopeus.
     * @param projectileFrontOffset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     * @param projectileLeftOffset Ammuksen aloituspaikan poikkeus aluksen vasempaan suuntaan.
     * @param shootingTime Ampumisen kesto.
     */
    public BlasterSprinkler(Controller controller, Unit shooter, int orientation, double xOffset, double yOffset,
                            Color projectileColor, double projectileSpeed, double projectileFrontOffset, double projectileLeftOffset, double shootingTime) {
        super("rectangle", 4, orientation, COLOR, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
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
                        getProjectileFrontOffset(), getProjectileLeftOffset(), projectileColor, Math.random()*140-70, tag));
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

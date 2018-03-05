package model.weapons;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import model.Component;
import model.Player;
import model.Unit;
import model.Updateable;
import model.projectiles.LaserBeam;

import static view.GameMain.ENEMY_TRACE_TAG;
import static view.GameMain.PLAYER_TRACE_TAG;
import static view.GameMain.WINDOW_WIDTH;

public class LaserGun extends Component implements Weapon, Updateable {

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

    private double shootingDelay = 0;

    private double timeCounter = 0;

    private boolean triggeredShoot = false;

    private Circle chargingEffect;

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
                    double yOffset, Color laserColor, double projectileFrontOffset, double projectileLeftOffset) {
        super(shape, size, orientation, COLOR, xOffset, yOffset, projectileFrontOffset, projectileLeftOffset);
        this.controller = controller;
        this.shooter = shooter;
        this.laserColor = laserColor;
        controller.addUpdateable(this);
        if (shooter instanceof Player){
            this.tag = PLAYER_TRACE_TAG;
        }
        else{
            this.tag = ENEMY_TRACE_TAG;
        }
    }

    // ampumisviiveen kanssa
    public LaserGun(Controller controller, Unit shooter, String shape, int size, int orientation, double xOffset,
                    double yOffset, Color laserColor, double projectileFrontOffset, double projectileLeftOffset, double shootingDelay) {
        this(controller, shooter, shape, size, orientation, xOffset, yOffset, laserColor, projectileFrontOffset, projectileLeftOffset);
        this.shootingDelay = shootingDelay;
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        chargingEffect = buildChargingEffect(Color.WHITE);
        shooter.getChildren().add(chargingEffect);
        chargingEffect.setCenterX(degreesToVector(shooter.getDirection()).getX() * getProjectileFrontOffset());
        triggeredShoot = true;
    }

    @Override
    public void update(double deltaTime) {
        if(triggeredShoot){
            timeCounter += deltaTime;
            chargingEffect.setRadius(chargingEffect.getRadius() + deltaTime * 17 / shootingDelay);
            chargingEffect.setStrokeWidth(chargingEffect.getStrokeWidth() + deltaTime * 4 / shootingDelay);

            if(timeCounter > shootingDelay){
                controller.addUpdateable(new LaserBeam(controller, shooter, SPEED, DAMAGE, laserColor, tag,
                        getProjectileFrontOffset(), getProjectileLeftOffset()));
                triggeredShoot = false;
                timeCounter = 0;
                shooter.getChildren().remove(chargingEffect);
            }
        }
    }

    private Circle buildChargingEffect(Color color) {
        // Ammuksen muoto
        chargingEffect = new Circle();
        chargingEffect.setRadius(1);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(10.0);
        blur.setInput(bloom);
        chargingEffect.setEffect(blur);
        chargingEffect.setFill(color);
        chargingEffect.setStroke(Color.GREEN);
        chargingEffect.setStrokeWidth(1);
        return chargingEffect;
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}


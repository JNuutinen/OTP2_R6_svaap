package model.weapons;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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

    private Line pointerEffect;
    private double visibilityAmount = 0;

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
        pointerEffect = buildPointerEffect(Color.WHITE);
        shooter.getChildren().add(pointerEffect);

        // en oo iha varma miten tää toimii mut toimii kuitenkin TODO poista kommentti
        if (shooter instanceof Player) {
            chargingEffect.setCenterX(degreesToVector(shooter.getDirection()).getX() * getProjectileFrontOffset());
            chargingEffect.setCenterY(degreesToVector(shooter.getDirection() + 90).getY() * getProjectileLeftOffset());

            pointerEffect.setStartX(degreesToVector(shooter.getDirection()).getX() * getProjectileFrontOffset());
            pointerEffect.setStartY(degreesToVector(shooter.getDirection() + 90).getY() * getProjectileLeftOffset());
        }
        else{
            chargingEffect.setCenterX(degreesToVector(shooter.getDirection()).getX() * getProjectileFrontOffset() * -1);
            chargingEffect.setCenterY(degreesToVector(shooter.getDirection() + 90).getY() * getProjectileLeftOffset() * -1);

            pointerEffect.setStartX(degreesToVector(shooter.getDirection()).getX() * getProjectileFrontOffset() * -1);
            pointerEffect.setStartY(degreesToVector(shooter.getDirection() + 90).getY() * getProjectileLeftOffset() * -1);
        }
        pointerEffect.setEndX(pointerEffect.getStartX() + (WINDOW_WIDTH * 0.4));
        pointerEffect.setEndY(pointerEffect.getStartY());

        triggeredShoot = true;
    }

    @Override
    public void update(double deltaTime) {
        if(triggeredShoot){
            timeCounter += deltaTime;
            chargingEffect.setRadius(chargingEffect.getRadius() + deltaTime * 17 / shootingDelay);
            chargingEffect.setStrokeWidth(chargingEffect.getStrokeWidth() + deltaTime * 4 / shootingDelay);
            //pointerEffect.setStroke();

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
        GaussianBlur blur = new GaussianBlur(6);
        blur.setInput(bloom);
        chargingEffect.setEffect(blur);
        chargingEffect.setFill(color);
        chargingEffect.setStroke(Color.GREEN);
        chargingEffect.setStrokeWidth(1);
        return chargingEffect;
    }

    private Line buildPointerEffect(Color color){
        Stop[] stops1 = new Stop[] { new Stop(0, Color.color(0, 1, 0, 1)), new Stop(1, Color.TRANSPARENT)};
        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1);
        Line line = new Line(0, 0, WINDOW_WIDTH * 0.6 * -1, 0);
        line.setStroke(lg);
        return line;
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}


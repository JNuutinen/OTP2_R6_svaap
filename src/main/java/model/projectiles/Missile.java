package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Unit;
import model.Updateable;

/**
 * Hakeutuva ohjusammus.
 */
@SuppressWarnings("unused")
public class Missile extends BaseProjectile {

    /**
     * Ammuksen vakioväri.
     */
    private static final Color COLOR = Color.YELLOW;

    /**
     * Ammuksen kääntymisnopeus.
     */
    private static final double ROTATING_SPEED = 9;

    /**
     * Viittaus kontrolleriin kohteen löytämiseksi updateables-listasta.
     */
    private Controller controller;

    /**
     * Ammuksen kohde.
     */
    private Updateable target;


    /**
     * Konstruktori projectilen vakiovärillä.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage);

        this.controller = controller;

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    /**
     * Konstruktori värin valinnalla.
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param color Projectilen väri.
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage, Color color) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage);

        this.controller = controller;

        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    @Override
    public void update(double deltaTime) {
        // Kutsutaan ensiksi BaseProjectilen perus updatea
        super.update(deltaTime);

        // Sen jälkeen missilelle ominaiset updatet
        double angleToTarget;
        if (target != null) {
            if (controller.getUpdateables().contains(target)) {
                angleToTarget = getAngleFromTarget(target.getPosition()) - getDirection();
                // taa vaa pitaa asteet -180 & 180 valissa
                while (angleToTarget >= 180.0) {
                    angleToTarget -= 360.0;
                }
                while (angleToTarget < -180) {
                    angleToTarget += 360.0;
                }
                rotate(angleToTarget * ROTATING_SPEED * deltaTime);
            } else {
                findAndSetTarget();
            }
        } else {
            findAndSetTarget();
        }
    }

    /**
     * Rakentaa projectilen Polygonin.
     * @param speed Projectilen nopeus, vaikuttaa hännän pituuteen.
     * @param color Projectilen väri.
     * @return Rakennettu Polygon.
     */
    private Polygon buildProjectile(double speed, Color color) {
        // Ammuksen muoto
        Polygon shape = new Polygon();
        shape.getPoints().addAll(-9.0, 0.0,
                0.0, -3.0,
                speed*0.6+1.0, 0.0, // ammuksen hanta skaalautuu nopeuden mukaan, mutta on ainakin 1.0
                0.0, 3.0);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        shape.setEffect(blur);
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(color);
        shape.setStrokeWidth(5.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
    }

    /**
     * Asettaa hakeutuvan ammuksen kohteen.
     */
    private void findAndSetTarget() {
        for (Updateable updateable : controller.getUpdateables()) {
            if (updateable.getTag().equals("enemy")) {
                target = updateable;
            }
        }
    }
}

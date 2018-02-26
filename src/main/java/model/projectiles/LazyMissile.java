package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Component;
import model.Unit;
import model.Updateable;

/**
 * Viiveellä hakeutuva ohjusammus.
 */
@SuppressWarnings("unused")
public class LazyMissile extends BaseProjectile {

    /**
     * Viive millisekunteina, jonka jälkeen ohjus alkaa hakeutua kohteisiin.
     */
    private static final long HOMING_DELAY = 200;

    /**
     * Ammuksen vakioväri.
     */
    private static final Color COLOR = Color.RED;

    /**
     * Viittaus kontrolleriin kohteen löytämiseksi updateables-listasta.
     */
    private Controller controller;

    /**
     * Ammuksen kääntymisnopeus, vakioarvo 9.
     */
    private double rotatingSpeed = 9;

    /**
     * Ammuksen kohde.
     */
    private Updateable target;

    /**
     * Pitää kirjaa ajasta, jonka ammus ollut luotu. Käytetään HOMING_DELAY:n kanssa.
     */
    private long aliveTime = 0;

    /**
     * Konstruktori vakiovärillä ja aloitussuunnan määrittelemisellä.
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param direction Projectilen väri.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction, Component component) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, component);

        this.controller = controller;

        // Asetetaan projectilen suunta
        rotate(direction);

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    /**
     * Konstruktori vakiovärillä, aloitussuunnalla ja kääntymisnopeudella.
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param direction Projectilen väri.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double rotatingSpeed, Component component) {
        // Kutsutaaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, component);

        this.controller = controller;

        // Asetetaan projectilen suunta
        rotate(direction);

        // Kääntymisnopeus
        this.rotatingSpeed = rotatingSpeed;

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    /**
     * Konstruktori värin valinnalla, aloitussuunnalla ja kääntymisnopeuden .
     * @param controller Pelin kontrolleri.
     * @param shooter Unit, jonka aseesta projectile ammutaan.
     * @param speed Projectilen nopeus.
     * @param damage Projectilen vahinko.
     * @param color Projectilen väri.
     */
    public LazyMissile(Controller controller, Unit shooter, double speed, int damage, double direction,
                       double rotatingSpeed, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, component);

        this.controller = controller;

        // Asetetaan projectilen suunta
        rotate(direction);

        // Kääntymisnopeus
        this.rotatingSpeed = rotatingSpeed;

        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    @Override
    public void update(double deltaTime) {
        // Kutsutaan ensiksi BaseProjectilen perus updatea
        super.update(deltaTime);

        // Sen jälkeen missilelle ominaiset updatet, kun on kulunut luonnista HOMING_DELAY verran aikaa.
        if (aliveTime > HOMING_DELAY) {
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
                    rotate(angleToTarget * rotatingSpeed * deltaTime);
                } else {
                    findAndSetTarget();
                }
            } else {
                findAndSetTarget();
            }
        } else {
            aliveTime += deltaTime * 1000;
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
            if (updateable.getTag().equals("enemy") || updateable.getTag().equals("boss")) {
                target = updateable;
            }
        }
    }
}

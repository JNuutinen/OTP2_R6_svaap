package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Component;
import model.Trail;
import model.Unit;
import model.Updateable;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Ohjusammuksen luokka. Luokassa vain ohjukselle ominaisten
 * piirteiden hallinta, kaikille ammuksille yhteiset piirteet
 * yliluokassa (BaseProjectile).
 */
public class Missile extends BaseProjectile {

    /**
     * Ammuksen vakioväri.
     */
    private static final Color COLOR = Color.YELLOW;

    /**
     * Ammuksen kääntymisnopeus, vakioarvo 9.
     */
    private double rotatingSpeed = 9;

    /**
     * Viittaus kontrolleriin kohteen löytämiseksi updateables-listasta.
     */
    private Controller controller;

    /**
     * Ammuksen kohde.
     */
    private Updateable target;


    /**
     * Konstruktori projectilen vakiovärillä ja kääntymisnopeudella.
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage, double rotatingSpeed, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, component);
        this.rotatingSpeed = rotatingSpeed;
        this.controller = controller;

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);

        Trail trail = new Trail(controller, this, getPosition());
        this.getChildren().addAll(trail);
    }

    /**
     * Konstruktori värin valinnalla
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     * @param color Projectilen väri
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage, double rotatingSpeed, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage, component);
        this.rotatingSpeed = rotatingSpeed;
        this.controller = controller;

        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);
    }

    @Override
    public void update(double deltaTime) {
        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH + 200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT + 100) {
            destroyThis();
        } else {
            moveStep(deltaTime * getVelocity());
        }

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
    }

    /**
     * Rakentaa projectilen Polygonin
     * @param speed Projectilen nopeus, vaikuttaa hännän pituuteen
     * @param color Projectilen väri
     * @return Rakennettu Polygon
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


        //setVelocity(0);

        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(5.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
    }

    private void findAndSetTarget() {
        for (Updateable updateable : controller.getUpdateables()) {
            if (updateable.getTag().equals("enemy") || updateable.getTag().equals("boss") ) {
                target = updateable;
            }
        }
    }
}

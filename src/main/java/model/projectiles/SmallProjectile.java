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

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Pieni perusammus
 */
public class SmallProjectile extends BaseProjectile implements Updateable {

    /**
     * Ammuksen vakioväri
     */
    private static final Color COLOR = Color.LIGHTSALMON;
    private Controller controller;
    private int damage;

    /**
     * Konstruktori projectilen vakiovärillä
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage, Component component, double frontOffset, double leftOffset,
                           int tag) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, frontOffset, leftOffset, tag);

        this.controller = controller;
        this.damage = damage;
        setHitbox(10);// TODO: hitboxin koko kovakoodattu | 16
        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
    }

    /**
     * Konstruktori värin valinnalla ja ammuksen aloituspaikan poikkeaman valinnalla
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     * @param color Projectilen väri
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage, Component component,
                           double frontOffset, double leftOffset, Color color, int tag) {
        this(controller, shooter, speed, damage, component, frontOffset, leftOffset, tag);
        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
    }
    /**
     * Konstruktori värin valinnalla ja ammuksen aloituspaikan poikkeaman valinnalla, ja suunnan valinnalla
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     * @param direction Projectilen suunta
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage, Component component,
                           double frontOffset, double leftOffset, Color color, double direction, int tag) {
        this(controller, shooter, speed, damage, component, frontOffset, leftOffset, color, tag);
        rotate(direction);
    }


    @Override
    public void destroyThis(){
        controller.removeUpdateable(this);
    }

    @Override
    public void collides(Updateable collidingUpdateable){
        destroyThis();
        for (Unit unit : controller.getCollisionList()) {
            if (unit == collidingUpdateable) {
                unit.takeDamage(damage);
            }
        }
    }

    @Override
    public void update(double deltaTime){
        // tarkastele menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH+200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT+100) {
            destroyThis();
        } else {
            moveStep(deltaTime * getVelocity());
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
        shape.getPoints().addAll(-10.0, 2.5,
                -10.0, -2.5,
                0.0, -8.0,
                speed*0.4+10.0, 0.0, // ammuksen hanta skaalautuu nopeuden mukaan, mutta on ainakin 1.0
                0.0, 8.0);

        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        //shape.setEffect(blur);
        shape.setFill(Color.WHITE);
        shape.setStroke(color);
        shape.setStrokeWidth(3.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
    }
}

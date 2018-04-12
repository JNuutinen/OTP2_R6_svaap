package model.projectiles;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.HitboxObject;
import model.Unit;
import model.Updateable;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Pieni perusammus
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class SmallProjectile extends BaseProjectile implements Updateable, HitboxObject {

    /**
     * Ammuksen vakioväri
     */
    private static final Color COLOR = Color.WHITE;
    private Controller controller;
    private int damage;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset TODO
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage, Point2D offset, int tag) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, offset, tag);

        this.controller = controller;
        this.damage = damage;
        setHitbox(10);// TODO: hitboxin koko kovakoodattu | 16
        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
    }

    /**
     * Konstruktori värin valinnalla.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset TODO
     * @param color Ammuksen väri.
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage,
                           Point2D offset, Color color, int tag) {
        this(controller, shooter, speed, damage, offset, tag);
        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
    }

    /**
     * Konstruktori suunnan ja värin valinnalla.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset TODO
     * @param color Ammuksen väri.
     * @param direction Ammuksen suunta.
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage,
                           Point2D offset, Color color, double direction, int tag) {
        this(controller, shooter, speed, damage, offset, color, tag);
        rotate(direction);
    }

    @Override
    public void collides(Object collidingTarget) {
        if(collidingTarget instanceof Unit){
            ((Unit) collidingTarget).takeDamage(damage);
        }
        destroyThis();
    }

    @Override
    public void destroyThis(){
        controller.removeUpdateable(this, this);
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

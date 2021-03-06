package model.projectiles;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.HitboxCircle;
import model.Tag;
import model.Updateable;
import model.units.Unit;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Pieni perusammus
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class SmallProjectile extends BaseProjectile implements Updateable, HitboxCircle {

    /**
     * Ammuksen vakioväri
     */
    private static final Color COLOR = Color.WHITE;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset Ammuken alkusijainnin poikkeama aluksesta.
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Unit shooter, double speed, int damage, Point2D offset, Tag tag) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(shooter, speed, offset, damage, tag);

        controller = GameController.getInstance();
        setHitbox(10);
        Polygon shape = buildProjectile(speed, COLOR);
        try {
            getChildren().add(shape);
        }catch (IllegalArgumentException e) {}
    }

    /**
     * Konstruktori värin valinnalla.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset Ammuken alkusijainnin poikkeama aluksesta.
     * @param color Ammuksen väri.
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Unit shooter, double speed, int damage, Point2D offset, Color color, Tag tag) {
        this(shooter, speed, damage, offset, tag);
        Polygon shape = buildProjectile(speed, color);
        try {
            getChildren().add(shape);
        }catch (IllegalArgumentException e) {}
    }

    /**
     * Konstruktori suunnan ja värin valinnalla.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param offset Ammuken alkusijainnin poikkeama aluksesta.
     * @param color Ammuksen väri.
     * @param direction Ammuksen suunta.
     * @param tag Ammuksen tagi.
     */
    public SmallProjectile(Unit shooter, double speed, int damage, Point2D offset, Color color, double direction,
                           Tag tag) {
        this(shooter, speed, damage, offset, color, tag);
        rotate(direction);
    }

    @Override
    public void collides(Object collidingTarget) {
        if(collidingTarget instanceof Unit){
            ((Unit) collidingTarget).takeDamage((int)getDamage());
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
        shape.getPoints().addAll(-7.0, 2.5,
                -7.0, -2.5,
                0.0, -6.0,
                speed*0.4+7.0, 0.0, // ammuksen hanta skaalautuu nopeuden mukaan, mutta on ainakin 7.0
                0.0, 6.0);

        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(1.0);
        blur.setInput(bloom);
        //shape.setEffect(blur);
        shape.setFill(Color.WHITE);
        shape.setStroke(color);
        shape.setStrokeWidth(3.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
    }
}

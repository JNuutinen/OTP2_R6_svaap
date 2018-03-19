package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.Unit;
import model.Updateable;

import static view.GameMain.WINDOW_WIDTH;

/**
 * Lasersädeammus.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class LaserBeam extends BaseProjectile implements Updateable {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Ammuksen tekemä vahinko.
     */
    private int damage;

    /**
     * Ammuksen shape (viiva).
     */
    private Polyline shape;

    /**
     * Ammuksen väri.
     */
    private Color color = Color.WHITE;

    /**
     * Kertoo osuiko ammus.
     */
    private boolean hitTarget = false;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param color Ammuksen väri.
     * @param tag Ammuksen tagi.
     * @param frontOffset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     * @param leftOffset Ammuksen aloituspaikan poikkeus aluksen vasempaan suuntaan.
     */
    public LaserBeam(Controller controller, Unit shooter, double speed, int damage, Color color, int tag, double frontOffset, double leftOffset) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, frontOffset, leftOffset, tag);

        this.controller = controller;
        this.damage = damage;


        shape = buildLaser(color);
        getChildren().add(shape);
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this);
    }

    @Override
    //Optimoidumpi versio collidesta (?)
    public void collides(Updateable collidingUpdateable){
        ((Unit)collidingUpdateable).takeDamage(damage);
    }


    @Override
    public void update(double deltaTime) {

        double deltaTimeMultiplied = deltaTime * 3;

        if(color.getRed() > deltaTimeMultiplied) {
            color = new Color(color.getRed() - deltaTimeMultiplied,
                    1, color.getBlue() - deltaTimeMultiplied, color.getOpacity() - deltaTimeMultiplied);
            shape.setStroke(color);
        }
        else{
            destroyThis();
        }

    }

    /**
     * Rakentaa projectilen Shapen
     *
     * @param color Projectilen väri
     * @return Rakennettu Shape
     */
    private Polyline buildLaser(Color color) {
        // Ammuksen muoto
        shape = new Polyline();
        shape.getPoints().addAll(-0.0, 0.0,
                WINDOW_WIDTH, 0.0);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        shape.setEffect(blur);
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(color);
        shape.setStrokeWidth(7.0);
        return shape;
    }
}
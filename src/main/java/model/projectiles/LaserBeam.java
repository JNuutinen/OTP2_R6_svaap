package model.projectiles;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.Trace;
import model.Unit;
import model.Updateable;

import java.util.List;

import static view.GameMain.WINDOW_WIDTH;

/**
 * Lasersädeammus.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class LaserBeam extends BaseProjectile implements Updateable, Trace {

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
     * Laserin aloitusväri jota häivytetään.
     */
    private Color currentColor = Color.WHITE;

    /**
     * nykyisestä väristä vähennettävä punainen arvo häivytyksen aikana.
     */
    private double redSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä vihreä arvo häivytyksen aikana.
     */
    private double greenSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä sininen arvo häivytyksen aikana.
     */
    private double blueSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä läpinäkyvyysarvo häivytyksen aikana.
     */
    private double opacitySubtraction = 1;

    /**
     * Kulunut aika olion luomisesta.
     */
    private double timeSinceSpawn = 0;

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
     * @param offset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     */
    public LaserBeam(Controller controller, Unit shooter, double speed, int damage, Color color, int tag, Point2D offset) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, offset, tag);

        redSubtraction = (1 - color.getRed());
        greenSubtraction = (1 - color.getGreen());
        blueSubtraction = (1 - color.getBlue());
        opacitySubtraction = opacitySubtraction * 0.5;
        //opacitySubtraction pitää muuttaa myös jos noita kaavoja muutetaan ^

        this.controller = controller;
        this.damage = damage;

        shape = buildLaser(currentColor);
        Platform.runLater(()->getChildren().add(shape)); // TODO hidastaaks tää oikeesti


    }

    @Override
    public List<Point2D> getTraceCoordinates() {
        return null;
    }

    @Override
    public void collides(Object obj) {
        if(obj instanceof Unit){
            ((Unit)obj).takeDamage(damage);
        }
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this, this);
    }


    @Override
    public void update(double deltaTime) {
        timeSinceSpawn += deltaTime;
        double deltaTimeMultiplied = deltaTime * 7;


        // laserin väri pysyy valkoisena 0.15 sec ajan ja sitten alkaa häipymään pois.
        if(timeSinceSpawn > 0.15){
            if(currentColor.getOpacity() * opacitySubtraction < currentColor.getOpacity()) {
                double newRedValue = 0;
                double newGreenValue = 0;
                double newBlueValue = 0;
                double newOpacity = 0;


                if(currentColor.getRed() > (redSubtraction * deltaTimeMultiplied)){
                    newRedValue = currentColor.getRed() - (redSubtraction * deltaTimeMultiplied);
                }
                if(currentColor.getGreen() > (greenSubtraction * deltaTimeMultiplied)){
                    newGreenValue = currentColor.getGreen() - (greenSubtraction * deltaTimeMultiplied);
                }
                if(currentColor.getBlue() > (blueSubtraction * deltaTimeMultiplied)){
                    newBlueValue = currentColor.getBlue() - (blueSubtraction * deltaTimeMultiplied);
                }
                if(currentColor.getOpacity() > (opacitySubtraction * deltaTimeMultiplied)){
                    newOpacity = currentColor.getOpacity() - (opacitySubtraction * deltaTimeMultiplied);
                }
                currentColor = new Color(newRedValue, newGreenValue, newBlueValue, newOpacity);
                shape.setStroke(currentColor);
            }
            else{
                destroyThis();
            }
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
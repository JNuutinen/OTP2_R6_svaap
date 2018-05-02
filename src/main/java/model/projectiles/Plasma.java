package model.projectiles;

import controller.Controller;
import controller.GameController;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.HitboxCircle;
import model.Sprite;
import model.Tag;
import model.Updateable;
import model.units.Unit;

import static view.GameMain.WINDOW_WIDTH;

/**
 * Lasersädeammus.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Plasma extends BaseProjectile implements Updateable, HitboxCircle {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

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
    private double redSubtraction;

    /**
     * nykyisestä väristä vähennettävä vihreä arvo häivytyksen aikana.
     */
    private double greenSubtraction;

    /**
     * nykyisestä väristä vähennettävä sininen arvo häivytyksen aikana.
     */
    private double blueSubtraction;

    /**
     * nykyisestä väristä vähennettävä läpinäkyvyysarvo häivytyksen aikana.
     */
    private double opacitySubtraction = 1;

    /**
     * Kulunut aika olion luomisesta.
     */
    private double timeSinceSpawn = 0;

    /**
     * Ammuksen kohde.
     */
    private HitboxCircle target;

    /**
     * Kertoo, onko ohjuksen kohde jo kerran haettu.
     */
    private boolean findTargerOnce = false;

    /**
     * Kertoo, onko ohjuksen kohde haettu kahdesti.
     */
    private boolean findTargetTwice = false;

    /**
     * Apumuuttuja, joka pitää kirjaa lähimmästä matkasta kohteeseensa sen elinkaaren aikana.
     * Käytetään sen määrittämisessä, menettääkö ohjust lukituksen kohteeseensa.
     */
    private double closestDistance = 999999;

    /**
     * Konstruktori.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param color Ammuksen väri.
     * @param tag Ammuksen tagi.
     * @param offset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     */
    public Plasma(Unit shooter, double speed, int damage, Color color, Tag tag, Point2D offset) {
        super(shooter, speed, offset, damage * 0, tag);
        setHitbox(500);
        controller = GameController.getInstance();
        redSubtraction = (1 - color.getRed());
        greenSubtraction = (1 - color.getGreen());
        blueSubtraction = (1 - color.getBlue());
        opacitySubtraction = opacitySubtraction * 0.5;

        findAndSetTarget();
        shape = buildPlasma(currentColor);
        Platform.runLater(()->getChildren().add(shape)); // TODO hidastaaks tää oikeesti



    }


    @Override
    public void collides(Object collidingTarget) {
        if(collidingTarget instanceof Unit){
            ((Unit) collidingTarget).takeDamage((int)getDamage());
        }
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this, this);
    }


    @Override
    public void update(double deltaTime) {
        findAndSetTarget();
        shape = buildPlasma(currentColor);

        timeSinceSpawn += deltaTime;
        double deltaTimeMultiplied = deltaTime * 7;

        // laserin väri pysyy valkoisena 0.15 TODO sec ajan ja sitten alkaa häipymään pois.
        if(timeSinceSpawn > 1.15){
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
     * Asettaa lähimmän vihollisen missilen kohteeksi.
     */
    private void findAndSetTarget() {
        double shortestDistance = Double.MAX_VALUE;
        HitboxCircle closestEnemy = null;
        if (getShooter().getTag() == Tag.SHIP_PLAYER) {
            for (HitboxCircle hitboxCircle : controller.getHitboxObjects()) {
                if (((Sprite) hitboxCircle).getTag() == Tag.SHIP_ENEMY
                        || ((Sprite) hitboxCircle).getTag() == Tag.SHIP_BOSS) {
                    double distance = getShooter().getDistanceFromTarget(hitboxCircle.getPosition());
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        closestEnemy = hitboxCircle;
                        if (findTargerOnce) {
                            findTargetTwice = true;
                        } else {
                            findTargerOnce = true;
                        }
                    }
                }
            }
        } else if (getShooter().getTag() == Tag.SHIP_ENEMY || getShooter().getTag() == Tag.SHIP_BOSS) {
            for (HitboxCircle hitboxCircle : controller.getPlayerHitboxObjects()) {
                double distance = getShooter().getDistanceFromTarget(hitboxCircle.getPosition());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestEnemy = hitboxCircle;
                    if (findTargerOnce) {
                        findTargetTwice = true;
                    } else {
                        findTargerOnce = true;
                    }
                }
            }
        }
        target = closestEnemy;
        closestDistance = Double.MAX_VALUE; // asettaa lähimmän kohteen etäisyyden maksimiin koska kohde vaihtui
    }

    /**
     * Rakentaa projectilen Shapen
     * @param color Projectilen väri
     * @return Rakennettu PolyLine
     */
    private Polyline buildPlasma(Color color) {
        // Ammuksen muoto
        shape = new Polyline();
        if(target != null) {
            shape.getPoints().addAll(-0.0, 0.0,
                    target.getPosition().getX(), target.getPosition().getY());
        }
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

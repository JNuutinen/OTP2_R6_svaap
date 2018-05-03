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
import model.fx.PlasmaTrail;
import model.units.Unit;

import static view.GameMain.WINDOW_WIDTH;

/**
 * Lasersädeammus.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Plasma extends BaseProjectile implements HitboxCircle {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;





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

    private PlasmaTrail plasmaTrail;

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

        plasmaTrail = new PlasmaTrail();

        findAndSetTarget();
        if(target != null){
            plasmaTrail.setTargetLocation(target.getPosition());
        }
        destroyThis();


    }


    @Override
    public void collides(Object collidingTarget) {
        if(collidingTarget instanceof Unit){
            ((Unit) collidingTarget).takeDamage((int)getDamage());
        }
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(null, this);
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
}

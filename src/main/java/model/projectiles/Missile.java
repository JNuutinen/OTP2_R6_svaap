package model.projectiles;

import controller.Controller;
import controller.GameController;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.*;
import model.fx.Explosion;
import model.fx.Trail;
import model.units.Unit;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Ohjusammuksen luokka.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Missile extends BaseProjectile implements Updateable, HitboxCircle {

    /**
     * Etäisyys pikseleinä, jonka ammus voi mennä x- tai y-akselilla ruudun ulkopuolelle,
     * ennen kuin se poistetaan pelistä.
     */
    private static final double OUT_OF_AREA_TRESHOLD = 500;

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
     * Ammuksen visuaalinen häntä.
     */
    private Trail trail;

    /**
     * Apumuuttuja, joka pitää kirjaa lähimmästä matkasta kohteeseensa sen elinkaaren aikana.
     * Käytetään sen määrittämisessä, menettääkö ohjust lukituksen kohteeseensa.
     */
    private double closestDistance = 999999;

    /**
     * Apumuuttuja joka määrittelee voiko ohjus hävittää kohteen jos menee liian kauas kohteesta
     */
    private boolean canLoseTarget = true;

    /**
     * apumuuttuja joka laskee aikaa viime kerrasta kun haki kohdetta. Nollaantuu haun jälkeen.
     */
    private double findTargetTimeCounter = 0;

    /**
     * Konstruktori.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param tag Ammuksen tagi.
     */
    public Missile(Unit shooter, double speed, int damage, double rotatingSpeed, Tag tag) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(shooter, speed, damage, tag);
        this.rotatingSpeed = rotatingSpeed;
        controller = GameController.getInstance();

        Polygon shape = buildProjectile(speed);
        getChildren().add(shape);
        setHitbox(10);

        trail = new Trail(this, shooter.getUnitColor());
        this.getChildren().addAll(trail);

        findAndSetTarget();
    }

    /**
     * Konstruktori, jossa myös canLoseTarget
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param tag Ammuksen tagi.
     * @param canLoseTarget voiko ohjus kadottaa kohteen jos siirtyy liikaa liian kauas koteesta
     */
    public Missile(Unit shooter, double speed, int damage, double rotatingSpeed, Tag tag, boolean canLoseTarget) {
        this(shooter, speed, damage, rotatingSpeed, tag);
        this.canLoseTarget = canLoseTarget;
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
        trail.destroyThis();
        new Explosion(getShooter().getUnitColor(), getPosition(), 0.3);
        controller.removeUpdateable(this, this);
    }

    @Override
    public void update(double deltaTime) {

        findTargetTimeCounter += deltaTime;
        // chekkaa menikö ulos ruudulta
        // Nää arvot vähän isompia kuin uniteilla, että ammukset voi kaartaa ruudun ulkopuolelta
        if (getXPosition() < -OUT_OF_AREA_TRESHOLD
                || getXPosition() > WINDOW_WIDTH + OUT_OF_AREA_TRESHOLD
                || getYPosition() < -OUT_OF_AREA_TRESHOLD
                || getYPosition() > WINDOW_HEIGHT + OUT_OF_AREA_TRESHOLD) {
            destroyThis();
        } else {
            moveStep(deltaTime * getVelocity());
        }

        double angleToTarget;
        if (target != null) {
            if (controller.getUpdateables().contains(target)) {
                double distanceToTarget = getDistanceFromTarget(target.getPosition());
                if(distanceToTarget < closestDistance){
                    closestDistance = distanceToTarget;
                }

                angleToTarget = getAngleFromTarget(target.getPosition()) - getDirection();
                if(distanceToTarget < closestDistance + 30 && canLoseTarget){
                    // taa vaa pitaa asteet -180 & 180 valissa
                    while (angleToTarget >= 180.0) {
                        angleToTarget -= 360.0;
                    }
                    while (angleToTarget < -180) {
                        angleToTarget += 360.0;
                    }
                    rotate(angleToTarget * rotatingSpeed * deltaTime);
                }
                else if(!canLoseTarget){
                    while (angleToTarget >= 180.0) {
                        angleToTarget -= 360.0;
                    }
                    while (angleToTarget < -180) {
                        angleToTarget += 360.0;
                    }
                    rotate(angleToTarget * rotatingSpeed * deltaTime);
                }
            } else if ((!findTargerOnce || !findTargetTwice) && findTargetTimeCounter > 0.1) {
                findTargetTimeCounter = 0;
                findAndSetTarget();
            }
        } else if ((!findTargerOnce || !findTargetTwice) && findTargetTimeCounter > 0.1){
            findTargetTimeCounter = 0;
            findAndSetTarget();
        }
    }

    /**
     * Rakentaa projectilen Polygonin
     * @param speed Projectilen nopeus, vaikuttaa hännän pituuteen
     * @return Rakennettu Polygon
     */
    private Polygon buildProjectile(double speed) {
        // Ammuksen muoto
        Polygon shape = new Polygon();
        shape.getPoints().addAll(-9.0, 0.0,
                0.0, -3.0,
                speed*0.6+1.0, 0.0, // ammuksen häntä skaalautuu nopeuden mukaan, mutta on ainakin 1.0
                0.0, 3.0);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(Color.WHITE);
        shape.setStrokeWidth(5.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
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
     * Asettaa missilen kääntymisnopeuden.
     * @param rotatingSpeed Missilen kääntymisnopeus.+
     */
    public void setRotatingSpeed(double rotatingSpeed){
        this.rotatingSpeed = rotatingSpeed;
    }

    /**
     * Palauttaa ammuksen kääntymisnopeuden.
     * @return Ammuksen kääntymisnopeus.
     */
    public double getRotatingSpeed(){
        return rotatingSpeed;
    }

}

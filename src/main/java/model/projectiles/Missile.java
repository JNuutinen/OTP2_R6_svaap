package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.*;

import static view.GameMain.*;

/**
 * Ohjusammuksen luokka. Luokassa vain ohjukselle ominaisten
 * piirteiden hallinta, kaikille ammuksille yhteiset piirteet
 * yliluokassa (BaseProjectile).
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Missile extends BaseProjectile implements Updateable, HitboxObject {

    /**
     * Ohjuksen perusväri.
     */
    private static final Color COLOR = Color.YELLOW;

    /**
     * Etäisyys pikseleinä, jonka ammus voi mennä x- tai y-akselilla ruudun ulkopuolelle,
     * ennen kuin se poistetaan pelistä.
     */
    private static final double OUT_OF_AREA_TRESHOLD = 500;

    /**
     * Ohjuksen tekemä vahinko
     */
    private int damage;

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
    private HitboxObject target;

    /**
     * Ammuksen visuaalinen häntä.
     */
    private Trail trail;

    /**
     * Kertoo, osoittaako ammus spawnatessaan kohdettaansa kohti.
     */
    private boolean initialDirectionToTarget = false;

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
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param tag Ammuksen tagi.
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage, double rotatingSpeed, int tag) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, tag);
        this.rotatingSpeed = rotatingSpeed;
        this.controller = controller;
        this.damage = damage;

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);

        trail = new Trail(controller, this);
        this.getChildren().addAll(trail);
    }

    /**
     * Konstruktori, jossa myös canLoseTarget
     * @param controller Pelin kontrolleri.
     * @param shooter Ammuksen ampuja.
     * @param speed Ammuksen nopeus.
     * @param damage Ammuksen tekemä vahinko.
     * @param rotatingSpeed Ammuksen kääntymisnopeus.
     * @param tag Ammuksen tagi.
     *
     * @param canLoseTarget voiko ohjus kadottaa kohteen jos siirtyy liikaa liian kauas koteesta
     */
    public Missile(Controller controller, Unit shooter, double speed, int damage, double rotatingSpeed, int tag, boolean canLoseTarget) {
        this(controller, shooter, speed, damage, rotatingSpeed, tag);
        this.canLoseTarget = canLoseTarget;
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
        trail.destroyThis();
        new Explosion(controller, Color.WHITE, getPosition(), 0.2);
        controller.removeUpdateable(this, this);
    }

    @Override
    public void update(double deltaTime) {
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
                if(distanceToTarget < closestDistance + 40 && canLoseTarget){
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
        HitboxObject closestEnemy = null;
        if (getShooter().getTag() == PLAYER_SHIP_TAG){
            for (HitboxObject hitboxObject : controller.getHitboxObjects()) {
                if (hitboxObject.getTag() == ENEMY_SHIP_TAG || hitboxObject.getTag() == BOSS_SHIP_TAG) {
                    double distance = getShooter().getDistanceFromTarget(hitboxObject.getPosition());
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        closestEnemy = hitboxObject;
                    }
                }
            }
        }
        else if(getShooter().getTag() == ENEMY_SHIP_TAG || getShooter().getTag() == BOSS_SHIP_TAG){
            for (HitboxObject hitboxObject : controller.getPlayerHitboxObjects()) {
                double distance = getShooter().getDistanceFromTarget(hitboxObject.getPosition());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestEnemy = hitboxObject;
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

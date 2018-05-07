package model.units;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import model.HitboxCircle;
import model.Tag;
import model.fx.TeleportEffect;

import java.util.List;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/**
 * Vihollinen, joka peliin lisätessä ilmaantuu näkyviin, kuin se teleporttaisi siihen.
 * Pysyy paikallaan, mutta tähtäilee pelaajaa kohti.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class TeleporterEnemy extends Unit {

    /**
     * Aluksen väri.
     */
    private static final Color COLOR = Color.ROSYBROWN;

    /**
     * Pelin kontrolleri
     */
    private Controller controller;

    /**
     * Osuma-efketin totuusarvo.
     */
    private boolean tookDamage = false;

    /**
     * Osuma-efektin aikalaskuri
     */
    private double damagedTimeCounter = 0;

    /**
     * Aluksen kääntymisnopeus
     */
    private double rotatingSpeed = 4;

    /**
     * Kertoo, ampuuko alus kohdettaan.
     */
    private boolean shootingTarget = false;

    /**
     * Aluksen kohde, jota se seuraa "katseellaan".
     */
    private HitboxCircle target = null;

    /**
     * Boolean flagi, joka kertoo ollaanko teleportattu ts. onko update() metodissa käyty.
     * Tämän avulla teleporttausefekti tehdään ainoastaan ensimmäisellä update() kutsulla.
     */
    private boolean teleported = false;

    /**
     * Konstruktori.
     *
     * @param primaries Lista, jossa aluksen aseet tageina ilmoitettuna.
     * @param position  Aloitussijainnin koordinaatit.
     */
    public TeleporterEnemy(List<Tag> primaries, Point2D position) {
        super(COLOR, 5, 20);
        controller = GameController.getInstance();
        setTag(Tag.SHIP_ENEMY);
        setPosition(position.getX(), position.getY());
        rotate(180);
        setIsMoving(false);
        setHitbox(80);
        setHp(40);
        drawShip(buildShape());
        makePrimaryWeapons(primaries);
        controller.addUpdateableAndSetToScene(this);
        controller.addHitboxObject(this);
        findAndSetTarget();
    }

    @Override
    public void update(double deltaTime) {
        if (!teleported) {
            new TeleportEffect(COLOR, getPosition(), getUnitSize());
            teleported = true;
        }
        if (getTookDamage()) {
            tookDamage = true;
            damagedTimeCounter = 0;
            setTookDamage(false);
        }
        if (tookDamage && damagedTimeCounter > 0.1) {
            tookDamage = false;
            setOriginalColor();
            damagedTimeCounter = 0;
        } else if (tookDamage) {
            damagedTimeCounter += deltaTime;
        }

        shootPrimary();

        // chekkaa menikö ulos ruudulta
        if (getXPosition() < -100
                || getXPosition() > WINDOW_WIDTH + 200
                || getYPosition() < -100
                || getYPosition() > WINDOW_HEIGHT + 100) {
            destroyThis();
        } else {
            moveStep(deltaTime);
        }

        double angleToTarget;
        if (target != null) {
            angleToTarget = getAngleFromTarget(target.getPosition()) - getDirection();
            // taa vaa pitaa asteet -180 & 180 valissa
            while (angleToTarget >= 180.0) {
                angleToTarget -= 360.0;
            }
            while (angleToTarget < -180) {
                angleToTarget += 360.0;
            }

            rotate(angleToTarget * rotatingSpeed * deltaTime);

        }
    }

    /**
     * Rakentaa aluksen Shapen.
     *
     * @return Rakennettu Shape.
     */
    private Shape buildShape() {
        Polygon shape = new Polygon();
        shape.getPoints().addAll(
                30.0, 10.0,
                60.0, 10.0,
                50.0, 20.0,
                10.0, 30.0,
                0.0, 20.0,
                -10.0, 40.0,
                -40.0, 30.0,
                -50.0, 20.0,
                -30.0, 20.0,
                -30.0, -20.0,
                -50.0, -20.0,
                -40.0, -30.0,
                -10.0, -40.0,
                0.0, -20.0,
                10.0, -30.0,
                50.0, -20.0,
                60.0, -10.0,
                30.0, -10.0
        );
        return shape;
    }

    /**
     * Etsii lähimmän pelaajan playerHitboxObjects listasta ja asettaa sen kohteeksi
     */
    public void findAndSetTarget() {
        double shortestDistance = Double.MAX_VALUE;
        HitboxCircle closestPlayer = null;
        for (HitboxCircle hitboxCircle : controller.getPlayerHitboxObjects()) {
            double distance = getDistanceFromTarget(hitboxCircle.getPosition());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestPlayer = hitboxCircle;
            }
        }
        target = closestPlayer;
    }
}

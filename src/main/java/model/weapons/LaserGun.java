package model.weapons;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Component;
import model.Player;
import model.Updateable;
import model.projectiles.LaserBeam;

import static view.GameMain.*;

/**
 * Laserpyssy.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class LaserGun extends Component implements Weapon, Updateable {

    /**
     * Aseen ammuksien nopeus
     */
    private static final int SPEED = 30;

    /**
     * Aseen ammuksien vahinko.
     */
    private static final int DAMAGE = 15;

    /**
     *  Aseen tulinopeus.
     */
    private static final double FIRE_RATE = 1.0;

    /**
     * Aseen väri.
     */
    private static final Color COLOR = Color.LIME;

    /**
     * Kontrolleriin viittaus projectilen spawnaamisen mahdollistamiseen.
     */
    private Controller controller;

    /**
     * Ampumisen viive.
     */
    private double shootingDelay = 0;

    /**
     * Ampumisen aikalaskuri.
     */
    private double timeCounter = 0;

    /**
     * Apumuuttuja
     */
    private boolean triggeredShoot = false;

    /**
     * Aseen latausefekti.
     */
    private Circle chargingEffect;

    /**
     * Aseen osoitinefekti.
     */
    private Line pointerEffect;

    /**
     * Ammuksen värinmuutos stopit
     */
    private Stop[] stops1;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param shootingDelay Ampumisen viive.
     */
    public LaserGun(Controller controller, int orientation, double shootingDelay) {
        super("triangle", 4, orientation, COLOR);
        this.shootingDelay = shootingDelay;
        this.controller = controller;
        controller.addUpdateable(this);
    }

    /**
     * Konstruktori ampumisviiveen kanssa. TODO param järjestys
     * @param controller Pelin kontrolleri.
     * @param orientation Aseen orientation.
     * @param componentOffset Aseen x-offset.
     * @param projectileOffset Ammuksen aloituspaikan poikkeus aluksen etusuuntaan.
     * @param shootingDelay Ampumisen viive.
     */
    public LaserGun(Controller controller, int orientation, double shootingDelay, Point2D componentOffset, Point2D projectileOffset) {
        this(controller, orientation, shootingDelay);
        setProjectileOffset(projectileOffset);
        setComponentOffset(componentOffset);
        controller.addUpdateable(this);
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void shoot() {
        if(!triggeredShoot && getParentUnit() != null) {

            chargingEffect = buildChargingEffect(Color.WHITE);
            Platform.runLater(()->getParentUnit().getChildren().add(chargingEffect));
            pointerEffect = buildPointerEffect(Color.WHITE);
            Platform.runLater(()->getParentUnit().getChildren().add(pointerEffect));

            if (getParentUnit() instanceof Player) {
                chargingEffect.setCenterX(degreesToVector(getParentUnit().getDirection()).getX() * getProjectileOffset().getX());
                chargingEffect.setCenterY(degreesToVector(getParentUnit().getDirection() + 90).getY() * getProjectileOffset().getY());

                pointerEffect.setStartX(degreesToVector(getParentUnit().getDirection()).getX() * getProjectileOffset().getX());
                pointerEffect.setStartY(degreesToVector(getParentUnit().getDirection() + 90).getY() * getProjectileOffset().getY());

            } else {
                chargingEffect.setCenterX(degreesToVector(getParentUnit().getDirection()).getX() * getProjectileOffset().getX() * -1);
                chargingEffect.setCenterY(degreesToVector(getParentUnit().getDirection() + 90).getY() * getProjectileOffset().getY() * -1);

                pointerEffect.setStartX(degreesToVector(getParentUnit().getDirection()).getX() * getProjectileOffset().getX() * -1);
                pointerEffect.setStartY(degreesToVector(getParentUnit().getDirection() + 90).getY() * getProjectileOffset().getY() * -1);
            }
            pointerEffect.setEndX(pointerEffect.getStartX() + (WINDOW_WIDTH));
            pointerEffect.setEndY(pointerEffect.getStartY());

            triggeredShoot = true;
        }

    }



    @Override
    public void update(double deltaTime) {
        if(triggeredShoot){ // jos ase on lataamassa laseria
            timeCounter += deltaTime; // viime silmukasta kulunut aika lisätään aikalaskuriin
            chargingEffect.setRadius(chargingEffect.getRadius() + deltaTime * 17 / shootingDelay);
            chargingEffect.setStrokeWidth(chargingEffect.getStrokeWidth() + deltaTime * 4 / shootingDelay);

            // jos latausefektin läpinäkyvyysarvo on suurempi kuin lisättävä näkyvyysarvo, niin lisää näkyvyysarvo
            if(1 - stops1[0].getColor().getOpacity() > deltaTime * 1 / shootingDelay) {
                stops1[0] = new Stop(0, Color.color(0, 1, 0, stops1[0].getColor().getOpacity() + deltaTime * 1 / shootingDelay));
                pointerEffect.setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1));
            }

            // jos aikaa on kulunut enemmän kuin ampumisviiveessä, niin luo LaserBeam peliin.
            if(timeCounter > shootingDelay){
                LaserBeam laserBeam = new LaserBeam(controller, getParentUnit(), SPEED, DAMAGE, Color.WHITE, getTag(), getProjectileOffset());
                controller.addUpdateableAndSetToScene(laserBeam, laserBeam);
                triggeredShoot = false;
                timeCounter = 0;
                Platform.runLater(()->getParentUnit().getChildren().remove(chargingEffect));
                Platform.runLater(()->getParentUnit().getChildren().remove(pointerEffect));
            }
        }
    }

    /**
     * Rakentaa latausefektin.
     * @param color Efektin väri.
     * @return Tehty efekti.
     */
    private Circle buildChargingEffect(Color color) {
        // Ammuksen muoto
        chargingEffect = new Circle();
        chargingEffect.setRadius(1);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(6);
        blur.setInput(bloom);
        chargingEffect.setEffect(blur);
        chargingEffect.setFill(color);
        chargingEffect.setStroke(Color.GREEN);
        chargingEffect.setStrokeWidth(1);
        return chargingEffect;
    }

    /**
     * Rakentaa osoitinefektin.
     * @param color Efektin väri.
     * @return Tehty efekti.
     */
    private Line buildPointerEffect(Color color){
        stops1 = new Stop[] { new Stop(0, Color.color(0, 1, 0, 0)), new Stop(1, Color.TRANSPARENT)};
        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1);
        Line line = new Line(); //koordinaatit määritellään shoot()-metodissa
        line.setStroke(lg);
        return line;
    }
}


package model.weapons;

import controller.Controller;
import controller.GameController;
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
import model.Updateable;
import model.projectiles.LaserBeam;

import static view.GameMain.WINDOW_WIDTH;

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
    private static final int DAMAGE = 40;

    /**
     *  Aseen tulinopeus.
     */
    private static final double FIRE_RATE = 1.0;

    /**
     * Aseen väri.
     */
    private static final Color COLOR = Color.LIME;

    /**
     * Laserin aloitusväri joka on valkoinen ja jota häivytetään.
     */
    private Color effectsColor = Color.WHITE;

    /**
     * nykyisestä laserista vähennettävä läpinäkyvyysarvo häivytyksen aikana.
     */
    private double opacityAddition = 0;

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
     * Apumuuttuja
     */
    private boolean readyToShoot = true;

    /**
     * Ammusten vahinkomääräkerroin.
     */
    private double damageMultiplier = 1;

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
     * @param orientation Aseen orientation.
     * @param shootingDelay Ampumisen viive.
     */
    public LaserGun(int orientation, double shootingDelay) {
        super("triangle", 4, orientation, COLOR);
        this.shootingDelay = shootingDelay;
        controller = GameController.getInstance();
        controller.addUpdateable(this);
    }

    /**
     * Konstruktori ampumisviiveen kanssa.
     * @param orientation Aseen orientation.
     * @param shootingDelay Ampumisen viive
     * @param componentOffset Aseen visuaalinen poikkeama aluksesta.
     * @param projectileOffset Ammuksen aloituspaikan poikkeama aluksesta.
     * .
     */
    public LaserGun(int orientation, double shootingDelay, Point2D componentOffset, Point2D projectileOffset) {
        this(orientation, shootingDelay);
        setProjectileOffset(projectileOffset);
        setComponentOffset(componentOffset);
        controller.addUpdateable(this);
    }

    @Override
    public double getFireRate() {
        return FIRE_RATE;
    }

    @Override
    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void shoot() {
        if(!triggeredShoot && getParentUnit() != null) {
            effectsColor = new Color(getParentUnitColor().getRed(), getParentUnitColor().getGreen(), getParentUnitColor().getBlue(), 0);
            opacityAddition = 0;

            chargingEffect = buildChargingEffect(effectsColor);
            Platform.runLater(()->getParentUnit().getChildren().add(chargingEffect));
            pointerEffect = buildPointerEffect(effectsColor);
            Platform.runLater(()->getParentUnit().getChildren().add(pointerEffect));

            chargingEffect.setCenterX(getProjectileOffset().getX());
            chargingEffect.setCenterY(getProjectileOffset().getY());

            pointerEffect.setStartX(getProjectileOffset().getX());
            pointerEffect.setStartY(getProjectileOffset().getY());
            pointerEffect.setEndX(WINDOW_WIDTH);
            pointerEffect.setEndY(getProjectileOffset().getY());

            if(readyToShoot){
                triggeredShoot = true;
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        if(getParentUnit() != null) {
            if (!getParentUnit().isNull()) {
                if (triggeredShoot) { // jos ase on lataamassa laseria
                    readyToShoot = false;
                    timeCounter += deltaTime; // viime silmukasta kulunut aika lisätään aikalaskuriin
                    chargingEffect.setRadius(chargingEffect.getRadius() + deltaTime * 17 / shootingDelay);
                    chargingEffect.setStrokeWidth(chargingEffect.getStrokeWidth() + deltaTime * 4 / shootingDelay);


                    chargingEffect.setCenterX(getProjectileOffset().getX());
                    chargingEffect.setCenterY(getProjectileOffset().getY());

                    pointerEffect.setStartX(getProjectileOffset().getX());
                    pointerEffect.setStartY(getProjectileOffset().getY());
                    pointerEffect.setEndX(WINDOW_WIDTH);
                    pointerEffect.setEndY(getProjectileOffset().getY());

                    double deltaTimeMultiplied = deltaTime * 1;

                    if (1 - opacityAddition > (deltaTimeMultiplied) / shootingDelay) {
                        opacityAddition = opacityAddition + (deltaTimeMultiplied) / shootingDelay;


                        effectsColor = new Color(effectsColor.getRed(), effectsColor.getGreen(), effectsColor.getBlue(), opacityAddition);
                        pointerEffect.setStroke(effectsColor);
                        chargingEffect.setStroke(effectsColor);
                    }

                    // kun ase on lataus on täynnä, niin ammu.
                    if (timeCounter > shootingDelay) {
                        LaserBeam laserBeam = new LaserBeam(getParentUnit(), SPEED, (int) (DAMAGE * damageMultiplier), getParentUnitColor(), getComponentProjectileTag(),
                                new Point2D(getProjectileOffset().getX(), getProjectileOffset().getY()));
                        controller.addUpdateableAndSetToScene(laserBeam);
                        controller.addTrace(laserBeam);
                        triggeredShoot = false;
                        readyToShoot = true;
                        timeCounter = 0;
                        Platform.runLater(() -> getParentUnit().getChildren().remove(chargingEffect));
                        Platform.runLater(() -> getParentUnit().getChildren().remove(pointerEffect));
                    }
                }
            } else {
                Platform.runLater(() -> getParentUnit().getChildren().remove(chargingEffect));
                Platform.runLater(() -> getParentUnit().getChildren().remove(pointerEffect));
                controller.removeUpdateable(this);
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
        chargingEffect.setFill(Color.WHITE);
        chargingEffect.setStroke(color);
        chargingEffect.setStrokeWidth(1);
        return chargingEffect;
    }

    /**
     * Rakentaa osoitinefektin.
     * @param color Efektin väri.
     * @return Tehty efekti.
     */
    private Line buildPointerEffect(Color color){
        stops1 = new Stop[] { new Stop(0, color), new Stop(1, color)};
        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops1);
        Line line = new Line(); //koordinaatit määritellään shoot()-metodissa
        line.setStroke(lg);
        return line;
    }
}


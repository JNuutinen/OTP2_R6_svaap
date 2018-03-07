package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import model.Component;
import model.Player;
import model.Unit;
import model.Updateable;

import static view.GameMain.*;

public class LaserBeam extends BaseProjectile implements Updateable {

    private static final Color COLOR = Color.WHITE;
    private Controller controller;
    private int damage;
    private Polyline shape;
    private Color color = Color.WHITE;
    private boolean hitTarget = false;

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
    /*
    @Override
    public void collides(Updateable collidingUpdateable) {
        if(!hitTarget) {
            for (Unit unit : controller.getCollisionList()) {
                if (unit == collidingUpdateable) {
                    unit.takeDamage(damage);
                }
            }
        }
    }
    */
    @Override
    //Optimoidumpi versio collidesta (?)
    public void collides(Updateable collidingUpdateable){
        destroyThis();
        ((Unit)collidingUpdateable).takeDamage(damage);
    }


    @Override
    public void update(double deltaTime) {

        double deltaTimeMultiplied = deltaTime * 2;

        if(color.getRed() > deltaTimeMultiplied) {
            color = new Color(color.getRed() - deltaTimeMultiplied,
                    1, color.getBlue() - deltaTimeMultiplied, color.getOpacity() - deltaTimeMultiplied);
            shape.setStroke(color);
        }
        /*if(i > deltaTimeMultiplied){
            i -= deltaTimeMultiplied;
        }*/
        else{
            destroyThis();
        }

    }

    /**
     * Rakentaa projectilen Polygonin
     *
     * @param color Projectilen väri
     * @return Rakennettu Polygon
     */
    private Polyline buildLaser(Color color) {
        // Ammuksen muoto
        shape = new Polyline();
        shape.getPoints().addAll(-0.0, 0.0,
                (double)WINDOW_WIDTH, 0.0);
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
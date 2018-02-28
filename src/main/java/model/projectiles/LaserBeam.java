package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Component;
import model.Unit;
import model.Updateable;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class LaserBeam extends BaseProjectile implements Updateable {

    private static final Color COLOR = Color.WHITE;
    private Controller controller;
    private int damage;
    private Polygon shape;
    private Color color = Color.WHITE;

    public LaserBeam(Controller controller, Unit shooter, double speed, int damage, Color color, Component component) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, component);

        this.controller = controller;
        this.damage = damage;

        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);

        shape = buildLaser(color);
        this.getChildren().add(shape);

    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this);
    }

    @Override
    public void collides(Updateable collidingUpdateable) {
        destroyThis();
        for (Unit unit : controller.getCollisionList()) {
            if (unit == collidingUpdateable) {
                unit.takeDamage(damage);
            }
        }
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
            controller.removeUpdateable(this);
        }

    }

    /**
     * Rakentaa projectilen Polygonin
     *
     * @param color Projectilen väri
     * @return Rakennettu Polygon
     */
    private Polygon buildLaser(Color color) {
        // Ammuksen muoto
        shape = new Polygon();
        shape.getPoints().addAll(-0.0, 0.0,
                2000.0, 0.0);
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
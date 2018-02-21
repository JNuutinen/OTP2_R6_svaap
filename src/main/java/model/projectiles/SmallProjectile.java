package model.projectiles;

import controller.Controller;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import model.Player;
import model.Unit;

public class SmallProjectile extends BaseProjectile {
    private static final Color COLOR = Color.CYAN;

    /**
     * Konstruktori projectilen vakiovärillä
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage);

        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);

        Polygon shape = buildProjectile(speed, COLOR);
        getChildren().add(shape);
    }

    /**
     * Konstruktori värin valinnalla
     * @param controller Pelin kontrolleri
     * @param shooter Unit, jonka aseesta projectile ammutaan
     * @param speed Projectilen nopeus
     * @param damage Projectilen vahinko
     * @param color Projectilen väri
     */
    public SmallProjectile(Controller controller, Unit shooter, double speed, int damage, Color color) {
        // Kutsutaan BaseProjectilen konstruktoria
        super(controller, shooter, speed, damage);

        // TODO: hitboxin koko kovakoodattu
        setHitbox(10);

        Polygon shape = buildProjectile(speed, color);
        getChildren().add(shape);
    }

    /**
     * Rakentaa projectilen Polygonin
     * @param speed Projectilen nopeus, vaikuttaa hännän pituuteen
     * @param color Projectilen väri
     * @return Rakennettu Polygon
     */
    private Polygon buildProjectile(double speed, Color color) {
        // Ammuksen muoto
        // TODO: Nyt vielä väriä lukuunottamatta sama kuin missilellä
        Polygon shape = new Polygon();
        shape.getPoints().addAll(-6.0, 0.0,
                0.0, -3.0,
                speed*0.6+1.0, 0.0, // ammuksen hanta skaalautuu nopeuden mukaan, mutta on ainakin 1.0
                0.0, 3.0);
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        shape.setEffect(blur);
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(color);
        shape.setStrokeWidth(5.0);
        shape.getTransforms().add(new Rotate(180, 0, 0));
        return shape;
    }
}

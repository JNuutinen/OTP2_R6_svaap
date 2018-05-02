package model.fx;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import model.SpriteImpl;
import model.Updateable;

/**
 * Teleporttausefekti Spriteille.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class TeleportEffect extends SpriteImpl implements Updateable {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Stopit värinmuutoksille.
     */
    private Stop[] colors;

    /**
     * Teleporttausefektin skaalauskerroin.
     */
    private double scaleMultiplier;

    /**
     * Teleporttausefektin ympyräefekti.
     */
    private Circle circle;

    /**
     * Teleporttausefektin ympyräefektin väri.
     */
    private Color circleColor;

    /**
     * Konstruktori luo efektin ja lisää sen Updateableihin. Lopussa kutsuu destroyThis() metodia,
     * jossa annetaan olla hetken "elossa" ja animoitua, jonka jälkeen se poistetaan.
     *
     * @param color    Efektin väri.
     * @param position Efektin sijainti.
     * @param unitSize Efekti skaalataan siihen liittyvän unitin koon mukaan.
     */
    public TeleportEffect(Color color, Point2D position, double unitSize) {

        // TODO: beitattu vaa explosionista, erilaine efekti tähän sit joskus

        controller = GameController.getInstance();
        setPosition(position.getX(), position.getY());
        scaleMultiplier = unitSize * 0.2;
        colors = new Stop[]{new Stop(0, color), new Stop(1, Color.TRANSPARENT)};

        circleColor = color;

        circle = new Circle(scaleMultiplier);
        circle.setFill(circleColor);
        circle.setStroke(Color.TRANSPARENT);
        circle.setStrokeWidth(20 * scaleMultiplier);

        Bloom bloom = new Bloom(20.0);
        GaussianBlur blur = new GaussianBlur(20.0);
        blur.setInput(bloom);
        circle.setEffect(blur);

        getChildren().addAll(circle);
        controller.addUpdateableAndSetToScene(this);
    }

    /**
     * Poistaa efektin pelistä.
     */
    private void destroyThis() {
        controller.removeUpdateable(this);
    }

    @Override
    public void update(double deltaTime) {
        double deltaTimeMultiplier = 3;
        // lisää läpinäkyvyyttä kunnes läpinäkyvyys on niin pieni että koko efektin voi poistaa.
        if (circleColor.getOpacity() > circleColor.getOpacity() * (1 - deltaTime * deltaTimeMultiplier) + 0.001 && deltaTime * deltaTimeMultiplier < 1) {
            circleColor = new Color(circleColor.getRed(), circleColor.getGreen(), circleColor.getBlue(), circleColor.getOpacity() * (1 - deltaTime * deltaTimeMultiplier));
            circle.setFill(circleColor);
            circle.setStroke(Color.TRANSPARENT);
            circle.setRadius(circle.getRadius() + (deltaTime * 700 * scaleMultiplier));
        } else {
            destroyThis();
        }
    }
}

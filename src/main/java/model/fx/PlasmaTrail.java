package model.fx;

import controller.Controller;
import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.SpriteImpl;
import model.Updateable;

public class PlasmaTrail extends SpriteImpl implements Updateable {

    /**
     * Ammuksen shape (viiva).
     */
    private Polyline shape = new Polyline();;

    /**
     * Laserin aloitusväri jota häivytetään.
     */
    private Color currentColor = Color.WHITE;

    private Controller controller;

    private Point2D targetLocation;

    public PlasmaTrail(){
        controller = GameController.getInstance();
        controller.addUpdateableAndSetToScene(this);
        getChildren().add(shape);
    }

    /**
     * Rakentaa projectilen Shapen
     * @param color Projectilen väri
     * @return Rakennettu PolyLine
     */
    private Polyline buildPlasma(Color color) {
        // Ammuksen muoto
        if(targetLocation != null) {
            System.out.println("hmmmm");
            shape.getPoints().addAll(-0.0, 0.0,
                    targetLocation.getX() + 10, targetLocation.getY() + 10);
        }
        Bloom bloom = new Bloom(0.0);
        GaussianBlur blur = new GaussianBlur(3.0);
        blur.setInput(bloom);
        //shape.setEffect(blur);
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(color);
        shape.setStrokeWidth(2.0); //7
        return shape;
    }

    @Override
    public void update(double deltaTime) {
        System.out.println("update");
        shape = buildPlasma(Color.WHITE);

    }

    public void setTargetLocation(Point2D targetLocation){
        this.targetLocation = targetLocation;
    }
}

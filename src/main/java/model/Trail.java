package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

public class Trail extends Sprite implements Updateable{

    private Updateable target;
    Polyline polyline = new Polyline();

    public Trail(Controller controller, Updateable target, Point2D startingPosition){
        this.target = target;
        this.setPosition(startingPosition.getX(), startingPosition.getY());
        polyline.setStrokeWidth(5.0);
        polyline.getPoints().addAll(0.0, 0.0, 20.0, 20.0, 500.0, 500.0);


        Stop[] stops = new Stop[] { new Stop(0, Color.GREEN), new Stop(1, Color.BLUE)};
        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        polyline.setStroke(lg);
        controller.addUpdateable(this);

    }



    public void destroyThis(){

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }
}

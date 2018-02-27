package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;


public class Trail extends Sprite implements Updateable{

    private Updateable target;
    private Polyline polyline = new Polyline();
    private List<Line> lines = new ArrayList<Line>();
    private List<Stop[]> colors = new ArrayList<Stop[]>();
    private Point2D newestTrailPoint = null;
    private Controller controller;

    public Trail(GraphicsContext gc, Controller controller, Updateable target){
        super(gc);
        this.controller = controller;
        this.target = target;
        this.setPosition(0, 0);

        newestTrailPoint = new Point2D(target.getPosition().getX(), target.getPosition().getY());

        /*
        polyline.setStrokeWidth(5.0);
        newestTrailPoint = new Point2D(target.getPosition().getX(), target.getPosition().getY());
        polyline.getPoints().addAll(newestTrailPoint.getX(), newestTrailPoint.getY());


        polyline.getPoints().addAll(newestTrailPoint.getX(), newestTrailPoint.getY());
        */

        Stop[] stops = new Stop[] { new Stop(0, Color.GREEN), new Stop(1, Color.BLUE)};
        this.colors.add(stops);

        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, this.colors.get(0));
        //polyline.setStroke(lg);

        lines.add(new Line(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getX(), target.getPosition().getY()));

        // TODO: trail graphicscontextilla
        //this.getChildren().addAll(lines);
        //controller.addUpdateable(this);

    }

    //TODO onks taa nyt hyva?
    public void destroyThis(){
        Trail toBeRemved = this;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        toBeRemved.destroyCompletely();
                    }
                });
            }
        });
        thread.start();
    }

    private void destroyCompletely(){
        controller.removeUpdateable(this);
    }

    @Override
    public void update(double deltaTime) {
        if(getDistanceFromBetweenPoints(newestTrailPoint, target.getPosition()) > 30.0){
            Line newLine = new Line(newestTrailPoint.getX(), newestTrailPoint.getY(), target.getPosition().getX(), target.getPosition().getY());
            newLine.setStrokeWidth(1);
            lines.add(newLine);

            Stop[] stops1 = new Stop[] { new Stop(0, Color.color(1, 1, 1)),
                    new Stop(1, Color.color(1, 1, 1))};
            this.colors.add(stops1);

            LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    colors.get(colors.size()-1));

            lines.get(lines.size()-1).setStroke(lg);

            newestTrailPoint = new Point2D(target.getPosition().getX(), target.getPosition().getY());

            // TODO: graphicscontextilla?
            //this.getChildren().addAll(lines.get(lines.size()-1));
        }

        for(Stop[] stops : colors){
            if(stops[0].getColor().getRed() > deltaTime) {
                stops[0] = new Stop(0, Color.color(stops[0].getColor().getRed() - deltaTime,
                        stops[0].getColor().getGreen() - deltaTime, 1, stops[0].getColor().getOpacity() - deltaTime));

                stops[1] = new Stop(0, Color.color(stops[1].getColor().getRed() - deltaTime,
                                stops[1].getColor().getGreen() - deltaTime, 1, stops[1].getColor().getOpacity() - deltaTime));
            }
        }

        int colorsIndex = 0;
        for(Line line : lines){
            LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    colors.get(colorsIndex));

            line.setStroke(lg);
            colorsIndex++;
        }

    }

    private double getDistanceFromBetweenPoints(Point2D source, Point2D target){
        return Math.sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }

    @Override
    public void collides(Updateable collidingUpdateable) {
    }
}

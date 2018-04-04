package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Häntä ammuksille.
 */
public class Trail extends Sprite implements Updateable{

    /**
     * Updateable, josta häntä lähtee.
     */
    private Updateable target;

    /**
     * Lista, jossa trailin muodostavat viivat.
     */
    private List<Line> lines;

    /**
     * Trailin värien muutokset.
     */
    private List<Stop[]> colors;

    /**
     * Uusin piste trailille.
     */
    private Point2D newestTrailPoint;

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Konstruktori, luo trailin.
     * @param controller Pelin kontrolleri.
     * @param target Updateable, josta trail lähtee.
     */
    public Trail(Controller controller, Updateable target){
        this.controller = controller;
        this.target = target;
        this.setPosition(0, 0);

        newestTrailPoint = new Point2D(target.getPosition().getX(), target.getPosition().getY());

        Stop[] stops = new Stop[] { new Stop(0, Color.GREEN), new Stop(1, Color.BLUE)};
        colors = new ArrayList<Stop[]>();
        this.colors.add(stops);

        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, this.colors.get(0));
        //polyline.setStroke(lg);

        lines = new ArrayList<Line>();
        lines.add(new Line(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getX(), target.getPosition().getY()));

        this.getChildren().addAll(lines);
        controller.addUpdateableAndSetToScene(this);

    }

    @Override
    public void destroyThis(){
        Trail toBeRemved = this;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> toBeRemved.destroyCompletely());
        });
        thread.start();
    }

    /**
     * Poistaa trailin.
     */
    private void destroyCompletely(){
        controller.removeUpdateable(this);
    }

    /**
     * vähentää joka päivityksessä väri- ja läpinäkyvyysarvon jokaisesta viivasta joka trailissa on.
     * Poistaa viivat hännästä joiden läpinäkyvyysarvo tippuu alle vähennettävän arvon.
     *
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
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

            this.getChildren().addAll(lines.get(lines.size()-1));
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

    /**
     * Palauttaa kahden pisteen etäisyyden toisistaan.
     * @param source Piste1.
     * @param target Piste2.
     * @return Pisteiden etäisyys toisistaan.
     */
    private double getDistanceFromBetweenPoints(Point2D source, Point2D target){
        return Math.sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }

    @Override
    public void collides(Updateable collidingUpdateable) {
    }
}

package model;

import controller.Controller;
import controller.GameController;
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
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class Trail extends Sprite implements Updateable{

    /**
     * Updateable, josta häntä lähtee.
     */
    private HitboxCircle target;

    /**
     * Lista, jossa trailin muodostavat viivat.
     */
    private List<Line> lines;

    /**
     * Trailin värien muutokset.
     */
    private List<Stop[]> colors;

    /**
     * nykyisestä väristä vähennettävä punainen arvo häivytyksen aikana.
     */
    private double redSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä vihreä arvo häivytyksen aikana.
     */
    private double greenSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä sininen arvo häivytyksen aikana.
     */
    private double blueSubtraction = 0;

    /**
     * nykyisestä väristä vähennettävä läpinäkyvyysarvo häivytyksen aikana.
     */
    private double opacitySubtraction = 1;

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
     * @param target Updateable, josta trail lähtee.
     */
    public Trail(HitboxCircle target, Color color){
        controller = GameController.getInstance();
        this.target = target;
        setPosition(0, 0);

        redSubtraction = (1 - color.getRed());
        greenSubtraction = (1 - color.getGreen());
        blueSubtraction = (1 - color.getBlue());
        opacitySubtraction = opacitySubtraction * 0.5;

        newestTrailPoint = new Point2D(target.getPosition().getX(), target.getPosition().getY());

        Stop[] stops = new Stop[] { new Stop(0, Color.GREEN), new Stop(1, Color.BLUE)};
        colors = new ArrayList<Stop[]>();
        colors.add(stops);

        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, this.colors.get(0));
        //polyline.setStroke(lg);

        lines = new ArrayList<Line>();
        lines.add(new Line(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getX(), target.getPosition().getY()));

        Platform.runLater(()->this.getChildren().addAll(lines));
        controller.addUpdateableAndSetToScene(this);

    }

    public void destroyThis(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroyCompletely();
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
        if(getDistanceFromBetweenPoints(newestTrailPoint, target.getPosition()) > 20){
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

        double deltaTimeMultiplied = deltaTime * 3;




        for(Stop[] stops : colors){
            if(stops[0].getColor().getOpacity() > opacitySubtraction * deltaTimeMultiplied) {

                Color color1 = stops[0].getColor();
                Color color2 = stops[0].getColor();

                double newRedValue = 0;
                double newGreenValue = 0;
                double newBlueValue = 0;
                double newOpacity = 0;

                if(color1.getRed() > (redSubtraction * deltaTimeMultiplied)){
                    newRedValue = color1.getRed() - (redSubtraction * deltaTimeMultiplied);
                }
                if(color1.getGreen() > (greenSubtraction * deltaTimeMultiplied)){
                    newGreenValue = color1.getGreen() - (greenSubtraction * deltaTimeMultiplied);
                }
                if(color1.getBlue() > (blueSubtraction * deltaTimeMultiplied)){
                    newBlueValue = color1.getBlue() - (blueSubtraction * deltaTimeMultiplied);
                }
                if(color1.getOpacity() > (opacitySubtraction * deltaTimeMultiplied)){
                    newOpacity = color1.getOpacity() - (opacitySubtraction * deltaTimeMultiplied);
                }
                color1 = new Color(newRedValue, newGreenValue, newBlueValue, newOpacity);
                stops[0] = new Stop(0, color1);

                // --

                if(color2.getRed() > (redSubtraction * deltaTimeMultiplied)){
                    newRedValue = color2.getRed() - (redSubtraction * deltaTimeMultiplied);
                }
                if(color2.getGreen() > (greenSubtraction * deltaTimeMultiplied)){
                    newGreenValue = color2.getGreen() - (greenSubtraction * deltaTimeMultiplied);
                }
                if(color2.getBlue() > (blueSubtraction * deltaTimeMultiplied)){
                    newBlueValue = color2.getBlue() - (blueSubtraction * deltaTimeMultiplied);
                }
                if(color2.getOpacity() > (opacitySubtraction * deltaTimeMultiplied)){
                    newOpacity = color2.getOpacity() - (opacitySubtraction * deltaTimeMultiplied);
                }
                color2 = new Color(newRedValue, newGreenValue, newBlueValue, newOpacity);
                stops[1] = new Stop(0, color2);
                /*
                stops[1] = new Stop(0, Color.color(stops[1].getColor().getRed() - deltaTime,
                                stops[1].getColor().getGreen() - deltaTime, 1, stops[1].getColor().getOpacity() - deltaTime));*/
            }
            else{
                stops[0] = new Stop(0, Color.TRANSPARENT);
                stops[1] = new Stop(0, Color.TRANSPARENT);
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

}

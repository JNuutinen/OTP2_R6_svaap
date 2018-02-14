package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class Component extends Sprite {
    double width = 60;
    double height = 60;
    Point2D startingPosition;
    private double xVelocity;
    private double yVelocity;
    public String imagePath;


    //UnitX & UnitY: Spawnataan komponentti samaan paikkaan isännänsä kanssa
    public Component(Shape shape) {
        setShape(shape);
    }

    public void addVelocity(double x, double y) {
        xVelocity += x;
        yVelocity += y;
    }

    public void resetVelocity() {
        xVelocity = 0;
        yVelocity = 0;
    }





}

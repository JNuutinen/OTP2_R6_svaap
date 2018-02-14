package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class Component extends Sprite {
    Point2D startingPosition;
    private double xVelocity;
    private double yVelocity;


    //UnitX & UnitY: Spawnataan komponentti samaan paikkaan isännänsä kanssa

    /**
     * @param shape parametreina "triangle", "rectangle" ja "circle"
     */
    public Component(String shape, int size, int orientation, Color color) {
        if (shape.equals("triangle")) {
            setShape(triangle(size, orientation, color));
        } else if (shape.equals("rectangle")) {
            setShape(rectangle(size, orientation, color));
        } else if (shape.equals("circle")) {
            setShape(circle(size, color));
        }
    }

    public Shape triangle(int size, int orientation, Color color) {
        double tip = 6 * size;
        double point = 3 * size;
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                0.0, 0.0,
                tip, point / 2,
                0.0, point});
        triangle.setFill(Color.BLACK);
        triangle.setStroke(color);
        triangle.setStrokeWidth(2.0);
        triangle.getTransforms().add(new Rotate(90 * orientation, 50, 30));
        return triangle;
    }

    public Shape rectangle(int size, int orientation, Color color) {
        double x = 5 * size;
        double y = 3 * size;
        Rectangle rectangle = new Rectangle(x, y);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(color);
        rectangle.setStrokeWidth(2.0);
        rectangle.getTransforms().add(new Rotate(90 * orientation, 50, 30));
        return rectangle;
    }

    public Shape circle(int size, Color color) {
        Circle circle = new Circle(3 * size);
        circle.setFill(Color.BLACK);
        circle.setStroke(color);
        circle.setStrokeWidth(2.0);
        return circle;
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

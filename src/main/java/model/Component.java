package model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class Component extends Sprite {
    Point2D startingPosition;
    private double xVelocity;
    private double yVelocity;

    private double xOffset;
    private double yOffset;

    private double projectileFrontOffset = 0;
    private double projectileLeftOffset = 0;


    //UnitX & UnitY: Spawnataan komponentti samaan paikkaan isännänsä kanssa

    /**
     *
     * @param shape parametreina "triangle", "rectangle" ja "circle"
     * @param size
     * @param orientation
     * @param color
     * @param xOffset
     * @param yOffset
     */
    public Component(String shape, int size, int orientation, Color color, double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        if (shape.equals("triangle")) {
            setShape(triangle(size, orientation, color));
        } else if (shape.equals("rectangle")) {
            setShape(rectangle(size, orientation, color));
        } else if (shape.equals("circle")) {
            setShape(circle(size, color));
        }
    }

    /**
     *
     * @param shape parametreina "triangle", "rectangle" ja "circle"
     * @param size
     * @param orientation
     * @param color komponentin väri
     * @param xOffset
     * @param yOffset
     * @param projectileFrontOffset ammuksen aloituspaikan poikkeama aluksen etusuuntaan
     * @param projectileLeftOffset ammuksen aloituspaikan poikkeama aluksen vasempaan suuntaan
     */
    public Component(String shape, int size, int orientation, Color color, double xOffset, double yOffset,
                     double projectileFrontOffset, double projectileLeftOffset) {
        this(shape, size, orientation, color, xOffset, yOffset);
        this.projectileFrontOffset = projectileFrontOffset;
        this.projectileLeftOffset = projectileLeftOffset;
    }

    public Shape triangle(int size, int orientation, Color color) {
        double tip = 6 * size * 1.3;
        double point = 3 * size * 1.3;
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                tip/2 * -1, point / 2,
                tip/2, 0.0,
                tip/2 * -1, point / 2 * -1);
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
        return (Shape)rectangle;
    }

    public Shape circle(int size, Color color) {
        Circle circle = new Circle(3 * size);
        circle.setFill(Color.BLACK);
        circle.setStroke(color);
        circle.setStrokeWidth(2.0);
        return (Shape)circle;
    }

    public void addVelocity(double x, double y) {
        xVelocity += x;
        yVelocity += y;
    }

    public void resetVelocity() {
        xVelocity = 0;
        yVelocity = 0;
    }

    public double getxOffset(){
        return xOffset;
    }

    public double getyOffset(){
        return yOffset;
    }

    public double getProjectileFrontOffset() { return projectileFrontOffset;}

    public double getProjectileLeftOffset() { return projectileLeftOffset;}



}

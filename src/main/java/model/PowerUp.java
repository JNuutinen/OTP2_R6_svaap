package model;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_BLUEPeer;
import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import javax.xml.bind.annotation.XmlType;

import static view.GameMain.ENEMY_PROJECTILE_TAG;
import static view.GameMain.PLAYER_SHIP_TAG;

public class PowerUp extends Sprite implements Updateable{
    int value;
    int type;
    static final int HP = 0, DAMAGE = 1, SPEED = 2, SCORE = 3;
    int size = 5;
    Shape shape;
    Controller controller;

    public PowerUp(Controller controller, Unit deadUnit,  int powerUpType, int value){
        switch (powerUpType) {
            case HP:
                type = HP;
                shape = circle(size, Color.GREEN);
                break;
            case DAMAGE:
                type = DAMAGE;
                shape = triangle(size, 3, Color.PURPLE);
                break;
            case SPEED:
                type = SPEED;
                shape = triangle(size, 0, Color.BLUE);
                break;
            case SCORE:
                type = SCORE;
                shape = circle(size, Color.YELLOW);
                break;
            default:
                return;
        }
        this.controller = controller;
        setTag(ENEMY_PROJECTILE_TAG); //ENEMY_PROJECTILE_TAG collisionia varten. Toimii toistaseksi ihan hyvin!
        controller.addUpdateable(this);
        this.value = value;
        type = powerUpType;
        double xOffset = degreesToVector(deadUnit.getDirection()).getX();
        double yOffset = degreesToVector(deadUnit.getDirection()).getY();
        Point2D startingLocation = new Point2D(deadUnit.getPosition().getX() + xOffset, deadUnit.getPosition().getY() + yOffset);
        this.setPosition(startingLocation.getX(), startingLocation.getY());

        getChildren().add(shape);
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

    public void givePowerUp(Player player) {
        switch (type) {
            case HP:
                player.addHP(value);
                System.out.println("hp++");
                break;
            case DAMAGE:
                //TODO player.getPrimaryWeapon().setDamage(value); tms
                System.out.println("QUAD DAMAGE ACTIVATED");
                break;
            case SPEED:
                //TODO tää
                System.out.println("FAST FAST FAST");
                break;
            case SCORE:
                player.addScore(value);
                System.out.println("Score++");
                break;
        }
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void collides(Updateable collidingUpdateable){
        givePowerUp((Player)collidingUpdateable);
        destroyThis();
    }

    @Override
    public void destroyThis() {
        controller.removeUpdateable(this);
    }
}

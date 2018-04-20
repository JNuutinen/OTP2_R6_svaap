package model;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 * Perusluokka kaikille spriteille.
 *
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class SpriteImpl extends Pane implements Sprite {

    /**
     * Spriten suunta (kulma).
     */
    private double direction;

    /**
     * Spriten nopeus.
     */
    private double velocity = 200;

    /**
     * Ympyrähitboxin halkaisija.
     */
    private double hitboxRadius = 0;

    /**
     * Spriten kuvan säiliö.
     */
    private ImageView imageView = new ImageView();

    /**
     * Kertoo onko spriten tarkoitus liikkua tällä hetkellä.
     */
    private boolean isMoving = false;

    /**
     * Spriten muoto.
     */
    private Shape shape;

    /**
     * Debuggaustyökalujen toggle.
     */
    private boolean debuggerToolsEnabled = true;

    /**
     * Toggle lockedDirectionille.
     */
    private boolean movingDirectionLocked = false;

    /**
     * Voi laittaa spriten menee tiettyyn suuntaan vaikka sita kaantelisi samalla ks. lockDirection().
     */
    private double lockedDirection = 0;

    /**
     * Spriten tunnistetagi.
     */
    private Tag tag = Tag.UNDEFINED;

    @Override
    public double getHitboxRadius() {
        return hitboxRadius;
    }

    @Override
    public Point2D getPosition() {
        return new Point2D(this.getLayoutX(), this.getLayoutY());
    }

    @Override
    public Tag getTag() {
        return tag;
    }

    @Override
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public void setImage(Image newImage, double width, double height) {
        imageView.setImage(newImage);
        imageView.setX(-width / 2);
        imageView.setY(-height / 2);
        this.getChildren().add(imageView);
    }

    @Override
    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    @Override
    public void setPosition(double newX, double newY) {
        this.setLayoutX(newX);
        this.setLayoutY(newY);
    }

    @Override
    public double getXPosition() {
        return this.getLayoutX();
    }

    @Override
    public double getYPosition() {
        return this.getLayoutY();
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public void setHitbox(double circleHitboxDiameter) {
        hitboxRadius = circleHitboxDiameter / 2;
        shape = new Circle(0, 0, circleHitboxDiameter / 2);
        shape.setFill(Color.TRANSPARENT);
        if (debuggerToolsEnabled) {
            shape.setStroke(Color.LIGHTGREY);
        }
        shape.setStrokeWidth(0.4);
        Platform.runLater(() -> this.getChildren().add(shape));
    }

    @Override
    public void setSize(Point2D newSize) {//TODO ei kaytos atm
        resize(newSize.getX(), newSize.getY());
    }

    /**
     * Muuttaa astekulman vektoriksi.
     *
     * @param degrees Kulma, joka pitää muuntaa.
     * @return Kulma vektorina esitettynä (Point2D).
     */
    public Point2D degreesToVector(double degrees) {
        double sin = Math.sin(Math.toRadians(degrees) * -1);
        double cos = Math.cos(Math.toRadians(degrees));
        return new Point2D(cos, sin);
    }

    @Override
    public double getAngleFromTarget(Point2D target) {
        return Math.toDegrees(Math.atan2(getYPosition() - target.getY(), getXPosition() * -1 - target.getX() * -1));
    }

    @Override
    public double getDistanceFromTarget(Point2D target) {
        return Math.sqrt(Math.pow(target.getX() - this.getXPosition(), 2) + Math.pow(target.getY() - this.getYPosition(), 2));
    }

    @Override
    public void lockDirection(double angle) {
        movingDirectionLocked = true;
        lockedDirection = angle;
    }

    @Override
    public void moveStep(double deltaTime) {
        if (isMoving) {
            if (movingDirectionLocked) {
                Point2D directionInVector = degreesToVector(lockedDirection);
                Point2D currentPosition = getPosition();
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            } else {
                Point2D directionInVector = degreesToVector(direction);
                Point2D currentPosition = getPosition();
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
        }
    }

    @Override
    public void rotate(double degrees) {
        this.getTransforms().add(new Rotate(degrees * -1, Rotate.Z_AXIS));
        this.direction += degrees;
        while (direction >= 180.0) {
            direction -= 360.0;
        }
        while (direction < -180) {
            direction += 360.0;
        }
    }
}

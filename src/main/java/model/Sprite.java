package model;

import controller.Controller;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/*
    Ylaluokka kaytetaan mm. aluksille ja ammuksille.
    unitin ylaluokka.
 */
public class Sprite extends Pane {
    private Controller controller;
    private double direction;
    private double velocity = 200;
    private ImageView imageView = new ImageView();
    private boolean isMoving = false;
    private Shape shape;
    private boolean debuggerToolsEnabled = true;
    //nailla voi laittaa spriten menee tiettyyn suuntaan vaikka sita kaantelisi samalla ks. lockDirection()
    private boolean movingDirectionLocked = false;
    private double lockedDirection = 0;

    private String tag = "undefined";


    public Sprite() {

    }

    /**
     * konstruktori ympyrahitboxilla
     * @param circleHitboxDiameter eli halkaisija
     */
    public void setHitbox(double circleHitboxDiameter){
        shape = new Circle(0, 0, circleHitboxDiameter/2);
        shape.setFill(Color.TRANSPARENT);
        if(debuggerToolsEnabled) {
            shape.setStroke(Color.LIGHTGREY);
        }
        shape.setStrokeWidth(0.4);
        this.getChildren().add(shape);
    }

    /**
     *  konstruktori neliohitboxilla
     * @param rectangleHitboxHeight
     * @param rectangleHitboxWidth
     */
    public void setHitbox(double rectangleHitboxHeight, double rectangleHitboxWidth){
        shape = new Rectangle(0 - (rectangleHitboxWidth/2), 0 - (rectangleHitboxHeight/2), rectangleHitboxWidth, rectangleHitboxHeight);
        shape.setFill(Color.TRANSPARENT);
        if(debuggerToolsEnabled) {
            shape.setStroke(Color.LIGHTGREY);
        }
        shape.setStrokeWidth(0.4);
        this.getChildren().add(shape);
    }

    //kutsutut metodit löytyy Pane-ylaluokasta
    public void setPosition(double newX, double newY){
        this.setLayoutX(newX);
        this.setLayoutY(newY);
    }

    // 0=oikealle, 90 on ylos...
    public void rotate(double degrees){
        this.getTransforms().add(new Rotate(degrees * -1, Rotate.Z_AXIS));
        this.direction += degrees;
        while(direction >= 180.0){
            direction -= 360.0;
        }
        while(direction < -180){
            direction += 360.0;
        }
    }

    public void setIsMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

    public Point2D getPosition(){
        return new Point2D(this.getLayoutX(), this.getLayoutY());
    }

    public double getXPosition(){
        return this.getLayoutX();
    }

    public double getYPosition(){
        return this.getLayoutY();
    }

    public void setVelocity(double velocity){
        this.velocity = velocity;
    }

    public double getVelocity(){
        return velocity;
    }

    public void setSize(Point2D newSize){//TODO ei kaytos atm
        this.resize(newSize.getX(), newSize.getY());
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getDirection(){
        return direction;
    }

    public String getTag(){
        return this.tag;
    }

    //liikkuu yhden askeleen direction-suuntaan kerrottuuna velocity-muuttujalla.
    //kaytetään peliloopin yhteydessa
    public void moveStep(double deltaTime) {
        if (isMoving) {
            if(movingDirectionLocked) {
                Point2D directionInVector = degreesToVector(lockedDirection);
                Point2D currentPosition = getPosition();
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
            else{
                Point2D directionInVector = degreesToVector(direction);
                Point2D currentPosition = getPosition();
                this.setPosition(currentPosition.getX() + (directionInVector.getX() * velocity * deltaTime),
                        currentPosition.getY() + (directionInVector.getY() * velocity * deltaTime));
            }
        }
    }

    public void moveBoss(double deltaTime){
        if (isMoving) {
            Point2D directionInVector = degreesToVector(direction);
            Point2D currentPosition = getPosition();
            this.setPosition(currentPosition.getX(),
                    currentPosition.getY() + (directionInVector.getY()));
        }
    }

    /**
     * lukitsee suunnan niin etta jatkaa tiettyyn suuntaan menemista vaikka spritea kaantelisi samalla.
     * @param angle lukittu suunta.
     */
    public void lockDirection(double angle){
        movingDirectionLocked = true;
        lockedDirection = angle;
    }

    public Point2D degreesToVector(double degrees){
        double sin = Math.sin(Math.toRadians(degrees) * -1);
        double cos = Math.cos(Math.toRadians(degrees));
        return new Point2D(cos, sin);
    }

    public void setImage(Image newImage){
        imageView.setImage(newImage);
        this.getChildren().add(imageView);
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImages(Image newImage) {
        ImageView imageView = new ImageView();
        imageView.setImage(newImage);
        this.getChildren().add(imageView);
    }


    public Shape getHitboxShape(){
        return shape;
    }

    // laskee kulman itsensa ja kohteen valilla yksikkoympyran mukaisesti (esim. jos kohde suoraan ylapuolella, kulma on 90)
    public double getAngleFromTarget(Point2D target){
        return Math.toDegrees(Math.atan2(getYPosition() - target.getY(), getXPosition() * -1 - target.getX() * -1));
    }

    // laskee itsensa ja kohteen valisen etaisyyden
    public double getDistanceFromTarget(Point2D target){
        return Math.sqrt(Math.pow(target.getX() - this.getXPosition(), 2) + Math.pow(target.getY() - this.getYPosition(), 2));
    }
}

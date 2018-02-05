package model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.geom.Ellipse2D;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

/*
    Ylaluokka kaytetaan mm. aluksille ja ammuksille.
    unitin ylaluokka.
 */
public class Sprite extends Pane {
    protected Point2D direction;
    private double velocity = 200;
    private ImageView imageView = new ImageView();
    private boolean isMoving = false;

    private Shape shape;
    private int hitboxShapeType;
    private final int rectangle = 1;
    private final int circle = 2;
    private double hitboxShapeMultiplier = 1;

    private String tag = "undefined";


    /*  konstruktori.
        @param int hitboxShapeType: kertoo hitboxin muodon.
        @param float hitboxShapeMultiplier: kertoo hitboxin suuruuden kertomalla hitboxin leveyden ja pituuden parametrin arvolla. (0.0-1.0)

     */
    public Sprite(int hitboxShapeType, float hitboxShapeMultiplier){
        this.hitboxShapeType = hitboxShapeType;
        this.hitboxShapeMultiplier = hitboxShapeMultiplier;
    }


    public Sprite(){
        this(1, 1);
        direction = degreesToDirection(0);
    }

    //kutsutut metodit löytyy Pane-ylaluokasta
    public void setPosition(double newX, double newY){
        this.setLayoutX(newX);
        this.setLayoutY(newY);
    }

    // 0=oikealle, 90 on ylös...
    public void setDirection(double degrees){
        this.direction = degreesToDirection(degrees);
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

    public String getTag(){
        return this.tag;
    }

    //liikkuu yhden askeleen direction-suuntaan kerrottuuna velocity-muuttujalla.
    //kaytetään peliloopin yhteydessa
    public void moveStep(double deltaTime) {
        if (isMoving) {
            Point2D currentPosition = getPosition();
            this.setPosition(currentPosition.getX() + (direction.getX() * velocity * deltaTime),
                    currentPosition.getY() + (direction.getY() * velocity * deltaTime));
        }
    }

    public Point2D degreesToDirection(double degrees){
        double sin = Math.sin(Math.toRadians(degrees) * -1);
        double cos = Math.cos(Math.toRadians(degrees));
        return new Point2D(cos, sin);
    }

    public void setImage(Image newImage){
        imageView.setImage(newImage);
        this.getChildren().add(imageView);
    }

    public void printSize(){//TODO poista lopuks
        System.out.println(this.getLayoutBounds().getWidth() + ", " + this.getLayoutBounds().getHeight());
    }


    public Shape getSpriteShape(){
        switch(hitboxShapeType){
            case rectangle:
                return new Rectangle(getXPosition(), getYPosition(),
                        getWidth() * hitboxShapeMultiplier, getHeight() * hitboxShapeMultiplier);
            case circle:
                return (Shape) new Circle(getXPosition() - getWidth()/2, getYPosition() - getHeight()/2,
                        getWidth() * hitboxShapeMultiplier);
            default:
                return null;
        }
    }
}

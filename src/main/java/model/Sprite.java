package model;



import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/*
    Ylaluokka kaytetaan mm. aluksille ja ammuksille.
    unitin ylaluokka.

 */
public class Sprite extends Pane {
    protected Point2D direction;
    private double velocity = 1;
    private ImageView imageView = new ImageView();
    private boolean isMoving = false;

    public Sprite(){
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

    //liikkuu yhden askeleen direction-suuntaan kerrottuuna velocity-muuttujalla.
    //kaytetään peliloopin yhteydessa
    public void moveStep() {
        if (isMoving) {
            Point2D currentPosition = getPosition();
            this.setPosition(currentPosition.getX() + direction.getX() * velocity, currentPosition.getY() + direction.getY() * velocity);
        }
    }

    public Point2D degreesToDirection(double degrees){
        double sin = Math.sin(Math.toRadians(degrees) * -1);
        double cos = Math.cos(Math.toRadians(degrees));
        return new Point2D(cos, sin);
    }


    public void setImage(Image newImage){
        Image image = newImage;
        imageView.setImage(image);
        this.getChildren().add(imageView);
    }

    public void printSize(){//TODO poista lopuks
        System.out.println(this.getLayoutBounds().getWidth() + ", " + this.getLayoutBounds().getHeight());
    }

}

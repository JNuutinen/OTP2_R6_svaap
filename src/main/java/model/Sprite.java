package model;



import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/*
    Ylaluokka kaytetaan mm. aluksille ja ammuksille.
    unitin ylaluokka.
 */
public class Sprite extends Pane {
    protected Point2D direction;
    private double velocity = 200;
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

    /**
     * Tarkistaa, törmääkö spriteBackup jonkin muun spriten kanssa.
     * @param sprite Sprite, jonka kanssa törmäys tarkistetaan
     * @return True jos törmäys, muuten false
     */

    public boolean collides(Sprite sprite) {
        return sprite.getBoundary().intersects(this.getBoundary());
    }

    /**
     * Palauttaa spriten rajat ja sijainnin.
     * @return Rectangle2D mallinnuksen spritestä
     */
    private Rectangle2D getBoundary() {
        return new Rectangle2D(getXPosition(), getYPosition(), getWidth(), getHeight());
    }
}
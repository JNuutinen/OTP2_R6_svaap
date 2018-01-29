package view;

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
    double velocity = 0;
    ImageView imageView = new ImageView();

    //kutsutut metodit löytyy Pane-ylaluokasta
    public void setPosition(double newX, double newY){
        this.setLayoutX(newX);
        this.setLayoutY(newY);
    }

    public Point2D getPosition(){
        return new Point2D(this.getLayoutX(), this.getLayoutY());
    }

    public void setVelocity(double velocity){
        this.velocity = velocity;
    }

    //liikkuu yhden askeleen direction-suuntaan kerrottuuna velocity-muuttujalla.
    //kaytetään peliloopin yhteydessa
    public void moveStep(){
        Point2D currentPosition = getPosition();
        this.setPosition(currentPosition.getX() + direction.getX() * velocity, currentPosition.getY() + direction.getY() * velocity);
    }

    //esim. image = new Image(getClass().getResourceAsStream("/kuv/img/spaceship.png"));
    public void setImage(Image newImage){
        Image image = newImage;
        imageView.setImage(image);
        this.getChildren().add(imageView);
    }

}


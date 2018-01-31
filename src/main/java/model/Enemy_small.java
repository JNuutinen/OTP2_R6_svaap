package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Enemy_small extends Unit {
    //alkuarvot, ellei alkuarvoja tuoda konstruktorissa
    private final Point2D startingPosition = new Point2D(200, 100);
    private final double speed = 3;
    private final double width = 64;
    private final double height = 64;
    private final double direction = 180;

    //konstruktori, ilman alkuarvoja
    public Enemy_small(){
        this.setImage(new Image(getClass().getResourceAsStream("/images/spaceship_small_cyan_placeholder.png"), width, height, true, true));
        this.setPosition(this.startingPosition.getX(), this.startingPosition.getY());
        this.setVelocity(this.speed);
        this.setDirection(this.direction);
    }

    //konstruktori, ilman alkuarvoja paitsi kuva ja sen suuruus
    public Enemy_small(Point2D startingPosition, double speed, double direction){
        this.setImage(new Image(getClass().getResourceAsStream("/images/spaceship_small_cyan_placeholder.png"), width, height, true, true));
        this.setPosition(startingPosition.getX(), startingPosition.getY());
        this.setVelocity(speed);
        this.setDirection(direction);
    }

    //konstruktori, ilman alkuarvoja
    public Enemy_small(Image image, Point2D startingPosition, double speed, double direction){
        this.setImage(image);
        this.setPosition(startingPosition.getX(), startingPosition.getY());
        this.setVelocity(speed);
        this.setDirection(direction);
    }


}

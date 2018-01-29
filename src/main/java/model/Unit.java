package model;

import javafx.scene.image.Image;
import view.Sprite;

/*
    Toimii yksittäisten alusten (pelaaja, vihut) ylaluokkana.

    Point2D getPosition()
    double getXPosition()
    double getYPosition()
    void setVelocity(double velocity)
    double getVelocity()
    void setImage(Image newImage)

 */
public class Unit extends Sprite {
    private int hp;
    //private Component components[];
    private int level;

    public Unit () {

    }

    public void shootPrimary() {

    }

    public void shootSecondary() {

    }

    public void luoTesteriAlus(){ //TODO Hege poistaa tan joskus.
        this.setImage(new Image(getClass().getResourceAsStream("/images/spaceship_small_cyan_placeholder.png")));
        this.setPosition(200, 200);
        this.setVelocity(3);

        /* tää generoi sellasen suorakaiteen

        Rectangle playerSprite = new Rectangle(this.getXPosition(), this.getYPosition(), 20, 10);
        playerSprite.setFill(Color.CYAN);
        this.getChildren().addAll(playerSprite);
        */

    }



    public void move(int direction) {
        switch(direction) {
            case 0: //ylös
                this.setPosition(this.getXPosition(), this.getYPosition() - this.getVelocity());
                //yPosition -= speed;
                break ;
            case 1: //oikealle, ylä-viistoon
                break ;
            case 2: //oikealle
                this.setPosition(this.getXPosition() + this.getVelocity(), this.getYPosition());
                break;
            case 3: //oikealle, ala-viisto
                break;
            case 4: //alas
                this.setPosition(this.getXPosition(), this.getYPosition() + this.getVelocity());
                break;
            case 5: //vasen, ala-viisto
                break;
            case 6: //vasen
                this.setPosition(this.getXPosition() - this.getVelocity(), this.getYPosition());
                break;
            case 7: //vasen, ylä-viisto
                break;
        }
    }


    public void takeDamage(int damage) {
        hp =- damage;
    }

}

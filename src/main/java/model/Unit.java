package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import view.Sprite;

/*
    Toimii yksittäisten alusten (pelaaja, vihut) ylaluokkana.

        yläluokan metodeja:
    Point2D getPosition()
    double getXPosition()
    double getYPosition()
    void setVelocity(double velocity)
    double getVelocity()
    void setImage(Image newImage)
    void setSize(Point2D newSize)
    printSize()

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




    public void takeDamage(int damage) {
        hp =- damage;
    }

}

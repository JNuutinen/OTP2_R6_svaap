package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

/**
 * Pelin liikkuva tausta. Sprite ei itsessään liiku, vaan se liikuttelee kuvia itsensä sisällä.
 */
public class GameBackground extends Sprite implements Updateable  {

    /**
     * Taustan vierimisnopeus.
     */
    private double scrollSpeed = 180;

    /**
     * Kuva1
     */
    private ImageView centerImage;

    /**
     * Seuraava kuva
     */
    private ImageView nextHorizontalImage;

    private ImageView nextVerticalImage;

    private ImageView nextDiagonalImage;

    /**
     * Konstruktori, luo kuvat ja lisää ne tämän Spriten Paneen.
     * @param controller Pelin kontrolleri.
     */
    public GameBackground(Controller controller) {


        controller.addUpdateable(this);

        String imagePath = "images/darkSpace.jpg";

        centerImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));

        nextHorizontalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));


        nextVerticalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));

        nextDiagonalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));

        nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());
        nextVerticalImage.setY(centerImage.getY() + centerImage.getImage().getHeight());

        nextDiagonalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());
        nextDiagonalImage.setY(centerImage.getY() + centerImage.getImage().getHeight());

        this.getChildren().add(centerImage);
        this.getChildren().add(nextHorizontalImage);
        this.getChildren().add(nextVerticalImage);
        this.getChildren().add(nextDiagonalImage);
    }


    @Override
    public void update(double deltaTime) {
        if(deltaTime < 100) { // fiksaa oudon bugin tason alussa
            centerImage.setX(centerImage.getX() - (scrollSpeed * deltaTime));
            centerImage.setY(centerImage.getY() + (yVelocity * deltaTime));

            nextHorizontalImage.setX(nextHorizontalImage.getX() - (scrollSpeed * deltaTime));
            nextHorizontalImage.setY(nextHorizontalImage.getY() + (yVelocity * deltaTime));

            nextVerticalImage.setX(nextVerticalImage.getX() - (scrollSpeed * deltaTime));
            nextVerticalImage.setY(nextVerticalImage.getY() + (yVelocity * deltaTime));

            nextDiagonalImage.setX(nextDiagonalImage.getX() - (scrollSpeed * deltaTime));
            nextDiagonalImage.setY(nextDiagonalImage.getY() + (yVelocity * deltaTime));


        }
        //Kuvien vaihto sivuun mentäessä
        if (centerImage.getImage().getWidth() + centerImage.getX() <= 0) {
            ImageView center = centerImage;
            centerImage = nextHorizontalImage;
            nextHorizontalImage = center;
            nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());

            ImageView verticalImage = nextVerticalImage;
            nextVerticalImage = nextDiagonalImage;
            nextDiagonalImage = verticalImage;
            nextDiagonalImage.setY(nextVerticalImage.getY());
            nextDiagonalImage.setX(nextHorizontalImage.getX());
        }

            if (centerImage.getY() < 0) { //Jos kuvat liikkuu ylöspäin
                if (nextVerticalImage.getY() < centerImage.getY()) { //tarkistetaan onko muut kuvat keskimmäisen alapuolella
                    //Vaihdetaan kuvat keskimmäisen yläpuolelle
                    nextVerticalImage.setY(centerImage.getImage().getHeight());
                    nextVerticalImage.setX(centerImage.getX());
                    nextDiagonalImage.setY(nextVerticalImage.getY());
                    nextDiagonalImage.setX(nextHorizontalImage.getX());

                } else if (centerImage.getImage().getHeight() + centerImage.getY() < 0) { //Kun vertikaalinen täyttää ruudun ja keskimmäinen on jäänyt alapuolelle
                    //Vaihdetaan keskimmäinen ja vertikaalinen, ja viedään uusi vertikaalinen keskimmäisen yläpuolelle
                    ImageView center = centerImage;
                    centerImage = nextVerticalImage;
                    nextVerticalImage = center;
                    nextVerticalImage.setY(centerImage.getY() + centerImage.getImage().getHeight());
                    nextVerticalImage.setX(centerImage.getX());

                    ImageView hi = nextHorizontalImage;
                    nextHorizontalImage = nextDiagonalImage;
                    nextDiagonalImage = hi;
                    nextDiagonalImage.setY(nextVerticalImage.getY());
                    nextDiagonalImage.setX(nextHorizontalImage.getX());

                }
            } else { // (centerImage.getY() > 0) { //Jos kuvat liikkuu alaspäin
                if (nextVerticalImage.getY() > centerImage.getY()) { //tarkistetaan onko muut kuvat keskimmäisen yläpuolella
                    //Vaihdetaan kuvat keskimmäisen alapuolelle
                    nextVerticalImage.setY(-centerImage.getImage().getHeight());
                    nextVerticalImage.setX(centerImage.getX());
                    nextDiagonalImage.setY(nextVerticalImage.getY());
                    nextDiagonalImage.setX(nextHorizontalImage.getX());
                } else if (centerImage.getY() > nextVerticalImage.getImage().getHeight()) {//Kun vertikaalinen täyttää ruudun ja keskimmäinen on jäänyt yläpuolelle
                    //Vaihdetaan keskimmäinen ja vertikaalinen, ja viedään uusi vertikaalinen keskimmäisen alapuolelle
                    ImageView center = centerImage;
                    centerImage = nextVerticalImage;
                    nextVerticalImage = center;
                    nextVerticalImage.setY(-nextVerticalImage.getImage().getHeight());
                    nextVerticalImage.setX(centerImage.getX());

                    ImageView hi = nextHorizontalImage;
                    nextHorizontalImage = nextDiagonalImage;
                    nextDiagonalImage = hi;
                    nextDiagonalImage.setY(nextVerticalImage.getY());
                    nextDiagonalImage.setX(nextHorizontalImage.getX());
                }
            }
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}
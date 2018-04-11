package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

/**
 * Pelin liikkuva tausta. Sprite ei itsessään liiku, vaan se liikuttelee kuvia itsensä sisällä.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class GameBackground extends Sprite implements Updateable  {

    /**
     * Taustan vakiovierimisnopeus.
     */
    private static final double NORMAL_SCROLL_SPEED = 30;

    /**
     * Taustan nykyinen vierimisnopeus.
     */
    private double scrollSpeed = NORMAL_SCROLL_SPEED;

    /**
     * Kertoo ajan sekunteina, kuinka pitkään tausta liikkuu eri vauhtia kuin vakio.
     */
    private double tempScrollSpeedDuration;

    /**
     * Kertoo, että tällä hetkellä on käytössä väliaikainen vierimisnopeus.
     */
    private boolean tempScrollFlag = false;

    /**
     * Kuva1
     */
    private ImageView centerImage;

    /**
     * Seuraava kuva
     */
    private ImageView nextHorizontalImage;

    /**
     * Konstruktori, luo kuvat ja lisää ne tämän Spriten Paneen.
     * @param controller Pelin kontrolleri.
     */
    public GameBackground(Controller controller) {

        controller.addUpdateableAndSetToScene(this);

        String imagePath = "images/darkSpace.jpg";

        centerImage = new ImageView(new Image(imagePath,
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));

        nextHorizontalImage = new ImageView(new Image(imagePath,
                GameMain.WINDOW_WIDTH, GameMain.WINDOW_HEIGHT, false, false));

        centerImage.setY(centerImage.getY());

        nextHorizontalImage.setY(nextHorizontalImage.getY());
        nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());

        this.getChildren().add(centerImage);
        this.getChildren().add(nextHorizontalImage);
    }

    /**
     * Vaihtaa taustan vierimisnopeutta.
     * @param scrollSpeed Taustan vierimisnopeus.
     */
    public void changeBackgroundScrollSpeed(double scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        tempScrollFlag = false;
    }

    /**
     * Vaihtaa taustan vierimisnopeutta tietyksi ajaksi, jonka jälkeen vierimisnopeus palaa vakioarvoon.
     * @param scrollSpeed Taustan vierimisnopeus.
     * @param duration Uuden vierimisnopeuden kesto sekunteina.
     */
    public void changeBackgroundScrollSpeed(double scrollSpeed, double duration) {
        this.scrollSpeed = scrollSpeed;
        tempScrollSpeedDuration = duration;
        tempScrollFlag = true;
    }

    /**
     * Alustaa taustan vierimisnopeuden vakioarvoon.
     */
    public void resetBackgroundScrollSpeed() {
        scrollSpeed = NORMAL_SCROLL_SPEED;
        tempScrollFlag = false;
    }

    @Override
    public void update(double deltaTime) {

        // Väliaikaisen vierimisnopeuden hoitaminen
        if (tempScrollFlag) {
            if (tempScrollSpeedDuration < 0) {
                scrollSpeed = NORMAL_SCROLL_SPEED;
                tempScrollFlag = false;
            } else {
                tempScrollSpeedDuration -= deltaTime;
            }
        }

        if(deltaTime < 100) { // fiksaa oudon bugin tason alussa
            centerImage.setX(centerImage.getX() - (scrollSpeed * deltaTime));
            nextHorizontalImage.setX(nextHorizontalImage.getX() - (scrollSpeed * deltaTime));
        }

        if (centerImage.getImage().getWidth() + centerImage.getX() <= 0) {
            ImageView iv = centerImage;
            centerImage = nextHorizontalImage;
            nextHorizontalImage = iv;
            nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());
        }
    }
}
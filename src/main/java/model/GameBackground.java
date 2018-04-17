package model;

import controller.Controller;
import controller.GameController;
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
     * Nopeus, johon taustan vierimisnopeus kiihtyy.
     */
    private double targetScrollSpeed;

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

    private boolean decelerate = false;

    /**
     * Konstruktori, luo kuvat ja lisää ne tämän Spriten Paneen.
     */
    public GameBackground() {
        Controller controller = GameController.getInstance();
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
        targetScrollSpeed = scrollSpeed;
        tempScrollFlag = false;
    }

    /**
     * Vaihtaa taustan vierimisnopeutta tietyksi ajaksi, jonka jälkeen vierimisnopeus palaa vakioarvoon.
     * @param scrollSpeed Taustan vierimisnopeus.
     * @param duration Uuden vierimisnopeuden kesto sekunteina.
     */
    public void changeBackgroundScrollSpeed(double scrollSpeed, double duration) {
        targetScrollSpeed = scrollSpeed;
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

            // Ollaan menty väliaikaista nopeutta asetetun ajan verran, aloitetaan hidastus
            if (tempScrollSpeedDuration < 0) {
                tempScrollFlag = false;
                targetScrollSpeed = NORMAL_SCROLL_SPEED;
                decelerate = true;
            } else {
                // Jos nykyinen nopeus ei vielä tavoitteessa, kasvatetaan nopeutta
                if (scrollSpeed < targetScrollSpeed) {
                    scrollSpeed += 20;
                } else {
                    // Ollaan nopeustavoitteessa (tai menty yli), asetetaan tavoite vierimisnopeudeksi
                    scrollSpeed = targetScrollSpeed;
                }
                // väliaikaisen nopeuden ajan kirjaus
                tempScrollSpeedDuration -= deltaTime;
            }
        }

        // Jos pitää hidastaa...
        if (decelerate) {
            // Ei olla vielä tavoitenopeudessa, hidastetaan lisää
            if (scrollSpeed > targetScrollSpeed) {
                scrollSpeed -= 20;
            } else {
                // ollaan tavoitteessa
                scrollSpeed = targetScrollSpeed;
                decelerate = false;
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
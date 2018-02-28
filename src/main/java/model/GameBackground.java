package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import view.GameMain;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class GameBackground extends Sprite implements Updateable  {
private double scrollSpeed = 15;

    /**
     * Pelin kontrolleri
     */
private Controller controller;

private ImageView centerImage;
private ImageView nextHorizontalImage;
private ImageView nextVerticalImage;
private ImageView nextDiagonalImage;

    public GameBackground(Controller controller) {

        controller.addUpdateable(this);

        String imagePath = "images/Background.jpg";

        centerImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                (double) GameMain.WINDOW_WIDTH, (double)GameMain.WINDOW_HEIGHT, false, false));

        nextHorizontalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                (double) GameMain.WINDOW_WIDTH, (double)GameMain.WINDOW_HEIGHT, false, false));

        nextVerticalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                (double) GameMain.WINDOW_WIDTH, (double)GameMain.WINDOW_HEIGHT, false, false));

        nextDiagonalImage = new ImageView(new Image(imagePath, //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                (double) GameMain.WINDOW_WIDTH, (double)GameMain.WINDOW_HEIGHT, false, false));


        centerImage.setY(centerImage.getY() + 15); //siirretään backgroundia alemmas, jotta fps näkyy

        nextHorizontalImage.setY(nextHorizontalImage.getY() + 15); //siirretään backgroundia alemmas, jotta fps näkyy
        nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());

        //nextDiagonalImage.setX(nextDiagonalImage.getX() + nextDiagonalImage.getImage().getWidth());
        //nextDiagonalImage.setY(1);


        this.getChildren().add(centerImage);
        this.getChildren().add(nextHorizontalImage);

    }

    @Override
    public void update(double deltaTime) {


        centerImage.setX(centerImage.getX() - scrollSpeed);
        nextHorizontalImage.setX(nextHorizontalImage.getX() - scrollSpeed);




        if (centerImage.getImage().getWidth() + centerImage.getX() <= 0) {
            ImageView iv = centerImage;
            centerImage = nextHorizontalImage;
            nextHorizontalImage = iv;
            nextHorizontalImage.setX(centerImage.getX() + centerImage.getImage().getWidth());
        }
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}
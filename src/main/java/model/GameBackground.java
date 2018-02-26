package model;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.GameMain;

public class GameBackground extends Sprite implements Updateable  {
private double scrollSpeed = 1;
    /**
     * Pelin kontrolleri
     */
private Controller controller;


/*
background = new Background(
                new BackgroundImage(new Image("images/bg.png"), BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,null, null)
                );
 */
    public GameBackground(Controller controller) {
        ImageView background = new ImageView(new Image("images/Background.jpg", //Kuvaa on nyt vain levitetty havainnollistamisen vuoksi
                (double)GameMain.WINDOW_WIDTH * 100, (double)GameMain.WINDOW_HEIGHT, false, false));
        background.setY(background.getY() + 15); //siirretään backgroundia alemmas, jotta fps näkyy

        controller.addUpdateable(this);
        this.getChildren().add(0, background);
    }

    @Override
    public void update(double deltaTime) {
        setPosition(getXPosition() - scrollSpeed, getYPosition());
        moveStep(deltaTime);
    }

    @Override
    public void collides(Updateable collidingUpdateable) {

    }

    @Override
    public void destroyThis() {

    }
}

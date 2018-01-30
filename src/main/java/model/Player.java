package model;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;


public class Player extends Unit {
    Scene scene;//liikkumis event handlereita varten

    //
    private final double width = 64;
    final private double height = 64;
    final Point2D startingPosition = new Point2D(20, 60);
    final double speed = 3;

    // ============ Pelaajan ohjaukseen liittyvää koodia TODO: viistoliike, nopeampi reaktio
    public Player(Scene scene) {//konstruktori
        this.scene = scene;
        spawnShip();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W://ylös
                    this.move(0);
                    break;
                case A://vasen
                    this.move(6);
                    break;
                case S://alas
                    this.move(4);
                    break;
                case D://oikea
                    this.move(2);
                    break;
            }
        });
    }

    // ============
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

    public void spawnShip(){
        this.setImage(new Image(getClass().getResourceAsStream("/images/spaceship_small_cyan_placeholder.png"), width, height, true, true));
        this.setPosition(startingPosition.getX(), startingPosition.getY());
        this.setVelocity(speed);
        //this.setSize(new Point2D(64, 64)); // ei tee mitään mutta jätän toistaseks jos löytyy käyttöä
    }
}

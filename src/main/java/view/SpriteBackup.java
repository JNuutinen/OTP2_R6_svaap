package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/*
    Inspoo näist:
    https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
    https://github.com/tutsplus/Introduction-to-JavaFX-for-Game-Development
 */

/**
 * Pitää sisällään spriten kuvan, sijainnin, nopeuden ja koon.
 * Metodeilla katsotaan törmäykset, päivitetään sijainti ja renderöidään sprite.
 */
public class SpriteBackup {
    /**
     * Spriten kuva
     */
    private Image image;

    /**
     * Spriten sijainnin x-koordinaatti
     */
    private double positionX;

    /**
     * Spriten y-koordinaatti
     */
    private double positionY;

    /**
     * Spriten vauhti suuntaan x
     */
    private double velocityX;

    /**
     * Spriten vauhti suuntaan y
     */
    private double velocityY;

    /**
     * Spriten leveys (otetaan kuvasta)
     */
    private double width;

    /**
     * Spriten korkeus (otetaan kuvasta)
     */
    private double height;

    protected SpriteBackup() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    @Override
    public String toString() {
        return "Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]";
    }

    /**
     * Lisää nopeutta.
     * @param x Lisää nopeutta suuntaan x
     * @param y Lisää nopeutta suuntaan y
     */
    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    /**
     * Tarkistaa, törmääkö spriteBackup jonkin muun spriten kanssa.
     * @param spriteBackup SpriteBackup, jonka kanssa törmäys tarkistetaan
     * @return True jos törmäys, muuten false
     */
    public boolean collides(SpriteBackup spriteBackup) {
        return spriteBackup.getBoundary().intersects(this.getBoundary());
    }

    /**
     * Piirtää spriten
     * @param graphicsContext GraphicsContext, johon piirretään
     */
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(image, positionX, positionY);
    }

    /**
     * Asettaa spriten kuvan ja koon kuvan mukaan
     * @param image Kuva, joka asetetaan spritelle
     */
    public void setImage(Image image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * Asettaa spriten sijainnin
     * @param x x-koordinaatti
     * @param y y-koordinaatti
     */
    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    /**
     * Asettaa spriten nopeuden
     * @param x Asettaa nopeuden suuntaan x
     * @param y Asettaa nopeuden suuntaan y
     */
    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    /**
     * Päivittää spriten sijainnin ajan ja nopeuden mukaan
     * @param time kulunut aika (TODO: sekunteina?)
     */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    /**
     * Palauttaa spriten rajat ja sijainnin.
     * @return Rectangle2D mallinnuksen spritestä
     */
    private Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }
}

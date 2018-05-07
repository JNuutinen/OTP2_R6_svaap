package model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Spriten rajapinta.
 */
public interface Sprite {

    /**
     * Palauttaa Spriten ympyrähitboxin säteen.
     * @return Spriten ymnpyrähitboxin säde desimaalilukuna.
     */
    double getHitboxRadius();

    /**
     * Palauttaa Spriten sijainnin koordinaatit.
     * @return Spriten sijainnin koordinaatit Point2D-muodossa.
     */
    Point2D getPosition();

    /**
     * Palauttaa Spriten tunnistetagin.
     *
     * @return Spriten Tag enum.
     */
    Tag getTag();

    /**
     * Asettaa Spriten tunnistetagin.
     * @param tag Spritelle asetettava Tag enum.
     */
    void setTag(Tag tag);

    /**
     * Palauttaa Spriten suunnan asteina.
     * @return Spriten suunta.
     */
    double getDirection();

    /**
     * Asettaa Spriten kuvan niin että kuvalla ja Spritellä on sama keskipiste.
     * @param image Spritelle annettava uusi kuva.
     * @param width Kuvan leveys.
     * @param height Kuvan korkeus.
     */
    void setImage(Image image, double width, double height);

    /**
     * Asettaa parametrin osoittamaan, että sprite on/ei ole liikkeessä.
     * @param isMoving Liikkuuko Sprite.
     */
    void setIsMoving(boolean isMoving);

    /**
     * Asettaa Spriten sijainnin.
     * @param x Spriten uusi x-koordinaatti.
     * @param y Spriten uusi y-koordinaatti.
     */
    void setPosition(double x, double y);

    /**
     * Palauttaa Spriten x-koordinaatin.
     * @return Spriten sijainnin x-koordinaatti.
     */
    double getXPosition();

    /**
     * Palauttaa Spriten y-koordinaatin.
     * @return Spriten sijainnin y-koordinaatti.
     */
    double getYPosition();

    /**
     * Palauttaa Spriten nopeuden.
     * @return Spriten nopeus.
     */
    double getVelocity();

    /**
     * Asettaa Spriten nopeuden.
     * @param velocity Spriten uusi nopeus.
     */
    void setVelocity(double velocity);

    /**
     * Asettaa Spriten ympyränmuotoisen hitboxin.
     * @param circleHitboxDiameter Hitboxin halkaisija.
     */
    void setHitbox(double circleHitboxDiameter);

    /**
     * Laskee kulman itsensa ja kohteen valilla yksikkoympyran mukaisesti (esim. jos kohde suoraan ylapuolella, kulma on 90)
     * @param target Kohteen sijainti Point2D oliona.
     * @return Kulma Spriten ja kohteen välillä.
     */
    double getAngleFromTarget(Point2D target);

    /**
     * Laskee itsensa ja kohteen valisen etaisyyden.
     * @param target Kohteen sijainti Point2D oliona.
     * @return Etäisyys Spriten ja kohteen välillä.
     */
    double getDistanceFromTarget(Point2D target);

    /**
     * lukitsee suunnan niin etta jatkaa tiettyyn suuntaan menemista vaikka spritea kaantelisi samalla.
     * @param angle lukittu suunta.
     */
    void lockDirection(double angle);

    /**
     * Liikuttaa Spritea askeleen direction-suuntaan kerrottuuna velocity-muuttujalla. Käytetään GameLoopissa.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    void moveStep(double deltaTime);

    /**
     * Kääntää Spriteä.
     * @param degrees Kulma asteina, jonka verran spriteä käännetään.
     */
    void rotate(double degrees);
}

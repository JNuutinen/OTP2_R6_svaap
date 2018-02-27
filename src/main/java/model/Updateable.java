package model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * Rajapinta Pelin loopissa päivitettävillä olioille.
 */
public interface Updateable {

    /**
     * Päivittää olion tilan kuluneen ajan mukaisesti.
     * @param deltaTime Kulunut aika viime päivityksestä.
     */
    void update(double deltaTime);

    /**
     * Kutsutaan, kun osuma on tapahtunut parametrinä annetun updateablen kanssa.
     * Hoitaa osuman seurausten käsittelyn. Olettaa, että törmäys olioiden kanssa on mahdollinen.
     * @param collidingUpdateable Updateable-rajapinnan toteuttava olio, johon törmätty.
     */
    void collides(Updateable collidingUpdateable);

    /**
     * Poistaa tämän olion pelistä.
     */
    void destroyThis();

    /**
     * Palauttaa olion hitboxin.
     * @return Olion hitbox Shape-oliona.
     */
    Shape getShape();

    /**
     * Palauttaa olion sijainnin.
     * @return Olion sijainti Point2D-oliona.
     */
    Point2D getPosition();

    /**
     * Palauttaa olion tunnistetagin.
     * @return Olion tagi merkkijonona.
     */
    String getTag();

    /**
     * Asettaa olion tunnistetagin.
     * @param tag Olion tagi merkkijonona.
     */
    void setTag(String tag);
}

package model;

import javafx.geometry.Point2D;

/**
 * Rajapinta Pelin loopissa päivitettävillä olioille.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
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
     * Palauttaa ympyränmuotoisen hitboxin halkaisijan.
     * @return Hitboxin halkaisija.
     */
    double getHitboxRadius();


    /**
     * Palauttaa olion sijainnin.
     * @return Olion sijainti Point2D-oliona.
     */
    Point2D getPosition();

    /**
     * Palauttaa olion tunnistetagin.
     * @return Olion taginumero.
     */
    int getTag();

    /**
     * Asettaa olion tunnistetagin.
     * @param tag Olion tagi kokonaisnumerona.
     */
    void setTag(int tag);
}

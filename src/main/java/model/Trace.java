package model;

import javafx.geometry.Point2D;

import java.util.List;

public interface Trace {

    /**
     * Palauttaa viivan aloitus- ja lopetuskoordinaatit.
     * @return List jossa on indeksissä 0 ja 1 viivan aloitus- ja lopetuskoordinaatti.
     */
    List<Point2D> getTraceCoordinates();

    /**
     * Palauttaa olion sijainnin.
     * @return Olion sijainti Point2D-oliona.
     */
    Point2D getPosition();

    /**
     * TODO koko homma
     * Kutsutaan, kun osuma on tapahtunut parametrinä annetun Hitboxin kanssa.
     * Hoitaa osuman seurausten käsittelyn. Olettaa, että törmäys olioiden kanssa on mahdollinen.
     * @param collidingHitbox Updateable-rajapinnan toteuttava olio, johon törmätty.
     */
    void collides(Object obj);

    /**
     * Poistaa tämän olion pelistä.
     */
    void destroyThis();


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

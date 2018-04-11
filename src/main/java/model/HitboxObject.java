package model;

import javafx.geometry.Point2D;

public interface HitboxObject {

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
     * TODO aa
     * Kutsutaan, kun osuma on tapahtunut parametrinä annetun updateablen kanssa.
     * Hoitaa osuman seurausten käsittelyn. Olettaa, että törmäys olioiden kanssa on mahdollinen.
     * @param collidingUpdateable Updateable-rajapinnan toteuttava olio, johon törmätty.
     */
    void collides(Object collidingTarget);

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

package model;

import javafx.geometry.Point2D;

public interface HitboxCircle {

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
     * Kutsutaan, kun osuma on tapahtunut parametrinä annetun Hitbox-olion kanssa.
     * Hoitaa osuman seurausten käsittelyn. Olettaa, että törmäys olioiden kanssa on mahdollinen.
     * @param collidingTarget olio johon osui, joka on Hitbox- -rajapintaolio.
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

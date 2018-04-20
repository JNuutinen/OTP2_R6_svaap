package model;

import javafx.geometry.Point2D;

import java.util.List;

public interface HitboxTrace {

    /**
     * Palauttaa viivan aloitus- ja lopetuskoordinaatit.
     * @return List jossa on indeksissä 0 ja 1 viivan aloitus- ja lopetuskoordinaatti.
     */
    List<Point2D> getTraceCoordinates();


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
}

package model.weapons;

import model.Unit;

/**
 * Rajapinta aseille.
 */
public interface Weapon {

    /**
     * Ampuu aseella.
     */
    void shoot();

    /**
     * Palauttaa aseen tulinopeuden.
     * @return Aseen tulinopeus.
     */
    double getFireRate();
}

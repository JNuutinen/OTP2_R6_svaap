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

    /** asettaa ampujan */
    void setShooter(Unit shooter);

    /**
     * Palauttaa aseen tulinopeuden.
     * @return Aseen tulinopeus.
     */
    double getFireRate();
}

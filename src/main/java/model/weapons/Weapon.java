package model.weapons;

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

package model.weapons;

/**
 * Rajapinta aseille.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
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

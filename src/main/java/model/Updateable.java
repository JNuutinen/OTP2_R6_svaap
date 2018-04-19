package model;

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
}

package model.level;

/**
 * Interface tasosäikeille.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public interface Level {

    /**
     * Jatkaa tason suoritusta.
     */
    void continueLevel();

    /**
     * Lopettaa (poistaa) tason.
     */
    void destroyLevel();

    /**
     * Pysäyttää tason.
     */
    void pauseLevel();

    /**
     * Käynnistää tason.
     */
    void startLevel();
}

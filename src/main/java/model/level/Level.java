package model.level;

/**
 * Interface tasosäikeille.
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

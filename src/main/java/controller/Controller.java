package controller;

import model.Player;
import model.Unit;
import model.Updateable;
import model.level.Level;

import java.util.ArrayList;

/**
 * Ohjelman kontrolleri. Ottaa vastaan kutsuja sekä Viewiltä että Modelilta.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public interface Controller {

    /**
     * Lisää peliin pelaajan.
     * @param player Pelaaja.
     */
    void addPlayer(Player player);

    /**
     * Lisää pelaajan pistemäärää.
     * @param score Pisteet, jotka lisätään pelaajalle.
     */
    void addScore(int score);

    /**
     * Lisää Updateable-olion Updateable listaan GameLoopille, sekä Viewiin Spritenä.
     * @param updateable Updateable, joka lisätään.
     */
    void addUpdateableAndSetToScene(Updateable updateable);

    void addUpdateable(Updateable updateable);

    // TODO: collision listan refaktorointi pois

    /**
     * Lisää Unitin collision listaan.
     * @param unit Unit, joka lisätään.
     */
    void addUnitToCollisionList(Unit unit);

    Level getLevel();

    /**
     * Poistaa Unitin collision listasta.
     * @param unit Unit, joka poistetaan.
     */
    void removeFromCollisionList(Unit unit);

    /**
     * Palauttaa pelaajan pisteet.
     * @return Pelaajan pisteet.
     */
    int getScore();

    /**
     * Palauttaa collision listan.
     * @return Lista, joka sisältää collision listan Unitit.
     */
    ArrayList<Unit> getCollisionList();

    /**
     * Palauttaa Updateable listan pelin päivitettävistä Spriteistä.
     * @return Updateable lista.
     */
    ArrayList<Updateable> getUpdateables();

    /**
     * Käynnistää tasosäikeen.
     * @param levelNumber Tason numero.
     */
    void startLevel(int levelNumber);

    /**
     * Käynnistää GameLoopin.
     */
    void startLoop();

    /**
     * Asettaa debug fps mittarin fps.
     * @param fps Viimesin fps.
     */
    void setFps(double fps);

    /**
     * Asettaa debug fps nykyisen fps arvon.
     * @param fps Viimeisin fps.
     */
    void setCurrentFps(double fps);

    /**
     * Poistaa GameLoopin käsittelystä Updateable olion.
     * @param updateable Olio, joka poistetaan.
     */
    void removeUpdateable(Updateable updateable);

    /**
     * Palauttaa pelin Main menuun, ja alustaa tarvittavat ohjelman komponentit uutta käynnistystä varten.
     */
    void returnToMain();

    /**
     * Asettaa healthbarin (joko pelaaja tai boss).
     * @param hp Healthbarissa olevat hitpointsit.
     * @param selector Kertoo, minkä healthbar luodaan.
     */
    void setHealthbar(int hp, int selector);

    /**
     * Laittaa pelin taukotilaan. Asettaa GameLoopissa mitattavan kuluneen ajan nollaan, ja pysäyttää Level säikeen.
     */
    void pauseGame();

    /**
     * Jatkaa peliä taukotilasta.
     */
    void continueGame();
}

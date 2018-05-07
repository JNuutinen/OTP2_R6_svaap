package view;

import javafx.scene.layout.Pane;
import model.Sprite;
import model.units.Unit;
import model.weapons.Weapon;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Viewin interface.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public interface View {

    /**
     * Lisää Spriten peliin.
     * @param sprite Sprite joka lisätään.
     */
    void addSprite(Sprite sprite);

    /**
     * Lisää Unitin collision listaan.
     * @param unit Unit, joka lisätään.
     */
    void addUnitToCollisionList(Unit unit);

    /**
     * Poistaa Unitin collision listasta.
     * @param unit Unit, joka poistetaan.
     */
    void removeFromCollisionList(Unit unit);

    /**
     * Palauttaa Collision listan.
     * @return ArrayList, jossa Unitteja.
     */
    ArrayList<Unit> getCollisionList();

    /**
     * Poistaa Spriten pelistä.
     * @param sprite Sprite, joka poistetaan.
     */
    void removeSprite(Sprite sprite);

    /**
     * Asettaa fps näkymään.
     * @param fps Nykyinen fps.
     */
    void setFps(double fps);

    /**
     * Asettaa fps näkymään 2
     * @param fps Nykyinen fps.
     */
    void setCurrentFps(double fps);

    /**
     * Asettaa pelaajan pisteet näkymään.
     * @param score Pelaajan pisteet.
     */
    void setScore(int score);

    /**
     * Palauttaa pelin päävalikkoon.
     */
    void returnToMain();

    /**
     * Asettaa healthbarin joko pelaajalle tai bossille.
     * @param hp Healthbarin hitpointsit.
     * @param selector Valitsin sille, kenen healthbar asetetaan.
     */
    void setHealthbar(int hp, int selector);

    /**
     * Asettaa pelin taukotilaan.
     */
    void pause();

    /**
     * Käynnistää pelin. Käskee kontrolleria aloittamaan GameLoopin ja Levelin.
     *
     * @param primary   Pelaajan pääase.
     * @param secondary Pelaajan sivuase.
     */
    void init(Weapon primary, Weapon secondary);

    /**
     * Käynnistää pelin.
     */
    void startGame();

    /**
     * Palauttaa pelin menunaikaisen rootin.
     *
     * @return Pane, jossa menun viewit.
     */
    Pane getUiRoot();

    /**
     * Muuttaa taustan vierimisnopeutta tietyksi ajaksi.
     * @param speed Taustan väliaikainen vierimisnopeus.
     * @param duration Väliaikaisen vierimisnopeuden kesto, jonka jälkeen vierimisnopeus palaa vakioarvoonsa.
     */
    void changeBackgroundScrollSpeed(double speed, double duration);

    /**
     * Asettaa Viewissä käytettävän ResourceBundlen lokalisaatiota varten.
     *
     * @param messages
     */
    void setResourceBundle(ResourceBundle messages);
}

package controller;

import model.*;
import model.units.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Ohjelman kontrolleri. Ottaa vastaan kutsuja sekä Viewiltä että Modelilta.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public interface Controller {

    /**
     * Lisää pelaajat peliin.
     * @param players Pelaajat
     */
    void addPlayers(ArrayList<Player> players);

    /**
     * Lisää pelaajan pistemäärää.
     * @param score Pisteet, jotka lisätään pelaajalle.
     */
    void addScore(int score);

    /**
     * Lisää HitboxCircle-rajapinnan toteuttavan olion pelilooppiin osumatarkastelua varten, muttei visuaalista Spriteä,
     * eikä oliota päivityslistaan.
     * @param hitboxCircle Ympyrähitboxillinen olio.
     */
    void addHitboxObject(HitboxCircle hitboxCircle);

    /**
     * Palauttaa pelissä olevat HitboxCircle-rajapinnan toteuttavat oliot listana.
     * @return Lista HitboxCircle-olioista pelissä.
     */
    List<HitboxCircle> getHitboxObjects();

    /**
     * Palauttaa listan pelaajan (pelaajien?) spriteistä.
     * @return Lista, jossa pelaajien spritet.
     */
    List<HitboxCircle> getPlayerHitboxObjects();

    /**
     * Lisää HitboxTrace-rajapinnan toteuttavan olion pelilooppiin, osumatarkastelua varten, muttei visuaalista Spriteä,
     * eikä oliota päivityslistaan.
     * @param hitboxTrace Tracehitboxillinen olio.
     */
    void addTrace(HitboxTrace hitboxTrace);

    /**
     * Lisää Updateable-olion peliloopin päivityslistaan, sekä visuaalisen Spriten pelimaailmaan.
     * @param updateable Updateable-rajapinnan toteuttava olio.
     */
    void addUpdateableAndSetToScene(Updateable updateable);

    /**
     * Lisää Updateable-olion peliloopin päivityslistaan, muttei osumatarkasteluun, eikä visuaalista Spriteä peliin.
     * @param updateable Updateable-olio, joka lisätään päivityslistaan.
     */
    void addUpdateable(Updateable updateable);

    /**
     * Palauttaa Updateable listan pelin päivitettävistä Spriteistä.
     * @return Updateable lista.
     */
    List<Updateable> getUpdateables();

    /**
     * Palauttaa pelaajan pisteet.
     * @return Pelaajan pisteet.
     */
    int getScore();

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
     * Poistaa GameLoopin käsittelystä Updateable- ja HitboxCircle -rajapinnat toteuttavan olion.
     * @param updateable Olio, joka poistetaan.
     * @param hitboxCircle Se sama olio.
     */
    void removeUpdateable(Updateable updateable, HitboxCircle hitboxCircle);

    /**
     * Poistaa GameLoopin käsittelystä Updateable- ja HitboxTrace -rajapinant toteuttavan olion.
     * @param updateable Olio, joka poistetaan.
     * @param hitboxTrace Se sama olio.
     */
    void removeUpdateable(Updateable updateable, HitboxTrace hitboxTrace);

    /**
     * Poistaa GameLoopin käsittelystä Updateable-rajapinnan toteuttavan olion.
     * @param updateable Olio, joka poistetaan.
     */
    void removeUpdateable(Updateable updateable);

    /**
     * Poistaa HitboxCircle -rajapintaolion pelistä
     * @param hitboxCircle HitboxCircle
     */
    void removeHitbox(HitboxCircle hitboxCircle);

    /**
     * Poistaa HitboxTrace -rajapintaolion pelistä
     * @param hitboxTrace HitboxTrace
     */
    void removeHitbox(HitboxTrace hitboxTrace);

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

    /**
     * Muuttaa taustan vierimisnopeutta tietyksi ajaksi.
     * @param speed Väliaikainen taustan vierimisnopeus.
     * @param duration Väliaikaisen nopeuden kesto sekunteina.
     */
    void changeBackgroundScrollSpeed(double speed, double duration);
}

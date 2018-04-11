package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import view.GameMain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static view.GameMain.UNDEFINED_TAG;

/**
 * Pelin game loop. Päivittää liikuteltavien spritejen sijainnin ja tarkastelee osumia.
 * @author Ilari Anttila
 * @author Jerry Hällfors
 * @author Juha Nuutinen
 * @author Henrik Virrankoski
 */
public class GameLoop {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Jono olioista, jotka lisätään pelistä seuraavan loopin alussa.
     */
    private volatile Queue<Updateable> updateableQueue;

    /**
     * Jono olioista, jotka poistetaan pelistä seuraavan loopin alussa.
     */
    private volatile Queue<Updateable> removeUpdateableQueue;

    /**
     * Lista pelissä olevista päivitettävistä olioista, jonka looppi käy läpi.
     */
    private volatile ArrayList<Updateable> updateables;

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<Updateable> enemies;

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<Updateable> enemyProjectiles;

    /**
     * Lista pelissä tiettynä hetkenä olevista pelaajan ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<Updateable> playerProjectiles;

    /**
     * Pelaaja, käytetään osumien tarkasteluun.
     */
    private Player player;

    /**
     * Pelin looppi. Spritejen sijaintien päivitys sekä osumatarkastelut.
     */
    private AnimationTimer gameLoop;

    /**
     * Kerroin, jolla delta timen voi muuttaa nollaksi = peli on pausella. Jos ei pausella, tää saa
     * arvon 1 = ei vaikuta deltatimeen
     */
    private int pauseModifier = 1;

    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     */
    public GameLoop(Controller controller) {
        this.controller = controller;
        updateableQueue = new LinkedList<>();
        removeUpdateableQueue = new LinkedList<>();
        updateables = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
        playerProjectiles = new ArrayList<>();
    }

    /**
     * Asettaa viittauksen pelaajaan.
     * @param player Pelin pelaaja.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Lisää olion jonoon, odottamaan lisäystä Updateable listaan.
     * @param updateable Updateable olio, joka lisätään listaan.
     */
    synchronized public void queueUpdateable(Updateable updateable) {
        updateableQueue.add(updateable);
    }

    /**
     * Lisää olion jonoon, odottamaan poistoa Updateable listasta.
     * @param updateable Updateable olio, joka poistetaan listasta.
     */
    synchronized public void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }

    /**
     * Palauttaa Updateable listan.
     * @return ArrayList, joka sisältää Updateable olioita.
     */
    public ArrayList<Updateable> getUpdateables(){
        return updateables;
    }

    /**
     * Asettaa GameLoopissa kuluneen ajan nollaksi, eli pysäyttää kaikkien Spritejen liikkeet.
     */
    public void pauseGame() {
        pauseModifier = 0;
    }

    /**
     * Poistaa nollauksen GameLoopissa kuluneesta ajasta, Spritet jatkavat liikkeitään.
     */
    public void continueGame() {
        pauseModifier = 1;
    }

    /**
     * Pysäyttää GameLoopin.
     */
    public void stopLoop(){
        gameLoop.stop();
    }

    /**
     * Alustaa ja käynnistää GameLoopin
     */
    public void startLoop() {

        gameLoop = new AnimationTimer(){

            private long lastUpdate = 0 ;
            double debugger_toSecondCounter = 0;
            int debugger_frameCounter = 0;

            @Override
            public void handle(long now) {
                if ((double) (now - lastUpdate) / 1_000_000_000.0 >= 1.0 / 150.0) {
                    double deltaTime = ((now - lastUpdate) / 1_000_000_000.0) * pauseModifier;
                    // jos taajuus on alhaisempi kuin asetettu taajuusrajagappi (250 fps)
                    if ((double) (now - lastUpdate) / 1_000_000_000.0 >= 1.0 / 250.0) {
                        // controller.setCurrentFps(1/((now - lastUpdate)/1000000000.0));

                        //---------- Olioiden lisäykset ja poistot

                        // Tarkistaa Updateable jonon, ja lisää jonottavat oliot Updateable listaan
                        // ja mahdollisesti joko enemies, enemyProjectiles, tai playerProjectiles listaan.
                        if (!updateableQueue.isEmpty()) {
                            for (Updateable updateable : updateableQueue) {
                                updateables.add(updateable);
                                switch (updateable.getTag()) {
                                    case GameMain.ENEMY_SHIP_TAG:
                                        enemies.add(updateable);
                                        break;
                                    case GameMain.BOSS_SHIP_TAG:
                                        enemies.add(updateable);
                                        break;
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        enemyProjectiles.add(updateable);
                                        break;
                                    case GameMain.ENEMY_TRACE_TAG:
                                        enemyProjectiles.add(updateable);
                                        break;
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        playerProjectiles.add(updateable);
                                        break;
                                    case GameMain.PLAYER_TRACE_TAG:
                                        playerProjectiles.add(updateable);
                                        break;
                                }
                            }
                            // Updateable jono tyhjennetään, kun sen oliot ovat lisätty peliin.
                            updateableQueue.clear();
                        }

                        // Poistojonossa olevat oliot poistetaan Updateable listasta, sekä mahdollisesti joko
                        // enemies, enemyProjectiles, tai playerProjectiles listasta.
                        if (!removeUpdateableQueue.isEmpty()) {
                            for (Updateable toBeRemoved : removeUpdateableQueue) {
                                if (updateables.contains(toBeRemoved)) {
                                    updateables.remove(toBeRemoved);
                                    switch (toBeRemoved.getTag()) {
                                        case GameMain.ENEMY_SHIP_TAG:
                                            enemies.remove(toBeRemoved);
                                            break;
                                        case GameMain.BOSS_SHIP_TAG:
                                            enemies.remove(toBeRemoved);
                                            break;
                                        case GameMain.ENEMY_PROJECTILE_TAG:
                                            enemyProjectiles.remove(toBeRemoved);
                                            break;
                                        case GameMain.ENEMY_TRACE_TAG:
                                            enemyProjectiles.remove(toBeRemoved);
                                            break;
                                        case GameMain.PLAYER_PROJECTILE_TAG:
                                            playerProjectiles.remove(toBeRemoved);
                                            break;
                                        case GameMain.PLAYER_TRACE_TAG:
                                            playerProjectiles.remove(toBeRemoved);
                                            break;
                                    }
                                }
                            }
                            // Poistojono tyhjätään, kun siinä olleet oliot on poistettu pelistä.
                            removeUpdateableQueue.clear();
                        }

                        //---------- Olioiden sijaintien päivitys

                        // Kun tässä iteraatiossa peliin lisättävät oliot on lisätty, päivitetään kaikkien
                        // pelissä olevien olioiden sijainnit.
                        for (Updateable updateable : updateables) {
                            if (updateable != null) {
                                //paivita rajapintaoliot
                                updateable.update(deltaTime);
                            }
                        }

                        //---------- Osumatarkastelu, käy läpi vain oleelliset Updateablet

                        // Vihollisten ammuksien vertailu Playeriin
                        for (Updateable enemyProjectile : enemyProjectiles) {
                            switch (enemyProjectile.getTag()) {

                                // Vihollisien perusammus
                                case GameMain.ENEMY_PROJECTILE_TAG:
                                    // jos objecktien valinen taisyys on pienempi kuin niiden hitboxien sateiden summa:
                                    if (getDistanceFromTarget(enemyProjectile.getPosition(), player.getPosition()) <
                                            (enemyProjectile.getHitboxRadius() + player.getHitboxRadius())) {
                                        // kutsu objektin collides-metodia
                                        enemyProjectile.collides(player);
                                    }
                                    break;

                                // Vihollisien laserammus
                                case GameMain.ENEMY_TRACE_TAG:
                                    if (enemyProjectile.getPosition().getX() < player.getPosition().getX()) {
                                        // jos playerin hitboxin säde yltää laseriin
                                        if (getDistanceFromTarget(enemyProjectile.getPosition(), player.getPosition()) <
                                                (enemyProjectile.getHitboxRadius())) {
                                            enemyProjectile.collides(player);
                                        }
                                    } else if (getDistanceFromTarget(new Point2D(0, enemyProjectile.getPosition().getY()),
                                            new Point2D(0, player.getPosition().getY())) < player.getHitboxRadius()) {
                                        enemyProjectile.collides(player);
                                    }
                                    // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                    enemyProjectile.setTag(UNDEFINED_TAG);
                                    break;
                            }
                        }

                        // Playerin ammuksien vertailu kaikkiin vihollisiin
                        for (Updateable playerProjectile : playerProjectiles) {
                            for (Updateable enemy : enemies) {
                                switch (playerProjectile.getTag()) {

                                    // Playerin perusammus
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        if (getDistanceFromTarget(playerProjectile.getPosition(), enemy.getPosition()) <
                                                (playerProjectile.getHitboxRadius() + enemy.getHitboxRadius())) {
                                            playerProjectile.collides(enemy);
                                        }
                                        break;

                                    // Playerin laserammus
                                    case GameMain.PLAYER_TRACE_TAG:
                                        if (playerProjectile.getPosition().getX() > enemy.getPosition().getX()) {
                                            // jos vihun hitboxin säde yltää laseriin
                                            if (getDistanceFromTarget(playerProjectile.getPosition(), enemy.getPosition()) <
                                                    (playerProjectile.getHitboxRadius())) {
                                                playerProjectile.collides(enemy);
                                            }
                                        } else if (getDistanceFromTarget(new Point2D(0, playerProjectile.getPosition().getY()),
                                                new Point2D(0, enemy.getPosition().getY())) < enemy.getHitboxRadius()) {
                                            playerProjectile.collides(enemy);
                                        }
                                        break;
                                }
                            }

                            // Kun kaikki viholliset käyty läpi laserin osumia tarkastellessa...
                            if (playerProjectile.getTag() == GameMain.PLAYER_TRACE_TAG) {
                                // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                playerProjectile.setTag(UNDEFINED_TAG);
                            }
                        }

                        // Fps päivitys
                        debugger_toSecondCounter += deltaTime;
                        debugger_frameCounter++;
                        if (debugger_toSecondCounter > 1) {
                            controller.setFps(debugger_frameCounter);
                            debugger_toSecondCounter = 0;
                            debugger_frameCounter = 0;
                        }

                        // Ajan kirjaus
                        lastUpdate = now;
                    }
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Apumetodi joka palauttaa kahden pisteen etäisyyden toisistaan.
     * @param source Piste 1.
     * @param target Piste 2.
     * @return Pisteiden etäisyys toisistaan.
     */
    private double getDistanceFromTarget(Point2D source, Point2D target){
        return Math.sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }
}

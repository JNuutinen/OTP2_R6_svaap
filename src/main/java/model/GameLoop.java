package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import view.GameMain;

import java.util.*;

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
    private volatile Queue<Updateable> updateableQueue = new LinkedList<>();

    /**
     * Jono olioista, jotka poistetaan pelistä seuraavan loopin alussa.
     */
    private volatile Queue<Updateable> removeUpdateableQueue = new LinkedList<>();

    /**
     * Lista pelissä olevista päivitettävistä olioista, jonka looppi käy läpi.
     */
    private volatile ArrayList<Updateable> updateables = new ArrayList<>();

    /**
     * TODO
     */
    private Queue<HitboxObject> hitboxObjectsQueue = new LinkedList<>();

    private Queue<HitboxObject> removeHitboxObjectsQueue = new LinkedList<>();

    /**
     * TODO
     */
    private Queue<Trace> tracesQueue = new LinkedList<>();

    private Queue<Trace> removeTracesQueue = new LinkedList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxObject> enemies = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxObject> enemyProjectiles = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista pelaajan ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxObject> playerProjectiles = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxObject> powerups = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<Trace> enemyTraces = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista pelaajan ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<Trace> playerTraces = new ArrayList<>();

    /**
     * Pelaaja, käytetään osumien tarkasteluun.
     */
    private ArrayList<Player> players = new ArrayList<>();

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
    }

    /**
     * Asettaa viittauksen pelaajiin;
     * @param players Pelaajat pelissä.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Lisää olion jonoon, odottamaan lisäystä Updateable listaan.
     * @param updateable Updateable olio, joka lisätään listaan.
     */
    synchronized public void queueUpdateable(Updateable updateable) {
        updateableQueue.add(updateable);
    }

    /**
     * Lisää Hitbox-rajapintaolion jonoon, odottamaan lisäystä peliin.
     * @param hitboxObject Hitbox-rajapintaolio
     */
    synchronized public void queueHitboxObject(HitboxObject hitboxObject) {
        hitboxObjectsQueue.add(hitboxObject);
    }

    /**
     * Lisää Trace-rajapintaolion jonoon, odottamaan lisäystä peliin.
     * @param trace Trace-rajapintaolio.
     */
    synchronized public void queueTrace(Trace trace) {
        tracesQueue.add(trace);
    }

    /**
     * Lisää Updateable-rajapintaolion jonoon, odottamaan poistoa Updateable listasta.
     * @param updateable Updateable olio, joka poistetaan listasta.
     */
    synchronized public void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }
    synchronized public void removeHitboxObject(HitboxObject hitboxObject) { removeHitboxObjectsQueue.add(hitboxObject);}
    synchronized public void removeTrace(Trace trace) { removeTracesQueue.add(trace);}

    /**
     * Palauttaa Updateable listan.
     * @return ArrayList, joka sisältää Updateable olioita.
     */
    public ArrayList<Updateable> getUpdateables(){
        return updateables;
    }

    /**
     * TODO
     * @return
     */
    public ArrayList<HitboxObject> getPlayerHitboxObjects(){
        ArrayList<HitboxObject> toHitboxlist = new ArrayList<>();
        toHitboxlist.addAll(players);
        return toHitboxlist;
    }

    public ArrayList<HitboxObject> getHitboxObjects(){
        ArrayList<HitboxObject> hitboxObjects = new ArrayList<>();

        hitboxObjects.addAll(enemyProjectiles);
        hitboxObjects.addAll(enemies);
        hitboxObjects.addAll(playerProjectiles);
        return hitboxObjects;
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

                        // TODO Tarkistaa Updateable jonon, ja lisää jonottavat oliot Updateable listaan
                        // ja mahdollisesti joko enemies, enemyProjectiles, tai playerProjectiles listaan.
                        if (!updateableQueue.isEmpty()) {
                            updateables.addAll(updateableQueue);

                            for(HitboxObject hitboxObject : hitboxObjectsQueue){
                                switch (hitboxObject.getTag()) {
                                    case GameMain.ENEMY_SHIP_TAG:
                                        enemies.add(hitboxObject);
                                        break;
                                    case GameMain.BOSS_SHIP_TAG:
                                        enemies.add(hitboxObject);
                                        break;
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        enemyProjectiles.add(hitboxObject);
                                        break;
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        playerProjectiles.add(hitboxObject);
                                        break;
                                    case GameMain.POWERUP_TAG:
                                        powerups.add(hitboxObject);
                                        break;
                                }
                            }
                            for (Trace trace : tracesQueue){
                                switch (trace.getTag()) {
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        enemyTraces.add(trace);
                                        break;
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        playerTraces.add(trace);
                                        break;
                                }
                            }
                            // queue-jonot tyhjennetään, kun sen oliot ovat lisätty peliin.
                            updateableQueue.clear();
                            hitboxObjectsQueue.clear();
                            tracesQueue.clear();
                        }

                        // Poistojonossa olevat oliot poistetaan Updateable listasta, sekä mahdollisesti joko
                        // enemies, enemyProjectiles, tai playerProjectiles listasta.
                        if (!removeUpdateableQueue.isEmpty()) {
                            //updateables.removeAll(removeUpdateableQueue);

                            for (Updateable toBeRemoved : removeUpdateableQueue) {
                                if (updateables.contains(toBeRemoved)) {
                                    updateables.remove(toBeRemoved);
                                }
                            }

                            for (HitboxObject toBeRemoved : removeHitboxObjectsQueue) {
                                if (enemies.contains(toBeRemoved)) {
                                    enemies.remove(toBeRemoved);
                                }
                                else if(enemyProjectiles.contains(toBeRemoved)) {
                                    enemyProjectiles.remove(toBeRemoved);
                                }
                                else if(playerProjectiles.contains(toBeRemoved)) {
                                    playerProjectiles.remove(toBeRemoved);
                                }
                                else if(enemyProjectiles.contains(toBeRemoved)) {
                                    enemyProjectiles.remove(toBeRemoved);
                                }
                                else if(powerups.contains(toBeRemoved)){
                                    powerups.remove(toBeRemoved);
                                }
                            }

                            for (Trace toBeRemoved : removeTracesQueue) {
                                if (enemyTraces.contains(toBeRemoved)) {
                                    enemyTraces.remove(toBeRemoved);
                                }
                                else if (playerTraces.contains(toBeRemoved)) {
                                    playerTraces.remove(toBeRemoved);
                                }
                            }
                            // Poistojono tyhjätään, kun siinä olleet oliot on poistettu pelistä.
                            removeTracesQueue.clear();
                            removeHitboxObjectsQueue.clear();;
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
                        for(Player player : players) {
                            for (HitboxObject enemyProjectile : enemyProjectiles) {
                                switch (enemyProjectile.getTag()) {

                                    // Vihollisien perusammus
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        // jos objecktien valinen taisyys on pienempi kuin niiden hitboxien sateiden summa:
                                        if (getDistanceFromTarget(enemyProjectile.getPosition(), player.getPosition()) <
                                                (enemyProjectile.getHitboxRadius() + player.getHitboxRadius())) {
                                            // kutsu objektin collides-metodia
                                            enemyProjectile.collides(players);
                                        }
                                        break;
                                }
                            }
                            //poweruppien vertailu playeriin
                            for (HitboxObject powerup : powerups) {
                                switch (powerup.getTag()) {

                                    // Vihollisien perusammus
                                    case GameMain.POWERUP_TAG:
                                        // jos objecktien valinen taisyys on pienempi kuin niiden hitboxien sateiden summa:
                                        if (getDistanceFromTarget(powerup.getPosition(), player.getPosition()) <
                                                (powerup.getHitboxRadius() + player.getHitboxRadius())) {
                                            // kutsu objektin collides-metodia
                                            powerup.collides(players);
                                            //powerup.setTag(UNDEFINED_TAG);
                                        }
                                        break;
                                }
                            }

                            for (Trace enemyTrace : enemyTraces) {
                                switch (enemyTrace.getTag()) {
                                    // Vihollisien laserammus
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        if (enemyTrace.getPosition().getX() < player.getPosition().getX()) {
                                            // jos playerin hitboxin säde yltää laseriin
                                            if (getDistanceFromTarget(enemyTrace.getPosition(), player.getPosition()) < 0) {
                                                enemyTrace.collides(players);
                                            }
                                        } else if (getDistanceFromTarget(new Point2D(0, enemyTrace.getPosition().getY()),
                                                new Point2D(0, player.getPosition().getY())) < player.getHitboxRadius()) {
                                            enemyTrace.collides(players);
                                        }
                                        // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                        enemyTrace.setTag(UNDEFINED_TAG);
                                        break;
                                }
                            }
                        }

                        // Playerin ammuksien vertailu kaikkiin vihollisiin
                        for (HitboxObject playerProjectile : playerProjectiles) {
                            for (HitboxObject enemy : enemies) {
                                switch (playerProjectile.getTag()) {

                                    // Playerin perusammus
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        if (getDistanceFromTarget(playerProjectile.getPosition(), enemy.getPosition()) <
                                                (playerProjectile.getHitboxRadius() + enemy.getHitboxRadius())) {
                                            playerProjectile.collides(enemy);
                                        }
                                        break;
                                }
                            }
                            /*
                            // Kun kaikki viholliset käyty läpi laserin osumia tarkastellessa...
                            if (playerProjectile.getTag() == GameMain.PLAYER_TRACE_TAG) {
                                // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                playerProjectile.setTag(UNDEFINED_TAG);
                            }*/
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

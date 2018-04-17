package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import view.GameMain;

import java.util.*;

import static java.lang.Math.sqrt;
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
    private Queue<HitboxCircle> hitboxObjectsQueue = new LinkedList<>();

    private Queue<HitboxCircle> removeHitboxObjectsQueue = new LinkedList<>();

    /**
     * TODO
     */
    private Queue<HitboxTrace> tracesQueue = new LinkedList<>();

    private Queue<HitboxTrace> removeTracesQueue = new LinkedList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxCircle> enemies = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxCircle> enemyProjectiles = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista pelaajan ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxCircle> playerProjectiles = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxCircle> powerups = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista vihollisten ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxTrace> enemyHitboxTraces = new ArrayList<>();

    /**
     * Lista pelissä tiettynä hetkenä olevista pelaajan ammuksista, käytetään osumien tarkasteluun.
     */
    private volatile ArrayList<HitboxTrace> playerHitboxTraces = new ArrayList<>();

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
     * @param hitboxCircle Hitbox-rajapintaolio
     */
    synchronized public void queueHitboxObject(HitboxCircle hitboxCircle) {
        hitboxObjectsQueue.add(hitboxCircle);
    }

    /**
     * Lisää HitboxTrace-rajapintaolion jonoon, odottamaan lisäystä peliin.
     * @param hitboxTrace HitboxTrace-rajapintaolio.
     */
    synchronized public void queueTrace(HitboxTrace hitboxTrace) {
        tracesQueue.add(hitboxTrace);
    }

    /**
     * Lisää Updateable-rajapintaolion jonoon, odottamaan poistoa Updateable listasta.
     * @param updateable Updateable olio, joka poistetaan listasta.
     */
    synchronized public void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }
    synchronized public void removeHitboxObject(HitboxCircle hitboxCircle) { removeHitboxObjectsQueue.add(hitboxCircle);}
    synchronized public void removeTrace(HitboxTrace hitboxTrace) { removeTracesQueue.add(hitboxTrace);}

    /**
     * Palauttaa Updateable listan.
     * @return ArrayList, joka sisältää Updateable olioita.
     */
    public ArrayList<Updateable> getUpdateables(){
        return updateables;
    }

    /**
     * palauttaa pelaajat hitBoxObjecteina
     * @return
     */
    public ArrayList<HitboxCircle> getPlayerHitboxObjects(){
        ArrayList<HitboxCircle> toHitboxlist = new ArrayList<>();
        toHitboxlist.addAll(players);
        return toHitboxlist;
    }

    public ArrayList<HitboxCircle> getHitboxObjects(){
        ArrayList<HitboxCircle> hitboxCircles = new ArrayList<>();

        hitboxCircles.addAll(enemyProjectiles);
        hitboxCircles.addAll(enemies);
        hitboxCircles.addAll(playerProjectiles);
        return hitboxCircles;
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

                            for(HitboxCircle hitboxCircle : hitboxObjectsQueue){
                                switch (hitboxCircle.getTag()) {
                                    case GameMain.ENEMY_SHIP_TAG:
                                        enemies.add(hitboxCircle);
                                        break;
                                    case GameMain.BOSS_SHIP_TAG:
                                        enemies.add(hitboxCircle);
                                        break;
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        enemyProjectiles.add(hitboxCircle);
                                        break;
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        playerProjectiles.add(hitboxCircle);
                                        break;
                                    case GameMain.POWERUP_TAG:
                                        powerups.add(hitboxCircle);
                                        break;
                                }
                            }
                            for (HitboxTrace hitboxTrace : tracesQueue){
                                switch (hitboxTrace.getTag()) {
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        enemyHitboxTraces.add(hitboxTrace);
                                        break;
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        playerHitboxTraces.add(hitboxTrace);
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

                            for (HitboxCircle toBeRemoved : removeHitboxObjectsQueue) {
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

                            for (HitboxTrace toBeRemoved : removeTracesQueue) {
                                if (enemyHitboxTraces.contains(toBeRemoved)) {
                                    enemyHitboxTraces.remove(toBeRemoved);
                                }
                                else if (playerHitboxTraces.contains(toBeRemoved)) {
                                    playerHitboxTraces.remove(toBeRemoved);
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
                            for (HitboxCircle enemyProjectile : enemyProjectiles) {
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
                                }
                            }
                            //poweruppien vertailu playeriin
                            for (HitboxCircle powerup : powerups) {
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
                            for(HitboxTrace enemyHitboxTrace : enemyHitboxTraces){
                                switch (enemyHitboxTrace.getTag()) {
                                    case GameMain.ENEMY_PROJECTILE_TAG:
                                        if (traceIntersectsCircle(enemyHitboxTrace, player.getPosition(), player.getHitboxRadius())) {
                                            enemyHitboxTrace.collides(player);
                                        }
                                        break;
                                }
                                enemyHitboxTrace.setTag(UNDEFINED_TAG); // TODO enemytrace ei luultavasti voi osua kuin yhteen pelaajaan vaikka pitäisi toisin
                            }
                        }

                        // Playerin ammuksien vertailu kaikkiin vihollisiin
                        for (HitboxCircle playerProjectile : playerProjectiles) {
                            for (HitboxCircle enemy : enemies) {
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
                        }
                        for(HitboxTrace playerHitboxTrace : playerHitboxTraces){
                            for (HitboxCircle enemy : enemies) {
                                switch (playerHitboxTrace.getTag()) {
                                    // Playerin perusammus
                                    case GameMain.PLAYER_PROJECTILE_TAG:
                                        if (traceIntersectsCircle(playerHitboxTrace, enemy.getPosition(), enemy.getHitboxRadius())) {
                                            playerHitboxTrace.collides(enemy);
                                        }
                                        break;
                                }
                            }
                            playerHitboxTrace.setTag(UNDEFINED_TAG);
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
        return sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }

    /**
     * Apumetodi tarkastelemaan janan ja ympyrän välistä osumaa
     * @param hitboxTrace jana hitboxTrace oliona.
     * @param circleLocation ympyrähitboxin sijainti.
     * @param radius ympyrähitboxin säde
     * @return osuiko jana ympyrään.
     */
    private boolean traceIntersectsCircle(HitboxTrace hitboxTrace, Point2D circleLocation, double radius){
        Point2D E = hitboxTrace.getTraceCoordinates().get(0);
        Point2D L = hitboxTrace.getTraceCoordinates().get(1);
        // tracen vektori, aloituspisteestä lopetuspisteeseen.
        Point2D d = new Point2D(L.getX() - E.getX(), L.getY() - E.getY());
        // vektori ympyrän keskipisteestä tracen aloituspisteeseen
        Point2D f = new Point2D(E.getX() - circleLocation.getX(), E.getY() - circleLocation.getY());

        double a = vectorDotProduct(d, d);
        double b = 2 * vectorDotProduct(f, d);
        double c = vectorDotProduct(f, f) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if( discriminant < 0 )
        {
            // ei osumaa
        }
        else
        {
            // luultavasti osui mutta pitää tarkistaa kaikki tilanteet...

            discriminant = sqrt(discriminant);
            double t1 = (-b - discriminant)/(2 * a);
            double t2 = (-b + discriminant)/(2 * a);

            // jos lävistää ympyrän kahesti tai lävistää vain ulkoa päin
            if( t1 >= 0 && t1 <= 1 )
            {
                return true ;
            }

            // jos lävistää vain ympyrän sisältä päin
            if( t2 >= 0 && t2 <= 1 )
            {
                return true ;
            }
            // muuten ei osunut
            return false ;
        }
        return false;
    }

    private double vectorDotProduct(Point2D vector1, Point2D vector2){
        return (vector1.getX() * vector2.getX()) + (vector1.getY() * vector2.getY());
    }
}

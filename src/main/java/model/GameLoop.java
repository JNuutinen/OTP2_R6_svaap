package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static view.GameMain.*;

/**
 * Pelin game loop. Päivittää liikuteltavien spritejen sijainnin ja tarkastelee osumia.
 */
public class GameLoop {

    /**
     * Pelin kontrolleri.
     */
    private Controller controller;

    /**
     * Jono olioista, jotka lisätään Updateable listaan seuraavan loopin alussa.
     */
    private volatile Queue<Updateable> updateableQueue;

    /**
     * Jono olioista, jotka poistetaan Updateable listasta seuraavan loopin alussa.
     */
    private volatile Queue<Updateable> removeUpdateableQueue;

    /**
     * Lista pelissä olevista päivitettävistä olioista, jonka looppi käy läpi.
     */
    private ArrayList<Updateable> updateables;

    /**
     * Spritejen liikuttelusta vastaava AnimationTimer looppi.
     */
    private AnimationTimer mainLoop;

    /**
     * Osumatarkastelujen AnimationTimer looppi.
     */
    private AnimationTimer collisionLoop;

    /**
     * Kerroin, jolla delta timen voi muuttaa nollaksi = peli on pausella. Jos ei pausella, tää saa
     * arvon 1 = ei vaikuta deltatimeen
     */
    private int pauseModifier = 1;


    /**
     * Konstruktori.
     * @param controller Pelin kontrolleri.
     */
    private double deltaTime = 0;

    public GameLoop(Controller controller) {
        this.controller = controller;
        updateableQueue = new LinkedList<>();
        removeUpdateableQueue = new LinkedList<>();
        updateables = new ArrayList<>();
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
     * Pysäyttää pääloopin ja osumatarkasteluloopin.
     */
    public void stopLoops(){
        mainLoop.stop();
        collisionLoop.stop();
    }


    /**
     * Alustaa ja käynnistää pääloopin ja osumatarkasteluloopin.
     */
    public void startLoop(){

        // -------- main looppi
        mainLoop = new AnimationTimer(){

            private long lastUpdate = 0 ;

            double debugger_toSecondCounter = 0;
            int debugger_frameCounter = 0;

            @Override
            public void handle(long now) {
                    deltaTime = ((now - lastUpdate) / 1_000_000_000.0) * pauseModifier;
                    // jos taajuus on alhaisempi kuin asetettu taajuusrajagappi (250 fps)
                    if ((double) (now - lastUpdate) / 1_000_000_000.0 >= 1.0 / 250.0) {
                        // controller.setCurrentFps(1/((now - lastUpdate)/1000000000.0));
                        // Tarkista updateable -jono
                        if (!updateableQueue.isEmpty()) {
                            updateables.addAll(updateableQueue);
                            updateableQueue.clear();
                        }

                        // tarkista poistojono
                        if (!removeUpdateableQueue.isEmpty()) {
                            for (Updateable toBeRemoved : removeUpdateableQueue) {
                                if (updateables.contains(toBeRemoved)) {
                                    updateables.remove(toBeRemoved);
                                }
                            }
                            removeUpdateableQueue.clear();
                        }

                        // Gameloopin taika
                        for (Updateable updateable : updateables) {
                            if (updateable != null) {
                                //paivita rajapintaoliot
                                updateable.update(deltaTime);
                            }
                        }

                        debugger_toSecondCounter += deltaTime;
                        debugger_frameCounter++;
                        if (debugger_toSecondCounter > 1) {
                            controller.setFps(debugger_frameCounter);
                            debugger_toSecondCounter = 0;
                            debugger_frameCounter = 0;
                        }
                        lastUpdate = now;
                    }
                }
        };

        // ------ osumatarkastelun looppi
        collisionLoop = new AnimationTimer(){

            private long lastUpdate = 0 ;

            @Override
            public void handle(long now) {
                    if ((double) (now - lastUpdate) / 1_000_000_000.0 >= 1.0 / 150.0) {
                        // Gameloopin taika
                        for (Updateable updateable : updateables) {
                            if (updateable != null) {
                                //osumistarkastelu
                                //TODO taa on KOVAKOODATTU >:(
                                if (updateable.getTag() == ENEMY_PROJECTILE_TAG) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag() == PLAYER_SHIP_TAG) {
                                                // jos objecktien valinen taisyys on pienempi kuin niiden hitboxien sateiden summa:
                                                if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                        (updateable.getHitboxRadius() + updateable2.getHitboxRadius())) {
                                                    // kutsu objektin collides-metodia
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (updateable.getTag() == PLAYER_PROJECTILE_TAG) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag() == ENEMY_SHIP_TAG || updateable2.getTag() == BOSS_SHIP_TAG) {
                                                if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                        (updateable.getHitboxRadius() + updateable2.getHitboxRadius())) {
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (updateable.getTag() == PLAYER_TRACE_TAG) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag() == ENEMY_SHIP_TAG || updateable2.getTag() == BOSS_SHIP_TAG) {
                                                // jos laser on vihun oikealla puolella (pelaaja ampuu laserin aina oikealle päin)
                                                if (updateable.getPosition().getX() > updateable2.getPosition().getX()) {
                                                    // jos vihun hitboxin säde yltää laseriin
                                                    if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                            (updateable.getHitboxRadius())) {
                                                        updateable.collides(updateable2);
                                                    }
                                                } else if (getDistanceFromTarget(new Point2D(0, updateable.getPosition().getY()),
                                                        new Point2D(0, updateable2.getPosition().getY())) < updateable2.getHitboxRadius()) {
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                        }
                                    }
                                    // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                    updateable.setTag(UNDEFINED_TAG);
                                }
                                if (updateable.getTag() == ENEMY_TRACE_TAG) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag() == PLAYER_SHIP_TAG) {
                                                // jos laser on vihun oikealla puolella (pelaaja ampuu laserin aina oikealle päin)
                                                if (updateable.getPosition().getX() < updateable2.getPosition().getX()) {
                                                    // jos vihun hitboxin säde yltää laseriin
                                                    if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                            (updateable.getHitboxRadius())) {
                                                        updateable.collides(updateable2);
                                                    }
                                                } else if (getDistanceFromTarget(new Point2D(0, updateable.getPosition().getY()),
                                                        new Point2D(0, updateable2.getPosition().getY())) < updateable2.getHitboxRadius()) {
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                        }
                                    }
                                    // vaiha laserin tagi pois jotta se ei enää kerran jälkeen tee vahinkoa, mutta pystyy lävitsemään.
                                    updateable.setTag(UNDEFINED_TAG);
                                }
                            }
                        }
                    }
                }
        };
        mainLoop.start();
        collisionLoop.start();
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

    public double getDeltaTime() {
        return deltaTime;
    }
}

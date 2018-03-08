package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static view.GameMain.*;

public class GameLoop {

    private Controller controller;
    private volatile Queue<Updateable> updateableQueue;
    private volatile Queue<Updateable> removeUpdateableQueue;
    private ArrayList<Updateable> updateables;

    private AnimationTimer mainLoop;
    private AnimationTimer collisionLoop;

    /**
     * Kerroin, jolla delta timen voi muuttaa nollaksi = peli on pausella. Jos ei pausella, tää saa
     * arvon 1 = ei vaikuta deltatimeen
     */
    private int pauseModifier = 1;

    public GameLoop(Controller controller) {
        this.controller = controller;
        updateableQueue = new LinkedList<>();
        removeUpdateableQueue = new LinkedList<>();
        updateables = new ArrayList<>();
    }

    synchronized public void queueUpdateable(Updateable updateable) {
        updateableQueue.add(updateable);
    }

    synchronized public void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }

    public ArrayList<Updateable> getUpdateables(){
        return updateables;
    }

    public void pauseGame() {
        pauseModifier = 0;
    }

    public void continueGame() {
        pauseModifier = 1;
    }

    public void stopLoops(){
        mainLoop.stop();
        collisionLoop.stop();
    }


    // ----- looppien alotusmetodi
    public void startLoop(){

        // -------- main looppi
        mainLoop = new AnimationTimer(){

            private long lastUpdate = 0 ;

            double debugger_toSecondCounter = 0;
            int debugger_frameCounter = 0;

            @Override
            public void handle(long now) {
                double deltaTime = ((now - lastUpdate) / 1_000_000_000.0) * pauseModifier;
                // jos taajuus on alhaisempi kuin asetettu taajuusrajagappi (250 fps)
                if ((double)(now - lastUpdate)/1_000_000_000.0 >= 1.0/250.0) {
                    controller.setCurrentFps(1/((now - lastUpdate)/1000000000.0));
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
                        if (updateable != null){
                            //paivita rajapintaoliot
                            updateable.update(deltaTime);
                        }
                    }

                    debugger_toSecondCounter += deltaTime;
                    debugger_frameCounter++;
                    if(debugger_toSecondCounter > 1){
                        controller.setFps(debugger_frameCounter);
                        debugger_toSecondCounter = 0;
                        debugger_frameCounter = 0;
                    }
                    lastUpdate = now ;
                }
            }
        };

        // ------ osumatarkastelun looppi
        collisionLoop = new AnimationTimer(){

            private long lastUpdate = 0 ;

            @Override
            public void handle(long now) {
                if ((double)(now - lastUpdate)/1_000_000_000.0 >= 1.0/150.0) {
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
                                            if(updateable.getPosition().getX() > updateable2.getPosition().getX()){
                                                // jos vihun hitboxin säde yltää laseriin
                                                if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                        (updateable.getHitboxRadius())) {
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                            else if (getDistanceFromTarget(new Point2D(0, updateable.getPosition().getY()),
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
                                            if(updateable.getPosition().getX() < updateable2.getPosition().getX()){
                                                // jos vihun hitboxin säde yltää laseriin
                                                if (getDistanceFromTarget(updateable.getPosition(), updateable2.getPosition()) <
                                                        (updateable.getHitboxRadius())) {
                                                    updateable.collides(updateable2);
                                                }
                                            }
                                            else if (getDistanceFromTarget(new Point2D(0, updateable.getPosition().getY()),
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

    private double getDistanceFromTarget(Point2D source, Point2D target){
        return Math.sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }
}

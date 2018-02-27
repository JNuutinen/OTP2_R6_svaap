package model;

import controller.Controller;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static view.GameMain.WINDOW_HEIGHT;
import static view.GameMain.WINDOW_WIDTH;

public class GameLoop {

    private GraphicsContext gc;
    private Controller controller;
    private volatile Queue<Updateable> updateableQueue;
    private volatile Queue<Updateable> removeUpdateableQueue;
    private ArrayList<Updateable> updateables;

    private AnimationTimer mainLoop;
    private AnimationTimer collisionLoop;

    public GameLoop(GraphicsContext gc, Controller controller) {
        this.gc = gc;
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

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
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

                    // Clearataan canvas
                    gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
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
                            if (updateable.getTag().equals("projectile_enemy")) {
                                for (Updateable updateable2 : updateables) {
                                    if (updateable != updateable2 && updateable2 != null) {
                                        if (updateable2.getTag().equals("player")) {
                                            if (((Path) Shape.intersect(updateable.getShape(),
                                                    updateable2.getShape())).getElements().size() > 0) {
                                                //updateable.collides(updateable2);
                                            }
                                        }
                                    }

                                }
                            }
                            if (updateable.getTag().equals("projectile_player")) {
                                for (Updateable updateable2 : updateables) {
                                    if (updateable != updateable2 && updateable2 != null) {
                                        if (updateable2.getTag().equals("enemy") || updateable2.getTag().equals("boss")) {
                                            if (((Path) Shape.intersect(updateable.getShape(),
                                                    updateable2.getShape())).getElements().size() > 0) {
                                                //updateable.collides(updateable2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        mainLoop.start();
        collisionLoop.start();
    }
}

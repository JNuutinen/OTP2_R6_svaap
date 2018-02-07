package model;

import javafx.application.Platform;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import view.GameMain;

import java.util.*;

public class GameLoop{

    private volatile static Queue<Updateable> updateableQueue = new LinkedList<>();

    private volatile static Queue<Updateable> removeUpdateableQueue = new LinkedList<>();

    private static ArrayList<Updateable> updateables = new ArrayList<>();

    private Timer timer;


    public GameLoop(GameMain gameMain){
    }

    synchronized public static void queueUpdateable(Updateable updateable) {
        updateableQueue.add(updateable);
    }

    synchronized public static void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }

    public void startLoop(){

        TimerTask timerTask = new TimerTask() {

            long previousTime = System.nanoTime();
            public void run() {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: alkaa pätkimään ja glitchailee rajusti jos nostaa fps. Pitäs selvittää mist johtuu
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;

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

                                //osumistarkastelu
                                //TODO taa on KOVAKOODATTU >:(
                                if(updateable.getTag().equals("projectile_enemy")) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag().equals("player")) {
                                                if (((Path) Shape.intersect(updateable.getSpriteShape(), updateable2.getSpriteShape())).getElements().size() > 0) {
                                                    updateable.collides(updateable2.getUpdateable());
                                                }
                                            }
                                        }

                                    }
                                }
                                if(updateable.getTag().equals("projectile_player")) {
                                    for (Updateable updateable2 : updateables) {
                                        if (updateable != updateable2 && updateable2 != null) {
                                            if (updateable2.getTag().equals("enemy")) {
                                                if (((Path) Shape.intersect(updateable.getSpriteShape(), updateable2.getSpriteShape())).getElements().size() > 0) {
                                                    updateable.collides(updateable2.getUpdateable());
                                                }
                                            }
                                        }

                                    }
                                }

                                //looppaa rajapintaoliot
                                updateable.update(deltaTime);
                            }
                        }

                        previousTime = currentTime;


                        /* -----------HEGE POISTAA TAN
                        while (isLooping)

                        {
                            long currentTime = System.nanoTime();
                            double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;




                            previousTime = currentTime;
                            double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;

                            if (frameTime < targetDelta) {

                                //TODO debuggeri. printtaa nykyisen fps 2 sekunnin valein
                                debugger_secondCounter = debugger_secondCounter + (long) ((targetDelta - frameTime) * 1000);
                                if (debugger_secondCounter > 2000) {
                                    System.out.println("fps: " + (long) (1 / (targetDelta - frameTime))); //talla voi katsoa viime framen fps-nopeus
                                    debugger_secondCounter = debugger_secondCounter - 1000;
                                }

                                try {

                                    Thread.sleep((long) ((targetDelta - frameTime) * 1000));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }*/
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / 60);
        this.timer = new java.util.Timer();
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }
}

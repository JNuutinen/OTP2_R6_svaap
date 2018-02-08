package model;

import javafx.application.Platform;
import javafx.scene.paint.Color;
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

            double debugger_avgFps = 0;
            double debugger_toSecondCounter = 0;
            int debugger_frameCounter = 0;
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

                        //TODO debugger
                        debugger_toSecondCounter += deltaTime;
                        debugger_frameCounter++;
                        if(debugger_toSecondCounter > 1){
                            debugger_avgFps = 1/(debugger_toSecondCounter/debugger_frameCounter);
                            debugger_frameCounter = 0;
                            debugger_toSecondCounter--;
                            if(debugger_avgFps < 61){
                                GameMain.debugger_fps.setTextFill(Color.web("#f44242"));
                            }
                            else if(debugger_avgFps > 61){
                                GameMain.debugger_fps.setTextFill(Color.web("#3d3d3d"));
                            }
                            GameMain.debugger_fps.setText("avg. fps of last second: " + debugger_avgFps);
                        }

                        previousTime = currentTime;

                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / 60);
        this.timer = new java.util.Timer();
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }
}

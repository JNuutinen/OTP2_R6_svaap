package model;

import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import view.GameMain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameLoop extends Thread {

    private volatile static Queue<Updateable> updateableQueue = new LinkedList<>();

    private volatile static Queue<Updateable> removeUpdateableQueue = new LinkedList<>();

    private static ArrayList<Updateable> updateables = new ArrayList<>();

    private boolean isLooping = false;

    public GameLoop(GameMain gameMain){
    }

    synchronized public static void queueUpdateable(Updateable updateable) {
        updateableQueue.add(updateable);
    }

    synchronized public static void removeUpdateable(Updateable updateable) {
        removeUpdateableQueue.add(updateable);
    }

    @Override
    public void run() {
        isLooping = true;
        // TODO: alkaa pätkimään ja glitchailee rajusti jos nostaa fps. Pitäs selvittää mist johtuu
        final double targetDelta = 0.0333; // 33.3ms ~ 30fps | 16.6ms ~ 60fps | 8.3ms ~ 129fps
        long previousTime = System.nanoTime();
        long debugger_secondCounter = 0;//TODO debuggeri
        while (isLooping) {
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

            //------------------paivita kaikki updateable-rajapintaluokat------------------
            for(Updateable updateable : updateables){


                if(updateable != null) {
                    for (Updateable updateable2 : updateables) {
                        if (((Path) Shape.intersect(updateable.getSpriteShape(), updateable2.getSpriteShape())).getElements().size() > 0
                                && updateable != updateable2 && updateable2 != null) {
                            updateable.collides(updateable2.getUpdateable());
                        }
                    }
                }

                updateable.update(deltaTime);
            }

            //-----------------osumatarkastaja---------------------------------------------


            previousTime = currentTime;
            double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;

            if (frameTime < targetDelta) {

                //TODO debuggeri. printtaa nykyisen fps 2 sekunnin valein
                debugger_secondCounter = debugger_secondCounter + (long)((targetDelta - frameTime) * 1000);
                if(debugger_secondCounter > 2000){
                    System.out.println("fps: " + (long)(1/(targetDelta - frameTime))); //talla voi katsoa viime framen fps-nopeus
                    debugger_secondCounter = debugger_secondCounter-1000;
                }

                try {

                    Thread.sleep( (long) ((targetDelta - frameTime) * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeThread(){
        isLooping = false;
    }
}

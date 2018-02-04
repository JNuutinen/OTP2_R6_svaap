package model;

import view.GameMain;

import java.util.*;

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
        final double targetDelta = 0.0166; // 33.3ms ~ 30fps | 16.6ms ~ 60fps | 8.3ms ~ 129fps
        long previousTime = System.nanoTime();

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
                updateable.update(deltaTime);
            }

            previousTime = currentTime;
            double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;
            if (frameTime < targetDelta) {
                try {
                    System.out.println((long)((targetDelta - frameTime) * 1000)); //talla voi katsoa ajan (ms) framejen valissa
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

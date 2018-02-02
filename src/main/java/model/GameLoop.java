package model;

import view.GameMain;

import java.util.ArrayList;

public class GameLoop extends Thread {

    public static ArrayList<Updateable> updateables = new ArrayList<Updateable>();
    private GameMain gameMain;
    private boolean isLooping = false;

    public GameLoop(GameMain gameMain){
        this.gameMain = gameMain;
    }


    @Override
    public void run() {
        isLooping = true;
        final double targetDelta = 0.0166; // 16.6ms ~ 60fps | 8.3 ~ 129fps
        long previousTime = System.nanoTime();

        while (isLooping) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;

            //------------------paivita kaikki updateable-rajapintaluokat------------------
            for(Updateable updateable : updateables){
                updateable.update(deltaTime);
            }

            previousTime = currentTime;
            double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;
            if (frameTime < targetDelta) {
                try {
                    //System.out.println((long)((targetDelta - frameTime) * 1000)); //talla voi katsoa ajan (ms) framejen valissa
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
